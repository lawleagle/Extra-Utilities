package com.rwtema.extrautils.nei;

import codechicken.microblock.MicroMaterialRegistry;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.rwtema.extrautils.multipart.FMPBase;
import com.rwtema.extrautils.multipart.microblock.RecipeMicroBlocks;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;

public class MicroBlocksHandler extends ShapedRecipeHandler {
  public static String[] currentMaterials = null;
  
  public static ItemStack[] currentBlocks = null;
  
  public static Set<RecipeMicroBlocks> recipes = null;
  
  public String currentMaterial = "";
  
  public ItemStack currentBlock = null;
  
  public boolean scroll = true;
  
  public static Set<RecipeMicroBlocks> getCraftingRecipes() {
    if (recipes == null) {
      recipes = new HashSet<RecipeMicroBlocks>();
      List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
      for (IRecipe irecipe : allrecipes) {
        if (irecipe instanceof RecipeMicroBlocks)
          recipes.add((RecipeMicroBlocks)irecipe); 
      } 
    } 
    return recipes;
  }
  
  public void loadTransferRects() {
    this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(84, 23, 24, 18), "xu_microblocks_crafting", new Object[0]));
  }
  
  public void loadCraftingRecipes(String outputId, Object... results) {
    if (outputId.equals("xu_microblocks_crafting")) {
      for (RecipeMicroBlocks irecipe : getCraftingRecipes()) {
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
    for (RecipeMicroBlocks irecipe : getCraftingRecipes()) {
      if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
        MicroblockCachedRecipe recipe = new MicroblockCachedRecipe(irecipe);
        recipe.computeVisuals();
        this.arecipes.add(recipe);
      } 
    } 
  }
  
  public void loadUsageRecipes(ItemStack ingredient) {
    if (!ingredient.hasTagCompound() || "".equals(ingredient.getTagCompound().getString("mat")))
      return; 
    MicroMaterialRegistry.IMicroMaterial m = MicroMaterialRegistry.getMaterial(ingredient.getTagCompound().getString("mat"));
    if (m == null)
      return; 
    this.scroll = false;
    this.currentMaterial = ingredient.getTagCompound().getString("mat");
    this.currentBlock = m.getItem().copy();
    for (RecipeMicroBlocks irecipe : getCraftingRecipes()) {
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
    return "Extra Utilities: Microblocks";
  }
  
  public class MicroblockCachedRecipe extends TemplateRecipeHandler.CachedRecipe {
    public ArrayList<PositionedStack> ingredients;
    
    public MicroblockPositionedStack result;
    
    public MicroblockCachedRecipe(int width, int height, Object[] items, ItemStack out) {
      super((TemplateRecipeHandler)MicroBlocksHandler.this);
      this.result = new MicroblockPositionedStack(out, 119, 24);
      this.ingredients = new ArrayList<PositionedStack>();
      setIngredients(width, height, items);
    }
    
    public MicroblockCachedRecipe(RecipeMicroBlocks irecipe) {
      this(irecipe.recipeWidth, irecipe.recipeHeight, (Object[])irecipe.getRecipeItems(), irecipe.getRecipeOutput());
    }
    
    public void setIngredients(int width, int height, Object[] items) {
      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          if (items[y * width + x] != null) {
            PositionedStack stack;
            if (items[y * width + x] instanceof ItemStack && ((ItemStack)items[y * width + x]).getItem() == FMPBase.getMicroBlockItemId()) {
              stack = new MicroblockPositionedStack((ItemStack)items[y * width + x], 25 + x * 18, 6 + y * 18);
            } else {
              stack = new PositionedStack(items[y * width + x], 25 + x * 18, 6 + y * 18, false);
            } 
            stack.setMaxSize(1);
            this.ingredients.add(stack);
          } 
        } 
      } 
    }
    
    public void permMaterial() {
      if (!MicroBlocksHandler.this.scroll)
        return; 
      if (MicroBlocksHandler.currentMaterials == null) {
        MicroBlocksHandler.currentMaterials = new String[(MicroMaterialRegistry.getIdMap()).length];
        for (int i = 0; i < (MicroMaterialRegistry.getIdMap()).length; i++)
          MicroBlocksHandler.currentMaterials[i] = (String)MicroMaterialRegistry.getIdMap()[i]._1(); 
      } 
      if (MicroBlocksHandler.currentBlocks == null) {
        MicroBlocksHandler.currentBlocks = new ItemStack[(MicroMaterialRegistry.getIdMap()).length];
        for (int i = 0; i < (MicroMaterialRegistry.getIdMap()).length; i++)
          MicroBlocksHandler.currentBlocks[i] = ((MicroMaterialRegistry.IMicroMaterial)MicroMaterialRegistry.getIdMap()[i]._2()).getItem().copy(); 
      } 
      MicroBlocksHandler.this.currentMaterial = MicroBlocksHandler.currentMaterials[MicroBlocksHandler.this.cycleticks / 20 % MicroBlocksHandler.currentMaterials.length];
      MicroBlocksHandler.this.currentBlock = MicroBlocksHandler.currentBlocks[MicroBlocksHandler.this.cycleticks / 20 % MicroBlocksHandler.currentMaterials.length];
    }
    
    public List<PositionedStack> getIngredients() {
      return getCycledIngredients(MicroBlocksHandler.this.cycleticks / 20, this.ingredients);
    }
    
    public List<PositionedStack> getCycledIngredients(int cycle, List<PositionedStack> ingredients) {
      return super.getCycledIngredients(cycle, ingredients);
    }
    
    public void randomRenderPermutation(PositionedStack stack, long cycle) {
      stack.setPermutationToRender(0);
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
      
      public MicroblockPositionedStack(ItemStack object, int x, int y) {
        super(object, x, y, false);
        this.item = this.items[0].copy();
        this.materialTag = (this.item.getItem() != FMPBase.getMicroBlockItemId() || this.item.getItemDamage() != 0);
      }
      
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
        MicroBlocksHandler.MicroblockCachedRecipe.this.permMaterial();
        if (this.materialTag) {
          addMaterial();
        } else {
          this.items[0] = MicroBlocksHandler.this.currentBlock.copy();
        } 
        super.setPermutationToRender(0);
      }
      
      public void addMaterial() {
        NBTTagCompound tag = this.items[0].getTagCompound();
        if (tag == null)
          tag = new NBTTagCompound(); 
        tag.setString("mat", MicroBlocksHandler.this.currentMaterial);
        this.items[0].setTagCompound(tag);
      }
    }
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\nei\MicroBlocksHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */