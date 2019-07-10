package terramisc.tileentities;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Items.Tools.ItemCustomBucketMilk;
import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.TFCItems;
import com.bioxx.tfc.api.Enums.EnumFuelMaterial;
import com.bioxx.tfc.api.TileEntities.TEFireEntity;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import terramisc.api.crafting.VatManager;
import terramisc.api.crafting.VatRecipe;
import terramisc.api.crafting.VatRecipeiFoF;

public class TEVat extends TEFireEntity implements IInventory
{
	public byte rotation;
	
	public int mode;
	public static final int MODE_IN = 0;
	public static final int MODE_OUT = 1;
	
	public ItemStack[] storage = new ItemStack[6];
	public FluidStack fluid;
	
	private int processTimer; //Tracks recipe processing time
	
	public static final int FUELSLOT_INPUT = 0; //Slots 0-3 hold fuel items
	public static final int CRAFTINGSLOT_INPUT = 4;
	public static final int CRAFTINGSLOT_OUTPUT = 5;
	
	private TEFireEntity fire; //TE used as the basis for TEFirepit and TEForge.
	
	public VatRecipe recipe;
	
	//temporary field. No need to save
	public boolean shouldDropItem = true;
	
	/**
	 * Crafting TE for combining items and fluids at specific temperatures over a period of time.
	 */
	public TEVat()
	{
		fuelTimeLeft = 375;
		fuelBurnTemp =  613;
		fireTemp = 350;
		maxFireTempScale = 2000;
	}

	/**
	 * Root Method for controlling TE Behavior
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void updateEntity()
	{
		if(!worldObj.isRemote)
		{
			//Create a list of all the items that are falling onto the firepit
			List list = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1.1, zCoord + 1));
			
			if(list != null && !list.isEmpty() && storage[FUELSLOT_INPUT] == null) //Only go through the list if more fuel can fit.
			{
				//Iterate through the list and check for logs and peat
				for(Iterator iterator = list.iterator(); iterator.hasNext();)
				{
					EntityItem entity = (EntityItem) iterator.next();
					ItemStack is = entity.getEntityItem();
					Item item = is.getItem();

					if(item == TFCItems.logs || item == Item.getItemFromBlock(TFCBlocks.peat))
					{
						for(int c = 0; c < is.stackSize; c++)
						{
							if(storage[FUELSLOT_INPUT] == null) //Secondary check for empty fuel input slot.
							{
								/**
								 * Place a copy of only one of the logs into the fuel slot, due to the stack limitation of the fuel slots.
								 * Do not change to storage[0] = is;
								 */
								setInventorySlotContents(1, new ItemStack(item, 1, is.getItemDamage()));
								is.stackSize--;
								handleFuelStack(); // Attempt to shift the fuel down so more fuel can be added within the same for loop.
							}
						}

						if(is.stackSize == 0)
							entity.setDead();
					}
				}
			}
			
			//push the input fuel down the stack
			handleFuelStack();
			
			if(fireTemp < 1 && worldObj.getBlockMetadata(xCoord, yCoord, zCoord) != 0)
			{
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			else if(fireTemp >= 1 && worldObj.getBlockMetadata(xCoord, yCoord, zCoord) != 1)
			{
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 3);
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}

			//If the fire is still burning and has fuel
			if(fuelTimeLeft > 0 && fireTemp >= 1)
			{
				if(worldObj.getBlockMetadata(xCoord, yCoord, zCoord) != 2)
				{
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 2, 3);
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			}
			else if(fuelTimeLeft <= 0 && fireTemp >= 1 && storage[3] != null && !TFC_Core.isExposedToRain(worldObj, xCoord, yCoord, zCoord))
			{
				if(storage[3] != null)
				{
					EnumFuelMaterial m = TFC_Core.getFuelMaterial(storage[3]);
					fuelTasteProfile = m.ordinal();
					storage[3] = null;
					fuelTimeLeft = m.burnTimeMax;
					fuelBurnTemp = m.burnTempMax;
				}
			}

			//Calculate the fire temp
			float desiredTemp = handleTemp();

			handleTempFlux(desiredTemp);

			//Here we handle the bellows
			handleAirReduction();
			
			if(fuelTimeLeft <= 0)
				TFC_Core.handleItemTicking(this, worldObj, xCoord, yCoord, zCoord);
			
			//Handle food decay for the inventory.
			TFC_Core.handleItemTicking(this, this.worldObj, xCoord, yCoord, zCoord);
			
			//We only want to bother ticking food once per 5 seconds to keep overhead low.
			processTimer++;
			if(processTimer > 100)
			{
				processItems();
				processTimer = 0;
			}
			
			/*Here we handle item stacks that are too big for MC to handle such as when making mortar.
			//If the stack is > its own max stack size then we split it and add it to the invisible solid storage area or 
			//spawn the item in the world if there is no room left.
			if (this.getFluidLevel() > 0 && getInputStack() != null)
			{
				int count = 1;
				while(this.getInputStack().stackSize > getInputStack().getMaxStackSize())
				{
					ItemStack is = getInputStack().splitStack(getInputStack().getMaxStackSize());
					if(count < this.storage.length && this.getStackInSlot(count) == null)
					{
						this.setInventorySlotContents(count, is);
					}
					else
					{
						worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, is));
					}
					count++;
				}
			}
			*/

			//Reset our fluid if all of the liquid is gone.
			if(fluid != null && fluid.amount == 0)
				fluid = null;

			//Handle adding fluids to the barrel if the barrel is currently in input mode.
			if(mode == MODE_IN)
			{
				ItemStack container = getInputStack();
				FluidStack inLiquid = FluidContainerRegistry.getFluidForFilledItem(container);

				if(container != null && container.getItem() instanceof IFluidContainerItem)
				{
					FluidStack isfs = ((IFluidContainerItem)container.getItem()).getFluid(container);
					if(isfs != null && addLiquid(isfs))
					{
						((IFluidContainerItem) container.getItem()).drain(container, ((IFluidContainerItem)container.getItem()).getCapacity(container), true);
					}
				}
				else if (inLiquid != null && container != null && container.stackSize == 1)
				{
					if(addLiquid(inLiquid))
					{
						this.setInventorySlotContents(CRAFTINGSLOT_INPUT, FluidContainerRegistry.drainFluidContainer(container));
					}
				}
			}
			//Drain liquid from the barrel to a container if the barrel is in output mode.
			else if(mode == MODE_OUT)
			{
				ItemStack container = getInputStack();

				if(container != null && fluid != null && container.getItem() instanceof IFluidContainerItem)
				{
					FluidStack isfs = ((IFluidContainerItem)container.getItem()).getFluid(container);
					if(isfs == null || fluid.isFluidEqual(isfs))
					{
						fluid.amount -= ((IFluidContainerItem) container.getItem()).fill(container, fluid, true);
						if(fluid.amount == 0)
							fluid = null;
					}
				}
				else if(FluidContainerRegistry.isEmptyContainer(container))
				{
					ItemStack fullContainer = this.removeLiquid(getInputStack());
					if (fullContainer.getItem() == TFCItems.woodenBucketMilk)
					{
						ItemCustomBucketMilk.createTag(fullContainer, 20f);
					}
					this.setInventorySlotContents(CRAFTINGSLOT_INPUT, fullContainer);
				}
			}
		}
	}
	
	/**
	 * TODO Finish development of this method
	 */
	public void processItems()
	{
		if(this.getInvCount() == 0)
		{
			if(getFluidStack() != null)
			{
				if(canProcess() && !worldObj.isRemote)
				{
					int time = 0;

					//Make sure that the recipe meets the time requirements
					if(time < recipe.cookTime)
						return;

					ItemStack origIS = getInputStack() != null ? getInputStack().copy() : null;
					FluidStack origFS = getFluidStack() != null ? getFluidStack().copy() : null;
					if(fluid.isFluidEqual(recipe.getResultFluid(origIS, origFS, time)) && recipe.removesLiquid)
					{
						fluid.amount -= recipe.getResultFluid(origIS, origFS, time).amount;
					}
					else
					{
						this.fluid = recipe.getResultFluid(origIS, origFS, time).copy();
						if(fluid != null && !(recipe instanceof VatRecipeiFoF) && origFS != null)
							this.fluid.amount = origFS.amount;

						worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					}

					Stack<ItemStack> resultStacks = recipe.getResult(origIS, origFS, time);
					if(!resultStacks.isEmpty())
					{
						ItemStack result = resultStacks.pop();
						storage[CRAFTINGSLOT_OUTPUT] = result;

						for (int i = 1; i < storage.length; i++)
						{
							if (storage[i] == null && !resultStacks.isEmpty())
								this.setInventorySlotContents(i, resultStacks.pop());
						}

						while (!resultStacks.isEmpty())
							worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, resultStacks.pop()));

						this.setInventorySlotContents(5, result);
					}
				}
			}
		}
	}
	
	/**
	 * Handles logic to determine if conditions for a valid recipe are present.
	 * @return True if a recipe process should proceed.
	 */
	public boolean canProcess()
	{
		recipe = VatManager.getInstance().findMatchingRecipe(getInputStack(), getFluidStack());
		
		if(recipe == null || !(this.fireTemp > 0))
		{
			//If a valid recipe isn't found or the vat is not being heated.
			return false;
		}
		else //Valid recipe and vat is being heated
		{
			//Item checks
			
			ItemStack outputIS = recipe.getRecipeOutIS();
			Item outputItem = outputIS.getItem();
			
			ItemStack inputIS = recipe.getRecipeInIS();
			Item inputItem = inputIS.getItem();
			
			/* Output slot must be empty or match output item
			 * We also check to see if the output item stack will fit in the slot.
			 * Lastly the combined stack size must also be able to fit in the output slot itself.
			 */
			if(outputItem != null)
				if(storage[CRAFTINGSLOT_OUTPUT] != null)
					if(storage[CRAFTINGSLOT_OUTPUT].getItem() != outputItem)
					{
						int combinedStackSize = (storage[CRAFTINGSLOT_OUTPUT].stackSize + outputIS.stackSize);
						if(combinedStackSize > outputIS.getMaxStackSize() || combinedStackSize > this.getInventoryStackLimit())
							return false;
					}
			
			//If the recipe does not require an item, one cannot still be provided.
			if(inputItem == null && this.storage[CRAFTINGSLOT_INPUT] != null)
				return false;
			
			FluidStack outputFluidStack = recipe.getRecipeOutFluid();
			Fluid outputFluid = outputFluidStack.getFluid();
			
			FluidStack inputFluidStack = recipe.getRecipeInFluid();
			Fluid inputFluid = inputFluidStack.getFluid();
			
			//Fluid checks
			
			//Fluid in vat and fluid required must be the same
			if(this.getFluidStack().getFluid() != inputFluid)
				return false;
			
			//If the fluid is unchanged by the recipe
			if(inputFluid == outputFluid)
			{
				//Fluid in vat must be equal to or greater than the amount required for the recipe
				if(this.getFluidStack().amount < inputFluidStack.amount)
					return false;
			}
			else //If the fluid is changed by the recipe
			{
				//All of the orginal fluid must be consumed in the recipe
				if(this.getFluidStack().amount - inputFluidStack.amount != 0)
					return false;
			}
			
			//Temperature
			int temp = (int) this.fireTemp;
			int recipeTemp = recipe.getRecipeTemperature();
			
			//Vat must be as hot or hotter than what the recipe requires.
			if(!(temp >= recipeTemp))
				return false;
			
			//If items, fluids, and temperature all check out our recipe is valid.
			return true;
		}
	}
	
	@Override
	public int getSizeInventory() 
	{
		return storage.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		return storage[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) 
	{
		if(storage[slot] != null)
		{
			if(storage[slot].stackSize <= amount)
			{
				ItemStack itemstack = storage[slot];
				storage[slot] = null;
				return itemstack;
			}
			ItemStack itemstack1 = storage[slot].splitStack(amount);
			if(storage[slot].stackSize == 0)
				storage[slot] = null;
			return itemstack1;
		}
		else
			return null;
	}
	
	public void ejectContents()
	{
		float f3 = 0.05F;
		EntityItem entityitem;
		Random rand = new Random();
		float f = rand.nextFloat() * 0.8F + 0.1F;
		float f1 = rand.nextFloat() * 0.8F + 0.3F;
		float f2 = rand.nextFloat() * 0.8F + 0.1F;

		for (int i = 0; i < getSizeInventory(); i++)
		{
			if(storage[i]!= null)
			{
				entityitem = new EntityItem(worldObj, xCoord + f, yCoord + f1, zCoord + f2, storage[i]);
				entityitem.motionX = (float)rand.nextGaussian() * f3;
				entityitem.motionY = (float)rand.nextGaussian() * f3 + 0.2F;
				entityitem.motionZ = (float)rand.nextGaussian() * f3;
				worldObj.spawnEntityInWorld(entityitem);
				storage[i] = null;
			}
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) 
	{
		return null;
	}

	public int getInvCount()
	{
		int count = 0;
		for(ItemStack is : storage)
		{
			if(is != null)
				count++;
		}
		if(storage[CRAFTINGSLOT_INPUT] != null && count == 1)
			return 0;
		return count;
	}
	
	public void handleFuelStack()
	{
		int slot = FUELSLOT_INPUT;
		int i = slot+1;
		int j = slot+2;
		int k = slot+3;
		
		if(storage[i] == null && storage[slot] != null)
		{
			storage[i] = storage[slot];
			storage[slot] = null;
		}
		if(storage[j] == null && storage[i] != null)
		{
			storage[j] = storage[i];
			storage[i] = null;
		}
		if(storage[k] == null && storage[j] != null)
		{
			storage[k] = storage[j];
			storage[j] = null;
		}
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack is) 
	{
		if(!ItemStack.areItemStacksEqual(storage[i], is))
		{
			storage[i] = is;
			if(i == 0)
			{
				processItems();
			}
		}
	}

	@Override
	public String getInventoryName() 
	{
		return "Vat";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) 
	{
		return false;
	}

	@Override
	public void openInventory() 
	{
	}

	@Override
	public void closeInventory() 
	{		
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack is) 
	{
		return false;
	}
	
	public int getFluidLevel()
	{
		if(fluid != null)
			return fluid.amount;
		return 0;
	}
	
	public ItemStack getInputStack()
	{
		return storage[CRAFTINGSLOT_INPUT];
	}
	
	public FluidStack getFluidStack()
	{
		return this.fluid;
	}
	
	public int getMaxLiquid()
	{
		return 5000;
	}

	public boolean addLiquid(FluidStack inFS)
	{
		if (inFS != null)
		{
			//Prevent Liquids that are hotter than the melting point of pewter from being added.
			if (inFS.getFluid() != null && inFS.getFluid().getTemperature(inFS) > 500)
				return false;

			if (fluid == null)
			{
				fluid = inFS.copy();
				if (fluid.amount > this.getMaxLiquid())
				{
					fluid.amount = getMaxLiquid();
					inFS.amount = inFS.amount - this.getMaxLiquid();

				}
				else
					inFS.amount = 0;
			}
			else
			{
				//check if the barrel is full or if the fluid being added does not match the barrel liquid
				if (fluid.amount == getMaxLiquid() || !fluid.isFluidEqual(inFS))
					return false;

				int a = fluid.amount + inFS.amount - getMaxLiquid();
				fluid.amount = Math.min(fluid.amount + inFS.amount, getMaxLiquid());
				if (a > 0)
					inFS.amount = a;
				else
					inFS.amount = 0;
			}
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			return true;
		}

		return false;
	}

	public ItemStack addLiquid(ItemStack is)
	{
		if(is == null || is.stackSize > 1)
			return is;
		if(FluidContainerRegistry.isFilledContainer(is))
		{
			FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(is);
			if(addLiquid(fs))
			{
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				return FluidContainerRegistry.drainFluidContainer(is);
			}
		}
		else if(is.getItem() instanceof IFluidContainerItem)
		{
			FluidStack isfs = ((IFluidContainerItem) is.getItem()).getFluid(is);
			if(isfs != null && addLiquid(isfs))
			{
				((IFluidContainerItem) is.getItem()).drain(is, is.getMaxDamage(), true);
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}
		return is;
	}

	/**
	 * This attempts to remove a portion of the water in this container and put it into a valid Container Item
	 */
	public ItemStack removeLiquid(ItemStack is)
	{
		if(is == null || is.stackSize > 1)
			return is;
		if(FluidContainerRegistry.isEmptyContainer(is))
		{
			ItemStack out = FluidContainerRegistry.fillFluidContainer(fluid, is);
			if(out != null)
			{
				FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(out);
				fluid.amount -= fs.amount;
				is = null;
				if(fluid.amount == 0)
				{
					fluid = null;
				}
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				return out;
			}
		}
		else if(fluid != null && is.getItem() instanceof IFluidContainerItem)
		{
			FluidStack isfs = ((IFluidContainerItem) is.getItem()).getFluid(is);
			if(isfs == null || fluid.isFluidEqual(isfs))
			{
				fluid.amount -= ((IFluidContainerItem) is.getItem()).fill(is, fluid, true);
				if(fluid.amount == 0)
					fluid = null;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}
		return is;
	}

	/**
	 * This removes a specified amount of liquid from the container and updates the block.
	 */
	public void drainLiquid(int amount)
	{
		if(this.getFluidStack() != null)
		{
			this.getFluidStack().amount -= amount;
			if(this.getFluidStack().amount <= 0)
				this.actionEmpty();
			else
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	public int getLiquidScaled(int i)
	{
		if(fluid != null)
			return fluid.amount * i/getMaxLiquid();
		return 0;
	}
	
	public void actionEmpty()
	{
		fluid = null;
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setByte("fluidID", (byte)-1);
		this.broadcastPacketInRange(this.createDataPacket(nbt));
	}
	
	public void actionMode()
	{
		mode = mode == 0 ? 1 : 0;
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setByte("mode", (byte)mode);
		this.broadcastPacketInRange(this.createDataPacket(nbt));
	}
	
	public ResourceLocation getTexture() //Sets texture for TESR
	{
		this.fire = (TEFireEntity) worldObj.getTileEntity(xCoord, yCoord-1, zCoord);
		
		if(this.fire != null && this.fire.fireTemp >= 1F)
		{
			return new ResourceLocation("tfcm:textures/blocks/models/Lead Pewter Vat_Lit.png");
		}
		
		return new ResourceLocation("tfcm:textures/blocks/models/Lead Pewter Vat.png");
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);
		
		fluid = FluidStack.loadFluidStackFromNBT(nbttagcompound.getCompoundTag("fluidNBT"));
		rotation = nbttagcompound.getByte("rotation");
		
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
		storage = new ItemStack[getSizeInventory()];
		for(int i = 0; i < nbttaglist.tagCount(); i++)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if(byte0 >= 0 && byte0 < storage.length)
				storage[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);
		
		NBTTagCompound fluidNBT = new NBTTagCompound();
		if(fluid != null)
			fluid.writeToNBT(fluidNBT);
		nbttagcompound.setTag("fluidNBT", fluidNBT);
		nbttagcompound.setByte("rotation", rotation);
		
		NBTTagList nbttaglist = new NBTTagList();
		for(int i = 0; i < storage.length; i++)
		{
			if(storage[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				storage[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbttagcompound.setTag("Items", nbttaglist);
	}
	
	public void updateGui()
	{
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void handleInitPacket(NBTTagCompound nbt) 
	{
		this.rotation = nbt.getByte("rotation");
		if(nbt.getInteger("fluid") != -1)
		{
			if(fluid != null)
				fluid.amount = nbt.getInteger("fluidAmount");
			else
				fluid = new FluidStack(nbt.getInteger("fluid"), nbt.getInteger("fluidAmount"));
		}
		else
		{
			fluid = null;
		}
		this.worldObj.func_147479_m(xCoord, yCoord, zCoord);
	}

	@Override
	public void createInitNBT(NBTTagCompound nbt) 
	{
		nbt.setByte("rotation", rotation);
		nbt.setInteger("fluid", fluid != null ? fluid.getFluidID() : -1);
		nbt.setInteger("fluidAmount", fluid != null ? fluid.amount : 0);
	}

	@Override
	public void handleDataPacket(NBTTagCompound nbt)
	{
		if(nbt.hasKey("fluidID"))
		{
			if(nbt.getByte("fluidID") == -1)
				fluid = null;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		if(!worldObj.isRemote)
		{	
			if(nbt.hasKey("mode"))
			{
				mode = nbt.getByte("mode");
			}
		}
	}
}
