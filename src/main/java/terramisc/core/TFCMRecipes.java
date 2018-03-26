package terramisc.core;

import java.util.List;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import terramisc.api.crafting.BarrelFermentationRecipe;
import terramisc.api.crafting.FruitPressManager;
import terramisc.api.crafting.FruitPressRecipe;

import com.bioxx.tfc.Core.Recipes;
import com.bioxx.tfc.Food.ItemFoodTFC;
import com.bioxx.tfc.api.HeatIndex;
import com.bioxx.tfc.api.HeatRaw;
import com.bioxx.tfc.api.HeatRegistry;
import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.TFCFluids;
import com.bioxx.tfc.api.TFCItems;
import com.bioxx.tfc.api.Constant.Global;
import com.bioxx.tfc.api.Crafting.AnvilManager;
import com.bioxx.tfc.api.Crafting.AnvilRecipe;
import com.bioxx.tfc.api.Crafting.AnvilReq;
import com.bioxx.tfc.api.Crafting.BarrelAlcoholRecipe;
import com.bioxx.tfc.api.Crafting.BarrelLiquidToLiquidRecipe;
import com.bioxx.tfc.api.Crafting.BarrelManager;
import com.bioxx.tfc.api.Crafting.BarrelRecipe;
import com.bioxx.tfc.api.Crafting.BarrelVinegarRecipe;
import com.bioxx.tfc.api.Crafting.CraftingManagerTFC;
import com.bioxx.tfc.api.Crafting.KilnCraftingManager;
import com.bioxx.tfc.api.Crafting.KilnRecipe;
import com.bioxx.tfc.api.Crafting.PlanRecipe;
import com.bioxx.tfc.api.Crafting.QuernManager;
import com.bioxx.tfc.api.Crafting.QuernRecipe;
import com.bioxx.tfc.api.Enums.RuleEnum;

import cpw.mods.fml.common.registry.GameRegistry;

public class TFCMRecipes
{	
	public static final int WILDCARD_VALUE = OreDictionary.WILDCARD_VALUE;
	
	private static AnvilManager anvilManager = AnvilManager.getInstance();
	//private static BarrelManager barrelManager = BarrelManager.getInstance();
	private static CraftingManagerTFC craftingManager = CraftingManagerTFC.getInstance();
	//private static KilnCraftingManager kilnCraftingManager = KilnCraftingManager.getInstance();
	//private static QuernManager quernManager = QuernManager.getInstance();
	private static HeatRegistry heatmanager = HeatRegistry.getInstance();

	// Plan values
	public static String PlanNameCasing = "casing";
	public static String PlanNameClockGear= "clockgear";
	public static String PlanNameCircuit= "circuit";
	public static String PlanNameHalberd= "halberd";
	public static String PlanNameWarHammer= "warhammer";
	public static String PlanNamePoniard= "poniard";
	public static String PlanNameCoil= "coil";
	public static String PlanNameLink= "link";
	public static String PlanNameBolt= "bolt";
	public static String PlanNameArrow= "arrow";

	public static void initialise()
	{
		System.out.println("[" + TFCMDetails.ModName + "] Registering Recipes");
		
		registerRecipes();
		registerBarrelRecipes();
		registerFruitPressRecipes();
		registerKilnRecipes();
		registerToolMolds();
		registerKnappingRecipes();
		registerQuernRecipes();
		registerHeatingRecipes();
		
		System.out.println("[" + TFCMDetails.ModName + "] Done Registering Recipes");
	}

	public static void initialiseAnvil()
	{
		// check if the plans/recipes have already been initialized.
		if (TFCMRecipes.areAnvilRecipesInitialised()) return;
		
		System.out.println("[" + TFCMDetails.ModName + "] Registering Anvil Recipes");

		registerAnvilPlans();
		registerAnvilRecipes();
		
		System.out.println("[" + TFCMDetails.ModName + "] Done Registering Anvil Recipes");
	}
	 
	public static boolean areAnvilRecipesInitialised() 
	{ 
	        Map<String, PlanRecipe> map = anvilManager.getPlans(); 
	        
	        return map != null && ( map.containsKey(PlanNameCasing ) || 
							        map.containsKey(PlanNameClockGear) ||
							        map.containsKey(PlanNameCircuit) ||
							        map.containsKey(PlanNameHalberd) ||
							        map.containsKey(PlanNameWarHammer) ||
							        map.containsKey(PlanNamePoniard) ||
							        map.containsKey(PlanNameCoil) ||
							        map.containsKey(PlanNameLink) ||
							        map.containsKey(PlanNameBolt) ||
							        map.containsKey(PlanNameArrow)
	        						); 
	} 
	 
	private static void registerAnvilPlans()
	{
		//Plans
		anvilManager.addPlan(PlanNameCasing , new PlanRecipe(new RuleEnum[] {RuleEnum.HITLAST, RuleEnum.BENDSECONDFROMLAST, RuleEnum.HITTHIRDFROMLAST}));
		anvilManager.addPlan(PlanNameClockGear, new PlanRecipe(new RuleEnum[] {RuleEnum.PUNCHLAST, RuleEnum.PUNCHSECONDFROMLAST, RuleEnum.UPSETTHIRDFROMLAST}));
		anvilManager.addPlan(PlanNameCircuit, new PlanRecipe(new RuleEnum[] {RuleEnum.PUNCHLAST, RuleEnum.PUNCHSECONDFROMLAST, RuleEnum.PUNCHTHIRDFROMLAST}));
		anvilManager.addPlan(PlanNameHalberd, new PlanRecipe(new RuleEnum[] {RuleEnum.BENDLAST, RuleEnum.PUNCHSECONDFROMLAST, RuleEnum.HITTHIRDFROMLAST}));
		anvilManager.addPlan(PlanNameWarHammer, new PlanRecipe(new RuleEnum[] {RuleEnum.BENDLAST, RuleEnum.HITSECONDFROMLAST, RuleEnum.HITTHIRDFROMLAST}));
		anvilManager.addPlan(PlanNamePoniard, new PlanRecipe(new RuleEnum[] {RuleEnum.HITLAST, RuleEnum.BENDSECONDFROMLAST, RuleEnum.ANY}));
		anvilManager.addPlan(PlanNameCoil, new PlanRecipe(new RuleEnum[] {RuleEnum.HITLAST, RuleEnum.BENDSECONDFROMLAST, RuleEnum.DRAWTHIRDFROMLAST}));
		anvilManager.addPlan(PlanNameLink, new PlanRecipe(new RuleEnum[] {RuleEnum.PUNCHLAST, RuleEnum.ANY, RuleEnum.ANY}));
		anvilManager.addPlan(PlanNameBolt, new PlanRecipe(new RuleEnum[] {RuleEnum.HITLAST, RuleEnum.PUNCHSECONDFROMLAST, RuleEnum.ANY}));
		anvilManager.addPlan(PlanNameArrow, new PlanRecipe(new RuleEnum[] {RuleEnum.HITLAST, RuleEnum.HITSECONDFROMLAST, RuleEnum.PUNCHTHIRDFROMLAST}));
				
	}
	
	private static void registerAnvilRecipes()
	{	
		//Recipes
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.brassIngot), null, PlanNameCasing , AnvilReq.COPPER, new ItemStack(TFCMItems.itemCasingBrass, 1)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronIngot), null, PlanNameCasing , AnvilReq.WROUGHTIRON, new ItemStack(TFCMItems.itemCasingIron, 1)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.roseGoldIngot), null, PlanNameCasing , AnvilReq.COPPER, new ItemStack(TFCMItems.itemCasingRoseGold, 1)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronIngot), null, PlanNameClockGear, AnvilReq.WROUGHTIRON, new ItemStack(TFCMItems.itemGear, 2)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
		
		if(TFCMOptions.enableCraftingLogicTiles = true)
		{
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.goldSheet), new ItemStack(Items.redstone), PlanNameCircuit, AnvilReq.COPPER, new ItemStack(TFCMItems.itemCircuit, 1)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
		}
		if(TFCMOptions.enableCraftingCrossbow = true)
		{
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bismuthBronzeIngot), null, PlanNameBolt, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemBolt_BismuthBronze_Head, 4)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blackBronzeIngot), null, PlanNameBolt, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemBolt_BlackBronze_Head, 4)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blackSteelIngot), null, PlanNameBolt, AnvilReq.BLACKSTEEL, new ItemStack(TFCMItems.itemBolt_BlackSteel_Head, 4)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blueSteelIngot), null, PlanNameBolt, AnvilReq.BLUESTEEL, new ItemStack(TFCMItems.itemBolt_BlueSteel_Head, 4)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bronzeIngot), null, PlanNameBolt, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemBolt_Bronze_Head, 4)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.copperIngot), null, PlanNameBolt, AnvilReq.COPPER, new ItemStack(TFCMItems.itemBolt_Copper_Head, 4)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.redSteelIngot), null, PlanNameBolt, AnvilReq.BLUESTEEL, new ItemStack(TFCMItems.itemBolt_RedSteel_Head, 4)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.steelIngot), null, PlanNameBolt, AnvilReq.STEEL, new ItemStack(TFCMItems.itemBolt_Steel_Head, 4)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronIngot), null, PlanNameBolt, AnvilReq.WROUGHTIRON, new ItemStack(TFCMItems.itemBolt_WroughtIron_Head, 4)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		}
		if(TFCMOptions.enableCraftingLongbow = true)
		{
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bismuthBronzeIngot), null, PlanNameArrow, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemArrow_BismuthBronze_Head, 4)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blackBronzeIngot), null, PlanNameArrow, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemArrow_BlackBronze_Head, 4)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blackSteelIngot), null, PlanNameArrow, AnvilReq.BLACKSTEEL, new ItemStack(TFCMItems.itemArrow_BlackSteel_Head, 4)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blueSteelIngot), null, PlanNameArrow, AnvilReq.BLUESTEEL, new ItemStack(TFCMItems.itemArrow_BlueSteel_Head, 4)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bronzeIngot), null, PlanNameArrow, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemArrow_Bronze_Head, 4)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.copperIngot), null, PlanNameArrow, AnvilReq.COPPER, new ItemStack(TFCMItems.itemArrow_Copper_Head, 4)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.redSteelIngot), null, PlanNameArrow, AnvilReq.BLUESTEEL, new ItemStack(TFCMItems.itemArrow_RedSteel_Head, 4)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.steelIngot), null, PlanNameArrow, AnvilReq.STEEL, new ItemStack(TFCMItems.itemArrow_Steel_Head, 4)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronIngot), null, PlanNameArrow, AnvilReq.WROUGHTIRON, new ItemStack(TFCMItems.itemArrow_WroughtIron_Head, 4)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		}
			//Halberds
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bismuthBronzeIngot2x), null, PlanNameHalberd, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemHalberd_BismuthBronze_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blackBronzeIngot2x), null, PlanNameHalberd, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemHalberd_BlackBronze_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blackSteelIngot2x), null, PlanNameHalberd, AnvilReq.BLACKSTEEL, new ItemStack(TFCMItems.itemHalberd_BlackSteel_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blueSteelIngot2x), null, PlanNameHalberd, AnvilReq.BLUESTEEL, new ItemStack(TFCMItems.itemHalberd_BlueSteel_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bronzeIngot2x), null, PlanNameHalberd, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemHalberd_Bronze_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.copperIngot2x), null, PlanNameHalberd, AnvilReq.COPPER, new ItemStack(TFCMItems.itemHalberd_Copper_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.redSteelIngot2x), null, PlanNameHalberd, AnvilReq.BLUESTEEL, new ItemStack(TFCMItems.itemHalberd_RedSteel_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.steelIngot2x), null, PlanNameHalberd, AnvilReq.STEEL, new ItemStack(TFCMItems.itemHalberd_Steel_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronIngot2x), null, PlanNameHalberd, AnvilReq.WROUGHTIRON, new ItemStack(TFCMItems.itemHalberd_WroughtIron_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			//War Hammers
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bismuthBronzeIngot2x), null, PlanNameWarHammer, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemWarHammer_BismuthBronze_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blackBronzeIngot2x), null, PlanNameWarHammer, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemWarHammer_BlackBronze_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blackSteelIngot2x), null, PlanNameWarHammer, AnvilReq.BLACKSTEEL, new ItemStack(TFCMItems.itemWarHammer_BlackSteel_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blueSteelIngot2x), null, PlanNameWarHammer, AnvilReq.BLUESTEEL, new ItemStack(TFCMItems.itemWarHammer_BlueSteel_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bronzeIngot2x), null, PlanNameWarHammer, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemWarHammer_Bronze_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.copperIngot2x), null, PlanNameWarHammer, AnvilReq.COPPER, new ItemStack(TFCMItems.itemWarHammer_Copper_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.redSteelIngot2x), null, PlanNameWarHammer, AnvilReq.BLUESTEEL, new ItemStack(TFCMItems.itemWarHammer_RedSteel_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.steelIngot2x), null, PlanNameWarHammer, AnvilReq.STEEL, new ItemStack(TFCMItems.itemWarHammer_Steel_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronIngot2x), null, PlanNameWarHammer, AnvilReq.WROUGHTIRON, new ItemStack(TFCMItems.itemWarHammer_WroughtIron_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			//Poniards
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bismuthBronzeIngot), null, PlanNamePoniard, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemPoniard_BismuthBronze_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blackBronzeIngot), null, PlanNamePoniard, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemPoniard_BlackBronze_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blackSteelIngot), null, PlanNamePoniard, AnvilReq.BLACKSTEEL, new ItemStack(TFCMItems.itemPoniard_BlackSteel_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blueSteelIngot), null, PlanNamePoniard, AnvilReq.BLUESTEEL, new ItemStack(TFCMItems.itemPoniard_BlueSteel_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bronzeIngot), null, PlanNamePoniard, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemPoniard_Bronze_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.copperIngot), null, PlanNamePoniard, AnvilReq.COPPER, new ItemStack(TFCMItems.itemPoniard_Copper_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.redSteelIngot), null, PlanNamePoniard, AnvilReq.BLUESTEEL, new ItemStack(TFCMItems.itemPoniard_RedSteel_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.steelIngot), null, PlanNamePoniard, AnvilReq.STEEL, new ItemStack(TFCMItems.itemPoniard_Steel_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronIngot), null, PlanNamePoniard, AnvilReq.WROUGHTIRON, new ItemStack(TFCMItems.itemPoniard_WroughtIron_Head, 1)).addRecipeSkill(Global.SKILL_WEAPONSMITH));
			//Coils
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bismuthBronzeIngot), null, PlanNameCoil, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemCoil_BismuthBronze, 1)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blackBronzeIngot), null, PlanNameCoil, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemCoil_BlackBronze, 1)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blackSteelIngot), null, PlanNameCoil, AnvilReq.BLACKSTEEL, new ItemStack(TFCMItems.itemCoil_BlackSteel, 1)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.blueSteelIngot), null, PlanNameCoil, AnvilReq.BLUESTEEL, new ItemStack(TFCMItems.itemCoil_BlueSteel, 1)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.bronzeIngot), null, PlanNameCoil, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemCoil_Bronze, 1)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.copperIngot), null, PlanNameCoil, AnvilReq.COPPER, new ItemStack(TFCMItems.itemCoil_Copper, 1)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronIngot), null, PlanNameCoil, AnvilReq.WROUGHTIRON, new ItemStack(TFCMItems.itemCoil_WroughtIron, 1)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.redSteelIngot), null, PlanNameCoil, AnvilReq.BLUESTEEL, new ItemStack(TFCMItems.itemCoil_RedSteel, 1)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.steelIngot), null, PlanNameCoil, AnvilReq.STEEL, new ItemStack(TFCMItems.itemCoil_Steel, 1)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
			//Links
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCMItems.itemCoil_BismuthBronze), null, PlanNameLink, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemLink_BismuthBronze, 16)).addRecipeSkill(Global.SKILL_ARMORSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCMItems.itemCoil_BlackBronze), null, PlanNameLink, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemLink_BlackBronze, 16)).addRecipeSkill(Global.SKILL_ARMORSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCMItems.itemCoil_BlackSteel), null, PlanNameLink, AnvilReq.BLACKSTEEL, new ItemStack(TFCMItems.itemLink_BlackSteel, 16)).addRecipeSkill(Global.SKILL_ARMORSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCMItems.itemCoil_BlueSteel), null, PlanNameLink, AnvilReq.BLUESTEEL, new ItemStack(TFCMItems.itemLink_BlueSteel, 16)).addRecipeSkill(Global.SKILL_ARMORSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCMItems.itemCoil_Bronze), null, PlanNameLink, AnvilReq.BRONZE, new ItemStack(TFCMItems.itemLink_Bronze, 16)).addRecipeSkill(Global.SKILL_ARMORSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCMItems.itemCoil_Copper), null, PlanNameLink, AnvilReq.COPPER, new ItemStack(TFCMItems.itemLink_Copper, 16)).addRecipeSkill(Global.SKILL_ARMORSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCMItems.itemCoil_WroughtIron), null, PlanNameLink, AnvilReq.WROUGHTIRON, new ItemStack(TFCMItems.itemLink_WroughtIron, 16)).addRecipeSkill(Global.SKILL_ARMORSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCMItems.itemCoil_RedSteel), null, PlanNameLink, AnvilReq.BLUESTEEL, new ItemStack(TFCMItems.itemLink_RedSteel, 16)).addRecipeSkill(Global.SKILL_ARMORSMITH));
		anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCMItems.itemCoil_Steel), null, PlanNameLink, AnvilReq.STEEL, new ItemStack(TFCMItems.itemLink_Steel, 16)).addRecipeSkill(Global.SKILL_ARMORSMITH));
	}
	 
		
	private static void registerRecipes()
	{
		//Item Stacks
		ItemStack smallMagnetite = new ItemStack(TFCItems.smallOreChunk, 1, 10);   // 10 unit ore
		ItemStack Gravel1 = new ItemStack(TFCBlocks.gravel, 1, WILDCARD_VALUE); //Used Since Gravel Ore Dictionary Support Doesn't Exist.
		ItemStack Gravel2 = new ItemStack(TFCBlocks.gravel2, 1, WILDCARD_VALUE);
		ItemStack Spring = new ItemStack(TFCMItems.itemCoil_WroughtIron, 1);
		ItemStack CeramicBowl = new ItemStack(TFCItems.potteryBowl, 1, 1);
		ItemStack TallowBowl = new ItemStack(TFCMItems.itemBowlTallow, 1, WILDCARD_VALUE);
		
		if(TFCMOptions.enableCraftingPiston = true)
		{
			removeRecipe(new ItemStack(Blocks.piston));
			
			GameRegistry.addRecipe(new ItemStack(Blocks.piston), new Object[]{"H","S","B", 'H',TFCItems.wroughtIronSheet, 'S', TFCItems.tuyereWroughtIron, 'B', TFCMItems.itemPistonBase});
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.sticky_piston), new ItemStack(Blocks.piston), new ItemStack(TFCMItems.itemCaseinGlue));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCMItems.itemPistonBase), new Object[]{"PRP","PPP", Character.valueOf('P'), "woodLumber", Character.valueOf('R'), Items.redstone}));
		}
		
		if(TFCMOptions.enableCraftingLogicTiles = true)
		{
			removeRecipe(new ItemStack(Items.repeater));
			removeRecipe(new ItemStack(Items.comparator));
			
			GameRegistry.addRecipe(new ItemStack(Items.repeater), new Object[]{"TCT", 'C', TFCMItems.itemCircuit, 'T', Blocks.redstone_torch});
			GameRegistry.addRecipe(new ItemStack(Items.comparator), new Object[]{" T ","TCT", 'C', TFCMItems.itemCircuit, 'T', Blocks.redstone_torch});
		}
		
		if(TFCMOptions.enableCraftingLongbow = true)
		{
			//Longbow
			GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemLongBow), new ItemStack(TFCMItems.itemBowLimb), new ItemStack(TFCMItems.itemSinewBowString));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCMItems.itemLongBow), new ItemStack(TFCMItems.itemCaseinGlue), new ItemStack(TFCMItems.itemLongBow, 1, WILDCARD_VALUE),"woodLumber"));
			//Arrows
			GameRegistry.addRecipe(new ItemStack(TFCMItems.itemArrow_BismuthBronze), new Object[]{"H","S","F",'H', TFCMItems.itemArrow_BismuthBronze_Head, 'S', TFCItems.stick, 'F', Items.feather});
			GameRegistry.addRecipe(new ItemStack(TFCMItems.itemArrow_BlackBronze), new Object[]{"H","S","F", 'H', TFCMItems.itemArrow_BlackBronze_Head, 'S', TFCItems.stick, 'F', Items.feather});
			GameRegistry.addRecipe(new ItemStack(TFCMItems.itemArrow_BlackSteel), new Object[]{"H","S","F", 'H', TFCMItems.itemArrow_BlackSteel_Head, 'S', TFCItems.stick, 'F', Items.feather});
			GameRegistry.addRecipe(new ItemStack(TFCMItems.itemArrow_BlueSteel), new Object[]{"H","S","F", 'H', TFCMItems.itemArrow_BlueSteel_Head, 'S', TFCItems.stick, 'F', Items.feather});
			GameRegistry.addRecipe(new ItemStack(TFCMItems.itemArrow_Bronze), new Object[]{"H","S","F", 'H', TFCMItems.itemArrow_Bronze_Head, 'S', TFCItems.stick, 'F', Items.feather});
			GameRegistry.addRecipe(new ItemStack(TFCMItems.itemArrow_Copper), new Object[]{"H","S","F", 'H', TFCMItems.itemArrow_Copper_Head, 'S', TFCItems.stick, 'F', Items.feather});
			GameRegistry.addRecipe(new ItemStack(TFCMItems.itemArrow_RedSteel), new Object[]{"H","S","F", 'H', TFCMItems.itemArrow_RedSteel_Head, 'S', TFCItems.stick, 'F', Items.feather});
			GameRegistry.addRecipe(new ItemStack(TFCMItems.itemArrow_Steel), new Object[]{"H","S","F", 'H', TFCMItems.itemArrow_Steel_Head, 'S', TFCItems.stick, 'F', Items.feather});
			GameRegistry.addRecipe(new ItemStack(TFCMItems.itemArrow_WroughtIron), new Object[]{"H","S","F", 'H', TFCMItems.itemArrow_WroughtIron_Head, 'S', TFCItems.stick, 'F', Items.feather});
			//Molds
			GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemArrow_Copper_Head, 4), new ItemStack(TFCMItems.itemArrow_Mold, 1, 2));
			GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemArrow_Bronze_Head, 4), new ItemStack(TFCMItems.itemArrow_Mold, 1, 3));
			GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemArrow_BismuthBronze_Head, 4), new ItemStack(TFCMItems.itemArrow_Mold, 1, 4));
			GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemArrow_BlackBronze_Head, 4), new ItemStack(TFCMItems.itemArrow_Mold, 1, 5));
		}
		if(TFCMOptions.enableCraftingCrossbow = true)
		{
			//Crossbow
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCMItems.itemCrossBow), new Object[]{"LBL","SGS","TLT", 'L', "woodLumber", 'B', TFCMItems.itemBowLimb, 'T', TFCItems.leather, 'S', TFCMItems.itemSinewString, 'G', TFCMItems.itemGear}));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCMItems.itemCrossBow), new ItemStack(TFCMItems.itemCaseinGlue), new ItemStack(TFCMItems.itemCrossBow, 1, WILDCARD_VALUE),"woodLumber"));
			//Bolts
			GameRegistry.addRecipe(new ItemStack(TFCMItems.itemBolt_BismuthBronze), new Object[]{"H","S", 'H', TFCMItems.itemBolt_BismuthBronze_Head, 'S', TFCItems.stick});
			GameRegistry.addRecipe(new ItemStack(TFCMItems.itemBolt_BlackBronze), new Object[]{"H","S", 'H', TFCMItems.itemBolt_BlackBronze_Head, 'S', TFCItems.stick});
			GameRegistry.addRecipe(new ItemStack(TFCMItems.itemBolt_BlackSteel), new Object[]{"H","S", 'H', TFCMItems.itemBolt_BlackSteel_Head, 'S', TFCItems.stick});
			GameRegistry.addRecipe(new ItemStack(TFCMItems.itemBolt_BlueSteel), new Object[]{"H","S", 'H', TFCMItems.itemBolt_BlueSteel_Head, 'S', TFCItems.stick});
			GameRegistry.addRecipe(new ItemStack(TFCMItems.itemBolt_Bronze), new Object[]{"H","S", 'H', TFCMItems.itemBolt_Bronze_Head, 'S', TFCItems.stick});
			GameRegistry.addRecipe(new ItemStack(TFCMItems.itemBolt_Copper), new Object[]{"H","S", 'H', TFCMItems.itemBolt_Copper_Head, 'S', TFCItems.stick});
			GameRegistry.addRecipe(new ItemStack(TFCMItems.itemBolt_RedSteel), new Object[]{"H","S", 'H', TFCMItems.itemBolt_RedSteel_Head, 'S', TFCItems.stick});
			GameRegistry.addRecipe(new ItemStack(TFCMItems.itemBolt_Steel), new Object[]{"H","S", 'H', TFCMItems.itemBolt_Steel_Head, 'S', TFCItems.stick});
			GameRegistry.addRecipe(new ItemStack(TFCMItems.itemBolt_WroughtIron), new Object[]{"H","S", 'H', TFCMItems.itemBolt_WroughtIron_Head, 'S', TFCItems.stick});
			//Molds
			GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemBolt_Copper_Head, 4), new ItemStack(TFCMItems.itemBolt_Mold, 1, 2));
			GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemBolt_Bronze_Head, 4), new ItemStack(TFCMItems.itemBolt_Mold, 1, 3));
			GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemBolt_BismuthBronze_Head, 4), new ItemStack(TFCMItems.itemBolt_Mold, 1, 4));
			GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemBolt_BlackBronze_Head, 4), new ItemStack(TFCMItems.itemBolt_Mold, 1, 5));
		}
		if(TFCMOptions.enableCraftingLongbow || TFCMOptions.enableCraftingCrossbow)
		{
			for(int j = 0; j < Recipes.knives.length; j++)
			{
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCMItems.itemBowLimb), new ItemStack(Recipes.knives[j], 1, 32767), "logWood"));
			}
		}
		
		if(TFCMOptions.enableCrucibleEmptying = true)
		{
			GameRegistry.addShapelessRecipe(new ItemStack(TFCBlocks.crucible), new ItemStack(TFCBlocks.crucible));
		}
		
		if(TFCMOptions.enableCraftingCompassClock = true)
		{
			removeRecipe(new ItemStack(Items.clock));
			removeRecipe(new ItemStack(Items.compass));
			
			GameRegistry.addRecipe(new ItemStack(Items.clock), new Object[]{"GPG","HCH","GSG", 'G', TFCMItems.itemGear, 'P', Blocks.glass_pane, 'H', TFCItems.stick, 'C', TFCMItems.itemCasingBrass, 'S', Spring});
			GameRegistry.addRecipe(new ItemStack(Items.clock), new Object[]{"GPG","HCH","GSG", 'G', TFCMItems.itemGear, 'P', Blocks.glass_pane, 'H', TFCItems.stick, 'C', TFCMItems.itemCasingRoseGold, 'S', Spring});
			GameRegistry.addRecipe(new ItemStack(Items.compass), new Object[]{"P","O","C", 'P', Blocks.glass_pane, 'O', smallMagnetite, 'C', TFCMItems.itemCasingIron});	
		}
		
		//Shaped
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemSinewString), new Object[]{"F","F","F", 'F', TFCMItems.itemSinewFiber});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemSinewBowString), new Object[]{"SSS", 'S', TFCMItems.itemSinewString});
		GameRegistry.addRecipe(new ItemStack(TFCMBlocks.blockTallowCandle), new Object[]{"W","T", 'W', TFCItems.woolYarn, 'T', TallowBowl});
			//Clay
		GameRegistry.addRecipe(new ItemStack(TFCMBlocks.blockClay), new Object[]{"CC", "CC", 'C', TFCItems.clayBall});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCMBlocks.blockStainedClay, 8, 1), new Object[]{"CCC", "CDC", "CCC", 'C', TFCMBlocks.blockStainedClay, 'D', "dyeBlack"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCMBlocks.blockStainedClay, 8, 2), new Object[]{"CCC", "CDC", "CCC", 'C', TFCMBlocks.blockStainedClay, 'D', "dyeRed"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCMBlocks.blockStainedClay, 8, 3), new Object[]{"CCC", "CDC", "CCC", 'C', TFCMBlocks.blockStainedClay, 'D', "dyeGreen"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCMBlocks.blockStainedClay, 8, 4), new Object[]{"CCC", "CDC", "CCC", 'C', TFCMBlocks.blockStainedClay, 'D', "dyeBrown"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCMBlocks.blockStainedClay, 8, 5), new Object[]{"CCC", "CDC", "CCC", 'C', TFCMBlocks.blockStainedClay, 'D', "dyeBlue"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCMBlocks.blockStainedClay, 8, 6), new Object[]{"CCC", "CDC", "CCC", 'C', TFCMBlocks.blockStainedClay, 'D', "dyePurple"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCMBlocks.blockStainedClay, 8, 7), new Object[]{"CCC", "CDC", "CCC", 'C', TFCMBlocks.blockStainedClay, 'D', "dyeCyan"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCMBlocks.blockStainedClay, 8, 8), new Object[]{"CCC", "CDC", "CCC", 'C', TFCMBlocks.blockStainedClay, 'D', "dyeSilver"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCMBlocks.blockStainedClay, 8, 9), new Object[]{"CCC", "CDC", "CCC", 'C', TFCMBlocks.blockStainedClay, 'D', "dyeGray"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCMBlocks.blockStainedClay, 8, 10), new Object[]{"CCC", "CDC", "CCC", 'C', TFCMBlocks.blockStainedClay, 'D', "dyePink"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCMBlocks.blockStainedClay, 8, 11), new Object[]{"CCC", "CDC", "CCC", 'C', TFCMBlocks.blockStainedClay, 'D', "dyeLime"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCMBlocks.blockStainedClay, 8, 12), new Object[]{"CCC", "CDC", "CCC", 'C', TFCMBlocks.blockStainedClay, 'D', "dyeYellow"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCMBlocks.blockStainedClay, 8, 13), new Object[]{"CCC", "CDC", "CCC", 'C', TFCMBlocks.blockStainedClay, 'D', "dyeLightBlue"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCMBlocks.blockStainedClay, 8, 14), new Object[]{"CCC", "CDC", "CCC", 'C', TFCMBlocks.blockStainedClay, 'D', "dyeMagenta"}));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TFCMBlocks.blockStainedClay, 8, 15), new Object[]{"CCC", "CDC", "CCC", 'C', TFCMBlocks.blockStainedClay, 'D', "dyeOrange"}));
		
			//Shapeless candle dyes
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCMItems.itemTallowDye, 1, 0), "dyeBlack", TallowBowl));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCMItems.itemTallowDye, 1, 1), "dyeRed", TallowBowl));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCMItems.itemTallowDye, 1, 2), "dyeGreen", TallowBowl));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCMItems.itemTallowDye, 1, 3), "dyeBrown", TallowBowl));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCMItems.itemTallowDye, 1, 4), "dyeBlue", TallowBowl));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCMItems.itemTallowDye, 1, 5), "dyePurple", TallowBowl));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCMItems.itemTallowDye, 1, 6), "dyeCyan", TallowBowl));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCMItems.itemTallowDye, 1, 7), "dyeSilver", TallowBowl));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCMItems.itemTallowDye, 1, 8), "dyeGray", TallowBowl));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCMItems.itemTallowDye, 1, 9), "dyePink", TallowBowl));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCMItems.itemTallowDye, 1, 10), "dyeLime", TallowBowl));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCMItems.itemTallowDye, 1, 11), "dyeYellow", TallowBowl));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCMItems.itemTallowDye, 1, 12), "dyeLightBlue", TallowBowl));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCMItems.itemTallowDye, 1, 13), "dyeMagenta", TallowBowl));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCMItems.itemTallowDye, 1, 14), "dyeOrange", TallowBowl));
			//Halberds
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemHalberd_BismuthBronze), new Object[]{"H  "," S ","  S", 'H', TFCMItems.itemHalberd_BismuthBronze_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemHalberd_BismuthBronze), new Object[]{"  H"," S ","S  ", 'H', TFCMItems.itemHalberd_BismuthBronze_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemHalberd_BlackBronze), new Object[]{"H  "," S ","  S", 'H', TFCMItems.itemHalberd_BlackBronze_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemHalberd_BlackBronze), new Object[]{"  H"," S ","S  ", 'H', TFCMItems.itemHalberd_BlackBronze_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemHalberd_BlackSteel), new Object[]{"H  "," S ","  S", 'H', TFCMItems.itemHalberd_BlackSteel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemHalberd_BlackSteel), new Object[]{"  H"," S ","S  ", 'H', TFCMItems.itemHalberd_BlackSteel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemHalberd_BlueSteel), new Object[]{"H  "," S ","  S", 'H', TFCMItems.itemHalberd_BlueSteel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemHalberd_BlueSteel), new Object[]{"  H"," S ","S  ", 'H', TFCMItems.itemHalberd_BlueSteel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemHalberd_Bronze), new Object[]{"H  "," S ","  S", 'H', TFCMItems.itemHalberd_Bronze_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemHalberd_Bronze), new Object[]{"  H"," S ","S  ", 'H', TFCMItems.itemHalberd_Bronze_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemHalberd_Copper), new Object[]{"H  "," S ","  S", 'H', TFCMItems.itemHalberd_Copper_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemHalberd_Copper), new Object[]{"  H"," S ","S  ", 'H', TFCMItems.itemHalberd_Copper_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemHalberd_RedSteel), new Object[]{"H  "," S ","  S", 'H', TFCMItems.itemHalberd_RedSteel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemHalberd_RedSteel), new Object[]{"  H"," S ","S  ", 'H', TFCMItems.itemHalberd_RedSteel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemHalberd_Steel), new Object[]{"H  "," S ","  S", 'H', TFCMItems.itemHalberd_Steel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemHalberd_Steel), new Object[]{"  H"," S ","S  ", 'H', TFCMItems.itemHalberd_Steel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemHalberd_WroughtIron), new Object[]{"H  "," S ","  S", 'H', TFCMItems.itemHalberd_WroughtIron_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemHalberd_WroughtIron), new Object[]{"  H"," S ","S  ", 'H', TFCMItems.itemHalberd_WroughtIron_Head, 'S', TFCItems.stick});
			//War Hammers
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemWarHammer_BismuthBronze), new Object[]{"H  "," S ","  S", 'H', TFCMItems.itemWarHammer_BismuthBronze_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemWarHammer_BismuthBronze), new Object[]{"  H"," S ","S  ", 'H', TFCMItems.itemWarHammer_BismuthBronze_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemWarHammer_BlackBronze), new Object[]{"H  "," S ","  S", 'H', TFCMItems.itemWarHammer_BlackBronze_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemWarHammer_BlackBronze), new Object[]{"  H"," S ","S  ", 'H', TFCMItems.itemWarHammer_BlackBronze_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemWarHammer_BlackSteel), new Object[]{"H  "," S ","  S", 'H', TFCMItems.itemWarHammer_BlackSteel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemWarHammer_BlackSteel), new Object[]{"  H"," S ","S  ", 'H', TFCMItems.itemWarHammer_BlackSteel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemWarHammer_BlueSteel), new Object[]{"H  "," S ","  S", 'H', TFCMItems.itemWarHammer_BlueSteel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemWarHammer_BlueSteel), new Object[]{"  H"," S ","S  ", 'H', TFCMItems.itemWarHammer_BlueSteel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemWarHammer_Bronze), new Object[]{"H  "," S ","  S", 'H', TFCMItems.itemWarHammer_Bronze_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemWarHammer_Bronze), new Object[]{"  H"," S ","S  ", 'H', TFCMItems.itemWarHammer_Bronze_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemWarHammer_Copper), new Object[]{"H  "," S ","  S", 'H', TFCMItems.itemWarHammer_Copper_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemWarHammer_Copper), new Object[]{"  H"," S ","S  ", 'H', TFCMItems.itemWarHammer_Copper_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemWarHammer_RedSteel), new Object[]{"H  "," S ","  S", 'H', TFCMItems.itemWarHammer_RedSteel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemWarHammer_RedSteel), new Object[]{"  H"," S ","S  ", 'H', TFCMItems.itemWarHammer_RedSteel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemWarHammer_Steel), new Object[]{"H  "," S ","  S", 'H', TFCMItems.itemWarHammer_Steel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemWarHammer_Steel), new Object[]{"  H"," S ","S  ", 'H', TFCMItems.itemWarHammer_Steel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemWarHammer_WroughtIron), new Object[]{"H  "," S ","  S", 'H', TFCMItems.itemWarHammer_WroughtIron_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemWarHammer_WroughtIron), new Object[]{"  H"," S ","S  ", 'H', TFCMItems.itemWarHammer_WroughtIron_Head, 'S', TFCItems.stick});
			//Poniards
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemPoniard_BismuthBronze), new Object[]{"H","S", 'H', TFCMItems.itemPoniard_BismuthBronze_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemPoniard_BlackBronze), new Object[]{"H","S", 'H', TFCMItems.itemPoniard_BlackBronze_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemPoniard_BlackSteel), new Object[]{"H","S", 'H', TFCMItems.itemPoniard_BlackSteel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemPoniard_BlueSteel), new Object[]{"H","S", 'H', TFCMItems.itemPoniard_BlueSteel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemPoniard_Bronze), new Object[]{"H","S", 'H', TFCMItems.itemPoniard_Bronze_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemPoniard_Copper), new Object[]{"H","S", 'H', TFCMItems.itemPoniard_Copper_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemPoniard_RedSteel), new Object[]{"H","S", 'H', TFCMItems.itemPoniard_RedSteel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemPoniard_Steel), new Object[]{"H","S", 'H', TFCMItems.itemPoniard_Steel_Head, 'S', TFCItems.stick});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemPoniard_WroughtIron), new Object[]{"H","S", 'H', TFCMItems.itemPoniard_WroughtIron_Head, 'S', TFCItems.stick});
		
		//Armor
			//Chain Helmets
		GameRegistry.addRecipe(new ItemStack(TFCMItems.BismuthBronzeChainHelmet), new Object[]{"XOX","X X","   ", 'X', new ItemStack(TFCMItems.itemChain_Sheet_BismuthBronze), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.BlackBronzeChainHelmet), new Object[]{"XOX","X X","   ", 'X', new ItemStack(TFCMItems.itemChain_Sheet_BlackBronze), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.BlackSteelChainHelmet), new Object[]{"XOX","X X","   ", 'X', new ItemStack(TFCMItems.itemChain_Sheet_BlackSteel), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.BlueSteelChainHelmet), new Object[]{"XOX","X X","   ", 'X', new ItemStack(TFCMItems.itemChain_Sheet_BlueSteel), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.BronzeChainHelmet), new Object[]{"XOX","X X","   ", 'X', new ItemStack(TFCMItems.itemChain_Sheet_Bronze), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.CopperChainHelmet), new Object[]{"XOX","X X","   ", 'X', new ItemStack(TFCMItems.itemChain_Sheet_Copper), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.WroughtIronChainHelmet), new Object[]{"XOX","X X","   ", 'X', new ItemStack(TFCMItems.itemChain_Sheet_WroughtIron), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.RedSteelChainHelmet), new Object[]{"XOX","X X","   ", 'X', new ItemStack(TFCMItems.itemChain_Sheet_RedSteel), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.SteelChainHelmet), new Object[]{"XOX","X X","   ", 'X', new ItemStack(TFCMItems.itemChain_Sheet_Steel), 'O', TFCItems.leather});
			//Chain Chestplate
		GameRegistry.addRecipe(new ItemStack(TFCMItems.BismuthBronzeChainChestplate), new Object[]{"X X","XOX","XXX", 'X', new ItemStack(TFCMItems.itemChain_Sheet_BismuthBronze), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.BlackBronzeChainChestplate), new Object[]{"X X","XOX","XXX", 'X', new ItemStack(TFCMItems.itemChain_Sheet_BlackBronze), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.BlackSteelChainChestplate), new Object[]{"X X","XOX","XXX", 'X', new ItemStack(TFCMItems.itemChain_Sheet_BlackSteel), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.BlueSteelChainChestplate), new Object[]{"X X","XOX","XXX", 'X', new ItemStack(TFCMItems.itemChain_Sheet_BlueSteel), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.BronzeChainChestplate), new Object[]{"X X","XOX","XXX", 'X', new ItemStack(TFCMItems.itemChain_Sheet_Bronze), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.CopperChainChestplate), new Object[]{"X X","XOX","XXX", 'X', new ItemStack(TFCMItems.itemChain_Sheet_Copper), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.WroughtIronChainChestplate), new Object[]{"X X","XOX","XXX", 'X', new ItemStack(TFCMItems.itemChain_Sheet_WroughtIron), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.RedSteelChainChestplate), new Object[]{"X X","XOX","XXX", 'X', new ItemStack(TFCMItems.itemChain_Sheet_RedSteel), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.SteelChainChestplate), new Object[]{"X X","XOX","XXX", 'X', new ItemStack(TFCMItems.itemChain_Sheet_Steel), 'O', TFCItems.leather});
			//Chain Leggings
		GameRegistry.addRecipe(new ItemStack(TFCMItems.BismuthBronzeChainGreaves), new Object[]{"XXX","XOX","X X", 'X', new ItemStack(TFCMItems.itemChain_Sheet_BismuthBronze), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.BlackBronzeChainGreaves), new Object[]{"XXX","XOX","X X", 'X', new ItemStack(TFCMItems.itemChain_Sheet_BlackBronze), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.BlackSteelChainGreaves), new Object[]{"XXX","XOX","X X", 'X', new ItemStack(TFCMItems.itemChain_Sheet_BlackSteel), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.BlueSteelChainGreaves), new Object[]{"XXX","XOX","X X", 'X', new ItemStack(TFCMItems.itemChain_Sheet_BlueSteel), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.BronzeChainGreaves), new Object[]{"XXX","XOX","X X", 'X', new ItemStack(TFCMItems.itemChain_Sheet_Bronze), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.CopperChainGreaves), new Object[]{"XXX","XOX","X X", 'X', new ItemStack(TFCMItems.itemChain_Sheet_Copper), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.WroughtIronChainGreaves), new Object[]{"XXX","XOX","X X", 'X', new ItemStack(TFCMItems.itemChain_Sheet_WroughtIron), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.RedSteelChainGreaves), new Object[]{"XXX","XOX","X X", 'X', new ItemStack(TFCMItems.itemChain_Sheet_RedSteel), 'O', TFCItems.leather});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.SteelChainGreaves), new Object[]{"XXX","XOX","X X", 'X', new ItemStack(TFCMItems.itemChain_Sheet_Steel), 'O', TFCItems.leather});
			//Chain Item Crafting
				//Squares
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemChain_Square_BismuthBronze), new Object[]{"XX","XX", 'X', new ItemStack(TFCMItems.itemLink_BismuthBronze)});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemChain_Square_BlackBronze), new Object[]{"XX","XX", 'X', new ItemStack(TFCMItems.itemLink_BlackBronze)});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemChain_Square_BlackSteel), new Object[]{"XX","XX", 'X', new ItemStack(TFCMItems.itemLink_BlackSteel)});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemChain_Square_BlueSteel), new Object[]{"XX","XX", 'X', new ItemStack(TFCMItems.itemLink_BlueSteel)});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemChain_Square_Bronze), new Object[]{"XX","XX", 'X', new ItemStack(TFCMItems.itemLink_Bronze)});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemChain_Square_Copper), new Object[]{"XX","XX", 'X', new ItemStack(TFCMItems.itemLink_Copper)});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemChain_Square_WroughtIron), new Object[]{"XX","XX", 'X', new ItemStack(TFCMItems.itemLink_WroughtIron)});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemChain_Square_RedSteel), new Object[]{"XX","XX", 'X', new ItemStack(TFCMItems.itemLink_RedSteel)});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemChain_Square_Steel), new Object[]{"XX","XX", 'X', new ItemStack(TFCMItems.itemLink_Steel)});
				//Sheets
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemChain_Sheet_BismuthBronze), new Object[]{"XX","XX", 'X', new ItemStack(TFCMItems.itemChain_Square_BismuthBronze)});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemChain_Sheet_BlackBronze), new Object[]{"XX","XX", 'X', new ItemStack(TFCMItems.itemChain_Square_BlackBronze)});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemChain_Sheet_BlackSteel), new Object[]{"XX","XX", 'X', new ItemStack(TFCMItems.itemChain_Square_BlackSteel)});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemChain_Sheet_BlueSteel), new Object[]{"XX","XX", 'X', new ItemStack(TFCMItems.itemChain_Square_BlueSteel)});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemChain_Sheet_Bronze), new Object[]{"XX","XX", 'X', new ItemStack(TFCMItems.itemChain_Square_Bronze)});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemChain_Sheet_Copper), new Object[]{"XX","XX", 'X', new ItemStack(TFCMItems.itemChain_Square_Copper)});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemChain_Sheet_WroughtIron), new Object[]{"XX","XX", 'X', new ItemStack(TFCMItems.itemChain_Square_WroughtIron)});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemChain_Sheet_RedSteel), new Object[]{"XX","XX", 'X', new ItemStack(TFCMItems.itemChain_Square_RedSteel)});
		GameRegistry.addRecipe(new ItemStack(TFCMItems.itemChain_Sheet_Steel), new Object[]{"XX","XX", 'X', new ItemStack(TFCMItems.itemChain_Square_Steel)});
		
		//Ore Dictionary
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.ladder, 16), new Object[]{"P P","PSP","P P", Character.valueOf('P'), "woodLumber", Character.valueOf('R'), TFCItems.stick}));
		
		//Shapeless
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemBowlSuet), CeramicBowl, new ItemStack(TFCMItems.itemSuet));
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMBlocks.blockFruitPress), new ItemStack(TFCBlocks.hopper));
		GameRegistry.addShapelessRecipe(new ItemStack(TFCBlocks.hopper), new ItemStack(TFCMBlocks.blockFruitPress));
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemQuiver), new ItemStack(TFCItems.quiver));
		//Dyes
		GameRegistry.addShapelessRecipe(new ItemStack(TFCItems.dye, 1, 6), new ItemStack(TFCItems.dye, 1, 2), new ItemStack(TFCItems.powder, 1, 6)); //Cyan
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.dye, 1, 7), new ItemStack(TFCItems.dye, 1, 8), new ItemStack(TFCItems.powder, 1, 0), ("blockSand"))); //Light Gray
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.dye, 1, 8), new ItemStack(TFCItems.dye, 1, 0), new ItemStack(TFCItems.powder, 1, 0), ("blockSand"))); //Gray
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.dye, 1, 9), new ItemStack(TFCItems.dye, 1, 1), new ItemStack(TFCItems.powder, 1, 0), ("blockSand"))); //Pink
		GameRegistry.addShapelessRecipe(new ItemStack(TFCItems.dye, 1, 10), new ItemStack(TFCItems.dye, 1, 2), new ItemStack(TFCItems.dye, 1, 11)); //Lime
		GameRegistry.addShapelessRecipe(new ItemStack(TFCItems.dye, 1, 13), new ItemStack(TFCItems.dye, 1, 5), new ItemStack(TFCItems.dye, 1, 9)); //Magenta
		GameRegistry.addShapelessRecipe(new ItemStack(TFCItems.dye, 1, 14), new ItemStack(TFCItems.dye, 1, 1), new ItemStack(TFCItems.dye, 1, 11)); //Orange
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemBrownDye), new ItemStack(TFCMItems.itemIronDust), new ItemStack(TFCItems.coal, 1, WILDCARD_VALUE)); //Brown
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemBrownDye), new ItemStack(TFCItems.powder, 1, 5), new ItemStack(TFCItems.coal, 1, WILDCARD_VALUE)); //Brown
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemBrownDye), new ItemStack(TFCItems.powder, 1, 7), new ItemStack(TFCItems.coal, 1, WILDCARD_VALUE)); //Brown
		GameRegistry.addShapelessRecipe(new ItemStack(TFCItems.dye, 1, 0), new ItemStack(TFCMItems.itemBrownDye), new ItemStack(TFCItems.coal, 1, WILDCARD_VALUE)); //Black
		
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemHalberd_Copper_Head), new ItemStack(TFCMItems.itemHalberd_Mold, 1, 2));
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemHalberd_Bronze_Head), new ItemStack(TFCMItems.itemHalberd_Mold, 1, 3));
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemHalberd_BismuthBronze_Head), new ItemStack(TFCMItems.itemHalberd_Mold, 1, 4));
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemHalberd_BlackBronze_Head), new ItemStack(TFCMItems.itemHalberd_Mold, 1, 5));
		
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemWarHammer_Copper_Head), new ItemStack(TFCMItems.itemWarHammer_Mold, 1, 2));
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemWarHammer_Bronze_Head), new ItemStack(TFCMItems.itemWarHammer_Mold, 1, 3));
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemWarHammer_BismuthBronze_Head), new ItemStack(TFCMItems.itemWarHammer_Mold, 1, 4));
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemWarHammer_BlackBronze_Head), new ItemStack(TFCMItems.itemWarHammer_Mold, 1, 5));
		
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemPoniard_Copper_Head), new ItemStack(TFCMItems.itemPoniard_Mold, 1, 2));
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemPoniard_Bronze_Head), new ItemStack(TFCMItems.itemPoniard_Mold, 1, 3));
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemPoniard_BismuthBronze_Head), new ItemStack(TFCMItems.itemPoniard_Mold, 1, 4));
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemPoniard_BlackBronze_Head), new ItemStack(TFCMItems.itemPoniard_Mold, 1, 5));
		
		//Food
		GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemBottleLemonade, 4), new ItemStack(TFCItems.woodenBucketWater), new ItemStack(TFCMItems.itemBottleJuiceLemon), new ItemStack(TFCMItems.itemBottleJuiceLemon), new ItemStack(TFCMItems.itemBottleJuiceLemon), new ItemStack(TFCMItems.itemBottleJuiceLemon), (ItemFoodTFC.createTag(new ItemStack(TFCItems.sugar), 4F)));

		//Road Block Crafting
		for (int j = 0; j < Global.STONE_IGEX.length; j++)
		{
			GameRegistry.addRecipe(new ItemStack(TFCMBlocks.blockRoadIgEx, 8, j), new Object[] { "GSG", "SMS", "GSG", Character.valueOf('S'), new ItemStack(TFCItems.stoneBrick, 1, j + Global.STONE_IGEX_START), Character.valueOf('M'), new ItemStack(TFCItems.mortar, 1), Character.valueOf('G'), Gravel1 });
			GameRegistry.addRecipe(new ItemStack(TFCMBlocks.blockRoadIgEx, 8, j), new Object[] { "GSG", "SMS", "GSG", Character.valueOf('S'), new ItemStack(TFCItems.stoneBrick, 1, j + Global.STONE_IGEX_START), Character.valueOf('M'), new ItemStack(TFCItems.mortar, 1), Character.valueOf('G'), Gravel2 });
		}
		
		for (int j = 0; j < Global.STONE_IGIN.length; j++)
		{
			GameRegistry.addRecipe(new ItemStack(TFCMBlocks.blockRoadIgIn, 8, j), new Object[] { "GSG", "SMS", "GSG", Character.valueOf('S'), new ItemStack(TFCItems.stoneBrick, 1, j + Global.STONE_IGIN_START), Character.valueOf('M'), new ItemStack(TFCItems.mortar, 1), Character.valueOf('G'), Gravel1 });
			GameRegistry.addRecipe(new ItemStack(TFCMBlocks.blockRoadIgIn, 8, j), new Object[] { "GSG", "SMS", "GSG", Character.valueOf('S'), new ItemStack(TFCItems.stoneBrick, 1, j + Global.STONE_IGIN_START), Character.valueOf('M'), new ItemStack(TFCItems.mortar, 1), Character.valueOf('G'), Gravel2 });
		}
		
		for (int j = 0; j < Global.STONE_MM.length; j++)
		{
			GameRegistry.addRecipe(new ItemStack(TFCMBlocks.blockRoadMM, 8, j), new Object[] { "GSG", "SMS", "GSG", Character.valueOf('S'), new ItemStack(TFCItems.stoneBrick, 1, j + Global.STONE_MM_START), Character.valueOf('M'), new ItemStack(TFCItems.mortar, 1), Character.valueOf('G'), Gravel1 });
			GameRegistry.addRecipe(new ItemStack(TFCMBlocks.blockRoadMM, 8, j), new Object[] { "GSG", "SMS", "GSG", Character.valueOf('S'), new ItemStack(TFCItems.stoneBrick, 1, j + Global.STONE_MM_START), Character.valueOf('M'), new ItemStack(TFCItems.mortar, 1), Character.valueOf('G'), Gravel2 });
		}
		
		for (int j = 0; j < Global.STONE_SED.length; j++)
		{
			GameRegistry.addRecipe(new ItemStack(TFCMBlocks.blockRoadSed, 8, j), new Object[] { "GSG", "SMS", "GSG", Character.valueOf('S'), new ItemStack(TFCItems.stoneBrick, 1, j + Global.STONE_SED_START), Character.valueOf('M'), new ItemStack(TFCItems.mortar, 1), Character.valueOf('G'), Gravel1 });
			GameRegistry.addRecipe(new ItemStack(TFCMBlocks.blockRoadSed, 8, j), new Object[] { "GSG", "SMS", "GSG", Character.valueOf('S'), new ItemStack(TFCItems.stoneBrick, 1, j + Global.STONE_SED_START), Character.valueOf('M'), new ItemStack(TFCItems.mortar, 1), Character.valueOf('G'), Gravel2 });
		}
		
		//Knife Recipes
		for(int j = 0; j < Recipes.knives.length; j++)
		{
			//moved bow limb recipe to the bow crafting section
		}
		
		//Hammer Recipes
		for(int j = 0; j < Recipes.hammers.length; j++)
		{
			GameRegistry.addShapelessRecipe(new ItemStack(TFCMItems.itemSinewFiber, 3), new ItemStack(Recipes.hammers[j], 1, 32767), new ItemStack(TFCMItems.itemDeerTendon));	
		}
	}
	
	//From TFCraft/Recipes.java
	@SuppressWarnings("unchecked")
	public static void removeRecipe(ItemStack resultItem)
	{
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (int i = 0; i < recipes.size(); i++)
		{
			if (recipes.get(i) != null)
			{
				ItemStack recipeResult = recipes.get(i).getRecipeOutput();
			
				if (ItemStack.areItemStacksEqual(resultItem, recipeResult))
					recipes.remove(i--);
			}
		}
	}
	
	//Barrel
	private static void registerBarrelRecipes()
	{
		BarrelManager.getInstance().addRecipe(new BarrelRecipe(new ItemStack(TFCItems.powder, 1, 0), new FluidStack(TFCFluids.MILKCURDLED, 100), new ItemStack(TFCMItems.itemCaseinGlue, 2), new FluidStack(TFCFluids.MILKCURDLED, 100), 1).setMinTechLevel(0));
		BarrelManager.getInstance().addRecipe(new BarrelFermentationRecipe(new FluidStack(TFCMFluids.FRUITJUICE, 250), new FluidStack(TFCMFluids.FRUITWINE, 250), 72));
		BarrelManager.getInstance().addRecipe(new BarrelVinegarRecipe(new FluidStack(TFCMFluids.FRUITWINE, 1000), (new FluidStack(TFCFluids.VINEGAR, 1000))));
		
		BarrelManager.getInstance().addRecipe(new BarrelAlcoholRecipe(ItemFoodTFC.createTag(new ItemStack(TFCMItems.itemSoyPaste), 160), new FluidStack(TFCMFluids.WATERHOT, 10000), null, new FluidStack(TFCMFluids.SOYMILK, 10000)));	
		BarrelManager.getInstance().addRecipe(new BarrelLiquidToLiquidRecipe(new FluidStack(TFCMFluids.SOYMILK, 9000), new FluidStack(TFCFluids.VINEGAR, 1000), new FluidStack(TFCFluids.MILKVINEGAR, 10000)).setSealedRecipe(false).setMinTechLevel(0).setRemovesLiquid(false));
		
		BarrelManager.getInstance().addRecipe(new BarrelRecipe(new ItemStack(TFCMItems.itemBowlSuet), new FluidStack(TFCMFluids.WATERHOT, 1000), new ItemStack(TFCMItems.itemBowlTallow), new FluidStack(TFCFluids.FRESHWATER, 1000), 2));
	}
	
	//Fruit Press
	private static void registerFruitPressRecipes()
	{
		//Fruit Juice
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.cherry, TFCMFluids.FRUITJUICE, 4));
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.plum, TFCMFluids.FRUITJUICE, 4));
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.wintergreenBerry, TFCMFluids.FRUITJUICE, 8));
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.blueberry, TFCMFluids.FRUITJUICE, 8));
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.raspberry, TFCMFluids.FRUITJUICE, 8));
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.strawberry, TFCMFluids.FRUITJUICE, 8));
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.blackberry, TFCMFluids.FRUITJUICE, 8));
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.bunchberry, TFCMFluids.FRUITJUICE, 8));
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.cranberry, TFCMFluids.FRUITJUICE, 8));
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.snowberry, TFCMFluids.FRUITJUICE, 8));
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.elderberry, TFCMFluids.FRUITJUICE, 8));
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.gooseberry, TFCMFluids.FRUITJUICE, 8));
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.cloudberry, TFCMFluids.FRUITJUICE, 8));
		
		//Apple Juice
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.redApple, TFCMFluids.JUICEAPPLE, 4));
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.greenApple, TFCMFluids.JUICEAPPLE, 4));
		
		//Orange Juice
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.orange, TFCMFluids.JUICEORANGE, 4));
		
		//Lemon Juice
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.lemon, TFCMFluids.JUICELEMON, 4));
		
		//Other
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.olive, TFCFluids.OLIVEOIL, 1));
		FruitPressManager.addRecipe(new FruitPressRecipe(TFCItems.onion, TFCMFluids.JUICEONION, 2));
	}
	
	//Kiln
	private static void registerKilnRecipes()
	{
		KilnCraftingManager.getInstance().addRecipe(
		new KilnRecipe(
		new ItemStack(TFCMItems.itemHalberd_Mold,1,0),
		0, 
		new ItemStack(TFCMItems.itemHalberd_Mold,1,1)));
		
		KilnCraftingManager.getInstance().addRecipe(
		new KilnRecipe(
		new ItemStack(TFCMItems.itemWarHammer_Mold,1,0),
		0, 
		new ItemStack(TFCMItems.itemWarHammer_Mold,1,1)));	
		
		KilnCraftingManager.getInstance().addRecipe(
		new KilnRecipe(
		new ItemStack(TFCMItems.itemPoniard_Mold,1,0),
		0, 
		new ItemStack(TFCMItems.itemPoniard_Mold,1,1)));	
		
		KilnCraftingManager.getInstance().addRecipe(
		new KilnRecipe(
		new ItemStack(TFCMItems.itemArrow_Mold,1,0),
		0, 
		new ItemStack(TFCMItems.itemArrow_Mold,1,1)));	
		
		KilnCraftingManager.getInstance().addRecipe(
		new KilnRecipe(
		new ItemStack(TFCMItems.itemBolt_Mold,1,0),
		0, 
		new ItemStack(TFCMItems.itemBolt_Mold,1,1)));
	}
	
	//Mold Pouring
	public static ItemStack getStackTemp(ItemStack is)
	{
		NBTTagCompound Temp = new NBTTagCompound();
		Temp.setBoolean("temp", true);
		is.setTagCompound(Temp);
		return is;
	}
			
			
	private static void registerToolMolds()
	{
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemHalberd_Mold, 1, 2), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.copperUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemHalberd_Mold, 1, 1)});
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemHalberd_Mold, 1, 3), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.bronzeUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemHalberd_Mold, 1, 1)});
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemHalberd_Mold, 1, 4), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.bismuthBronzeUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemHalberd_Mold, 1, 1)});
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemHalberd_Mold, 1, 5), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.blackBronzeUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemHalberd_Mold, 1, 1)});
		
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemWarHammer_Mold, 1, 2), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.copperUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemWarHammer_Mold, 1, 1)});
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemWarHammer_Mold, 1, 3), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.bronzeUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemWarHammer_Mold, 1, 1)});
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemWarHammer_Mold, 1, 4), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.bismuthBronzeUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemWarHammer_Mold, 1, 1)});
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemWarHammer_Mold, 1, 5), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.blackBronzeUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemWarHammer_Mold, 1, 1)});
		
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemPoniard_Mold, 1, 2), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.copperUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemPoniard_Mold, 1, 1)});
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemPoniard_Mold, 1, 3), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.bronzeUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemPoniard_Mold, 1, 1)});
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemPoniard_Mold, 1, 4), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.bismuthBronzeUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemPoniard_Mold, 1, 1)});
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemPoniard_Mold, 1, 5), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.blackBronzeUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemPoniard_Mold, 1, 1)});
		
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemArrow_Mold, 1, 2), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.copperUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemArrow_Mold, 1, 1)});
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemArrow_Mold, 1, 3), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.bronzeUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemArrow_Mold, 1, 1)});
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemArrow_Mold, 1, 4), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.bismuthBronzeUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemArrow_Mold, 1, 1)});
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemArrow_Mold, 1, 5), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.blackBronzeUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemArrow_Mold, 1, 1)});
		
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemBolt_Mold, 1, 2), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.copperUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemBolt_Mold, 1, 1)});
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemBolt_Mold, 1, 3), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.bronzeUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemBolt_Mold, 1, 1)});
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemBolt_Mold, 1, 4), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.bismuthBronzeUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemBolt_Mold, 1, 1)});
		craftingManager.addRecipe(new ItemStack(TFCMItems.itemBolt_Mold, 1, 5), 
				new Object[] {"12", Character.valueOf('1'), getStackTemp(new ItemStack(TFCItems.blackBronzeUnshaped, 1, 1)), Character.valueOf('2'), new ItemStack(TFCMItems.itemBolt_Mold, 1, 1)});
	}
	
	private static void registerKnappingRecipes()
	{
		CraftingManagerTFC.getInstance().addRecipe(new ItemStack(TFCMItems.itemHalberd_Mold, 1, 0), new Object[] { 
			" ##  ",
			"#### ",
			"#####",
			"#### ",
			" ##  ", Character.valueOf('#'), new ItemStack(TFCItems.flatClay, 1, 1)});
		
		CraftingManagerTFC.getInstance().addRecipe(new ItemStack(TFCMItems.itemWarHammer_Mold, 1, 0), new Object[] { 
			"     ",
			"#####",
			"###  ",
			"  #  ",
			"     ", Character.valueOf('#'), new ItemStack(TFCItems.flatClay, 1, 1)});
		
		CraftingManagerTFC.getInstance().addRecipe(new ItemStack(TFCMItems.itemPoniard_Mold, 1, 0), new Object[] { 
			"     ",
			"  ## ",
			" ### ",
			" ##  ",
			"#    ", Character.valueOf('#'), new ItemStack(TFCItems.flatClay, 1, 1)});
		
		if(TFCMOptions.enableCraftingCrossbow == true)
		{
			CraftingManagerTFC.getInstance().addRecipe(new ItemStack(TFCMItems.itemBolt_Mold, 1, 0), new Object[] { 
				"     ",
				"     ",
				"     ",
				"  #  ",
				"  #  ", Character.valueOf('#'), new ItemStack(TFCItems.flatClay, 1, 1)});
		}
		
		if(TFCMOptions.enableCraftingLongbow == true)
		{
			CraftingManagerTFC.getInstance().addRecipe(new ItemStack(TFCMItems.itemArrow_Mold, 1, 0), new Object[] { 
				"     ",
				" ### ",
				"  ## ",
				"   # ",
				"     ", Character.valueOf('#'), new ItemStack(TFCItems.flatClay, 1, 1)});
		}
	}
	
	private static void registerQuernRecipes()
	{
		QuernManager.getInstance().addRecipe(new QuernRecipe(new ItemStack(TFCItems.wroughtIronIngot), new ItemStack(TFCMItems.itemIronDust, 8)));
		QuernManager.getInstance().addRecipe(new QuernRecipe(new ItemStack(TFCItems.smallOreChunk, 1, 10), new ItemStack(TFCMItems.itemIronDust, 1)));
		QuernManager.getInstance().addRecipe(new QuernRecipe(new ItemStack(TFCItems.oreChunk, 1, 59), new ItemStack(TFCMItems.itemIronDust, 1)));
		QuernManager.getInstance().addRecipe(new QuernRecipe(new ItemStack(TFCItems.oreChunk, 1, 10), new ItemStack(TFCMItems.itemIronDust, 2)));
		QuernManager.getInstance().addRecipe(new QuernRecipe(new ItemStack(TFCItems.oreChunk, 1, 45), new ItemStack(TFCMItems.itemIronDust, 3)));
		QuernManager.getInstance().addRecipe(new QuernRecipe(new ItemStack(TFCItems.soybean, 1), new ItemStack(TFCMItems.itemSoyPaste, 1)));
	}
	
	@SuppressWarnings("unused")
	public static void registerHeatingRecipes()
	{
		HeatRaw bismuthRaw = new HeatRaw(0.14, 270);
		HeatRaw bismuthBronzeRaw = new HeatRaw(0.35, 985);
		HeatRaw blackBronzeRaw = new HeatRaw(0.35, 1070);
		HeatRaw blackSteelRaw = new HeatRaw(0.35, 1485);
		HeatRaw blueSteelRaw = new HeatRaw(0.35, 1540);
		HeatRaw brassRaw = new HeatRaw(0.35, 930);
		HeatRaw bronzeRaw = new HeatRaw(0.35, 950);
		HeatRaw copperRaw = new HeatRaw(0.35, 1080);
		HeatRaw goldRaw = new HeatRaw(0.6, 1060);
		HeatRaw ironRaw = new HeatRaw(0.35, 1535);
		HeatRaw leadRaw = new HeatRaw(0.22, 328);
		HeatRaw nickelRaw = new HeatRaw(0.48, 1453);
		HeatRaw pigIronRaw = new HeatRaw(0.35, 1500);
		HeatRaw platinumRaw = new HeatRaw(0.35, 1730);
		HeatRaw redSteelRaw = new HeatRaw(0.35, 1540);
		HeatRaw roseGoldRaw = new HeatRaw(0.35, 960);
		HeatRaw silverRaw = new HeatRaw(0.48, 961);
		HeatRaw steelRaw = new HeatRaw(0.35, 1540);//sh = 0.63F; boil = 3500; melt = 1540;
		HeatRaw sterlingSilverRaw = new HeatRaw(0.35, 900);//sh = 0.72F; boil = 2212; melt = 893;
		HeatRaw tinRaw = new HeatRaw(0.14, 230);
		HeatRaw zincRaw = new HeatRaw(0.21, 420);//sh = 0.66F; boil = 907; melt = 420;
		
		heatmanager.addIndex(new HeatIndex(new ItemStack(TFCMItems.itemBottleFruitJuice), 0.98D, 150.0D, (new ItemStack(TFCMItems.itemBottleSugar))));
		heatmanager.addIndex(new HeatIndex(new ItemStack(TFCItems.woodenBucketWater), 0.98D, 150.0D, (new ItemStack(TFCMItems.itemBucketHotWater))));
		
		heatmanager.addIndex(new HeatIndex(new ItemStack(TFCMItems.itemCoil_BismuthBronze, 1), bismuthBronzeRaw, new ItemStack(TFCItems.bismuthBronzeUnshaped, 1)));
		heatmanager.addIndex(new HeatIndex(new ItemStack(TFCMItems.itemCoil_BlackBronze, 1), blackBronzeRaw, new ItemStack(TFCItems.blackBronzeUnshaped, 1)));
		heatmanager.addIndex(new HeatIndex(new ItemStack(TFCMItems.itemCoil_BlackSteel, 1), blackSteelRaw, new ItemStack(TFCItems.blackSteelUnshaped, 1)));
		heatmanager.addIndex(new HeatIndex(new ItemStack(TFCMItems.itemCoil_BlueSteel, 1), blueSteelRaw, new ItemStack(TFCItems.blueSteelUnshaped, 1)));
		heatmanager.addIndex(new HeatIndex(new ItemStack(TFCMItems.itemCoil_Bronze, 1), bronzeRaw, new ItemStack(TFCItems.bronzeUnshaped, 1)));
		heatmanager.addIndex(new HeatIndex(new ItemStack(TFCMItems.itemCoil_Copper, 1), copperRaw, new ItemStack(TFCItems.copperUnshaped, 1)));
		heatmanager.addIndex(new HeatIndex(new ItemStack(TFCMItems.itemCoil_WroughtIron, 1), ironRaw, new ItemStack(TFCItems.wroughtIronUnshaped, 1)));
		heatmanager.addIndex(new HeatIndex(new ItemStack(TFCMItems.itemCoil_RedSteel, 1), redSteelRaw, new ItemStack(TFCItems.redSteelUnshaped, 1)));
		heatmanager.addIndex(new HeatIndex(new ItemStack(TFCMItems.itemCoil_Steel, 1), steelRaw, new ItemStack(TFCItems.steelUnshaped, 1)));
	}	
}