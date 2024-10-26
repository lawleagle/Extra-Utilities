package com.rwtema.extrautils.nei;

import codechicken.microblock.MicroMaterialRegistry;
import codechicken.microblock.MicroRecipe;
import codechicken.microblock.Saw;
import codechicken.nei.ItemList;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.rwtema.extrautils.multipart.FMPBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;

@SideOnly(Side.CLIENT)
public class FMPMicroBlocksHandler extends ShapedRecipeHandler {
  public static String[] currentMaterials = null;

  public static ItemStack[] currentBlocks = null;

  public static ArrayList<ShapedRecipes> recipes = null;

  public String currentMaterial = "";

  public ItemStack currentBlock = null;

  public boolean scroll = true;

  public static HashSet<ItemStack> sawList = null;

  public static final String identifier = "microblocks";

  public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
    if (RecipeInfo.hasDefaultOverlay(gui, "microblocks") || RecipeInfo.hasOverlayHandler(gui, "microblocks"))
      return true;
    if (isRecipe2x2(recipe) && RecipeInfo.hasDefaultOverlay(gui, "microblocks2x2"))
      return true;
    return super.hasOverlay(gui, container, recipe);
  }

  public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipe) {
    IOverlayHandler handler = RecipeInfo.getOverlayHandler(gui, "microblocks");
    if (handler != null)
      return handler;
    if (isRecipe2x2(recipe)) {
      handler = RecipeInfo.getOverlayHandler(gui, "microblocks2x2");
      if (handler != null)
        return handler;
    }
    return super.getOverlayHandler(gui, recipe);
  }

  public static HashSet<ItemStack> getSawList() {
    if (sawList == null) {
      sawList = new HashSet<ItemStack>();
      synchronized (ItemList.class) {
        for (Item item : ItemList.itemMap.keySet()) {
          if (item instanceof Saw)
            for (ItemStack stack : ItemList.itemMap.get(item))
              sawList.add(stack);
        }
      }
    }
    return sawList;
  }

  public ItemStack[] getSaws() {
    if (this.scroll || "".equals(this.currentMaterial) || this.currentBlock == null)
      return getSawList().<ItemStack>toArray(new ItemStack[0]);
    int p = MicroMaterialRegistry.getMaterial(this.currentMaterial).getCutterStrength();
    HashSet<ItemStack> s = new HashSet<ItemStack>();
    for (ItemStack saw : getSawList()) {
      int sawStrength = ((Saw)saw.getItem()).getCuttingStrength(saw);
      if (sawStrength >= p || sawStrength == MicroMaterialRegistry.getMaxCuttingStrength())
        s.add(saw);
    }
    return s.<ItemStack>toArray(new ItemStack[s.size()]);
  }

  public static ArrayList<ShapedRecipes> getCraftingRecipes() {
    if (recipes == null || recipes.size() == 0)
      recipes = FMPMicroBlockRecipeCreator.loadRecipes();
    return recipes;
  }

  public void loadTransferRects() {
    this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(84, 23, 24, 18), "microblocks", new Object[0]));
  }

  public void loadCraftingRecipes(String outputId, Object... results) {
    if (outputId.equals("microblocks")) {
      for (ShapedRecipes irecipe : getCraftingRecipes()) {
        MicroblockCachedRecipe recipe = new MicroblockCachedRecipe(irecipe);
        recipe.computeVisuals();
        this.arecipes.add(recipe);
      }
      this.scroll = true;
      this.currentMaterial = "";
      this.currentBlock = null;
    } else {
      super.loadCraftingRecipes(outputId, results);
    }
  }

  public void loadCraftingRecipes(ItemStack result) {
    if (!result.hasTagCompound() || "".equals(result.getTagCompound().getString("mat")))
      return;
    MicroMaterialRegistry.IMicroMaterial m = MicroMaterialRegistry.getMaterial(result.getTagCompound().getString("mat"));
    if (m == null)
      return;
    this.scroll = false;
    this.currentMaterial = result.getTagCompound().getString("mat");
    this.currentBlock = m.getItem().copy();
    for (ShapedRecipes irecipe : getCraftingRecipes()) {
      if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
        MicroblockCachedRecipe recipe = new MicroblockCachedRecipe(irecipe);
        recipe.computeVisuals();
        this.arecipes.add(recipe);
      }
    }
  }

  public void loadUsageRecipes(ItemStack ingredient) {
    if (ingredient.getItem() instanceof Saw) {
      this.scroll = true;
      this.currentMaterial = "";
      this.currentBlock = null;
    } else if (!ingredient.hasTagCompound() || "".equals(ingredient.getTagCompound().getString("mat"))) {
      int id = MicroRecipe.findMaterial(ingredient);
      if (id < 0)
        return;
      MicroMaterialRegistry.IMicroMaterial m = MicroMaterialRegistry.getMaterial(id);
      if (m == null)
        return;
      this.scroll = false;
      this.currentMaterial = MicroMaterialRegistry.materialName(id);
      this.currentBlock = m.getItem().copy();
    } else {
      MicroMaterialRegistry.IMicroMaterial m = MicroMaterialRegistry.getMaterial(ingredient.getTagCompound().getString("mat"));
      if (m == null)
        return;
      this.scroll = false;
      this.currentMaterial = ingredient.getTagCompound().getString("mat");
      this.currentBlock = m.getItem().copy();
    }
    for (ShapedRecipes irecipe : getCraftingRecipes()) {
      MicroblockCachedRecipe recipe = new MicroblockCachedRecipe(irecipe);
      recipe.computeVisuals();
      if (recipe.contains(recipe.ingredients, ingredient)) {
        recipe.setIngredientPermutation(recipe.ingredients, ingredient);
        this.arecipes.add(recipe);
      }
    }
  }

  public String getGuiTexture() {
    return "textures/gui/container/crafting_table.png";
  }

  public String getRecipeName() {
    return "Microblock Crafting";
  }

  public class MicroblockCachedRecipe extends TemplateRecipeHandler.CachedRecipe {
    public ArrayList<PositionedStack> ingredients;

    public MicroblockPositionedStack result;

    public MicroblockCachedRecipe(int width, int height, Object[] items, ItemStack out) {
      super();
      this.result = new MicroblockPositionedStack(out, 119, 24);
      this.ingredients = new ArrayList<PositionedStack>();
      setIngredients(width, height, items);
    }

    public MicroblockCachedRecipe(ShapedRecipes irecipe) {
      this(irecipe.recipeWidth, irecipe.recipeHeight, (Object[])irecipe.recipeItems, irecipe.getRecipeOutput());
    }

    public void setIngredients(int width, int height, Object[] items) {
      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          if (y * width + x <= items.length && items[y * width + x] != null) {
            PositionedStack stack;
            ItemStack item = (ItemStack)items[y * width + x];
            if (item.getItem() == FMPMicroBlockRecipeCreator.stone.getItem()) {
              stack = new MicroblockPositionedStack(item, 25 + x * 18, 6 + y * 18);
            } else if (item == FMPMicroBlockRecipeCreator.saw) {
              stack = new PositionedStack(FMPMicroBlocksHandler.this.getSaws(), 25 + x * 18, 6 + y * 18, false);
            } else if (item.getItem() == FMPBase.getMicroBlockItemId()) {
              stack = new MicroblockPositionedStack((ItemStack)items[y * width + x], 25 + x * 18, 6 + y * 18);
            } else {
              stack = new PositionedStack(items[y * width + x], 25 + x * 18, 6 + y * 18);
            }
            stack.setMaxSize(1);
            this.ingredients.add(stack);
          }
        }
      }
    }

    public void permMaterial() {
      if (!FMPMicroBlocksHandler.this.scroll)
        return;
      if (FMPMicroBlocksHandler.currentMaterials == null) {
        FMPMicroBlocksHandler.currentMaterials = new String[(MicroMaterialRegistry.getIdMap()).length];
        for (int i = 0; i < (MicroMaterialRegistry.getIdMap()).length; i++)
          FMPMicroBlocksHandler.currentMaterials[i] = (String)MicroMaterialRegistry.getIdMap()[i]._1();
      }
      if (FMPMicroBlocksHandler.currentBlocks == null) {
        FMPMicroBlocksHandler.currentBlocks = new ItemStack[(MicroMaterialRegistry.getIdMap()).length];
        for (int i = 0; i < (MicroMaterialRegistry.getIdMap()).length; i++)
          FMPMicroBlocksHandler.currentBlocks[i] = ((MicroMaterialRegistry.IMicroMaterial)MicroMaterialRegistry.getIdMap()[i]._2()).getItem().copy();
      }
      FMPMicroBlocksHandler.this.currentMaterial = FMPMicroBlocksHandler.currentMaterials[FMPMicroBlocksHandler.this.cycleticks / 20 % FMPMicroBlocksHandler.currentMaterials.length];
      FMPMicroBlocksHandler.this.currentBlock = FMPMicroBlocksHandler.currentBlocks[FMPMicroBlocksHandler.this.cycleticks / 20 % FMPMicroBlocksHandler.currentMaterials.length];
    }

    public List<PositionedStack> getIngredients() {
      return getCycledIngredients(FMPMicroBlocksHandler.this.cycleticks / 20, this.ingredients);
    }

    public void randomRenderPermutation(PositionedStack stack, long cycle) {
      super.randomRenderPermutation(stack, cycle);
    }

    public PositionedStack getResult() {
      this.result.setPermutationToRender(0);
      return this.result;
    }

    public void computeVisuals() {
      for (PositionedStack p : this.ingredients)
        p.generatePermutations();
      this.result.generatePermutations();
    }

    public class MicroblockPositionedStack extends PositionedStack {
      boolean materialTag = false;

      int stacksize;

      public MicroblockPositionedStack(ItemStack object, int x, int y) {
        super(object, x, y, true);
        this.stacksize = object.stackSize;
        this.item = this.items[0].copy();
        this.materialTag = (this.item.getItem() == FMPBase.getMicroBlockItemId() && this.item.getItemDamage() != 0 && this.item.getItemDamage() != 8);
        setPermutationToRender(0);
      }

      public void generatePermutations() {}

      public void setItem(ItemStack item) {
        if (item != null) {
          this.item = item.copy();
          this.items[0] = item.copy();
        } else {
          this.item = null;
        }
      }

      public void setPermutationToRender(int index) {
        if (this.item == null)
          return;
        FMPMicroBlocksHandler.MicroblockCachedRecipe.this.permMaterial();
        if (this.materialTag) {
          addMaterial();
        } else {
          this.items[0] = FMPMicroBlocksHandler.this.currentBlock.copy();
          (this.items[0]).stackSize = this.stacksize;
          this.item = this.items[0];
        }
        super.setPermutationToRender(0);
      }

      public void addMaterial() {
        NBTTagCompound tag = this.items[0].getTagCompound();
        if (tag == null)
          tag = new NBTTagCompound();
        tag.setString("mat", FMPMicroBlocksHandler.this.currentMaterial);
        this.items[0].setTagCompound(tag);
      }
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\nei\FMPMicroBlocksHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */
