package com.rwtema.extrautils.nei;

import codechicken.lib.inventory.InventoryUtils;
import codechicken.microblock.Saw;
import codechicken.nei.FastTransferManager;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.IOverlayHandler;
import codechicken.nei.recipe.IRecipeHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public class FMPMicroBlocksOverlayHandler implements IOverlayHandler {
  int offsetx;
  
  int offsety;
  
  public static class DistributedIngred {
    public boolean isSaw;
    
    public ItemStack stack;
    
    public int invAmount;
    
    public int distributed;
    
    public int numSlots;
    
    public int recipeAmount;
    
    public DistributedIngred(ItemStack item) {
      this.stack = InventoryUtils.copyStack(item, 1);
      this.isSaw = FMPMicroBlocksOverlayHandler.isSaw(item);
    }
  }
  
  public static class IngredientDistribution {
    public FMPMicroBlocksOverlayHandler.DistributedIngred distrib;
    
    public ItemStack permutation;
    
    public Slot[] slots;
    
    public IngredientDistribution(FMPMicroBlocksOverlayHandler.DistributedIngred distrib, ItemStack permutation) {
      this.distrib = distrib;
      this.permutation = permutation;
    }
  }
  
  public FMPMicroBlocksOverlayHandler(int x, int y) {
    this.offsetx = x;
    this.offsety = y;
  }
  
  public FMPMicroBlocksOverlayHandler() {
    this(5, 11);
  }
  
  public void overlayRecipe(GuiContainer gui, IRecipeHandler recipe, int recipeIndex, boolean shift) {
    List<PositionedStack> ingredients = recipe.getIngredientStacks(recipeIndex);
    List<DistributedIngred> ingredStacks = getPermutationIngredients(ingredients);
    if (!clearIngredients(gui, ingredients))
      return; 
    findInventoryQuantities(gui, ingredStacks);
    List<IngredientDistribution> assignedIngredients = assignIngredients(ingredients, ingredStacks);
    if (assignedIngredients == null)
      return; 
    assignIngredSlots(gui, ingredients, assignedIngredients);
    int quantity = calculateRecipeQuantity(assignedIngredients);
    if (quantity != 0)
      moveIngredients(gui, assignedIngredients, quantity); 
  }
  
  private boolean clearIngredients(GuiContainer gui, List<PositionedStack> ingreds) {
    for (PositionedStack pstack : ingreds) {
      for (Slot slot : gui.inventorySlots.inventorySlots) {
        if (slot.xDisplayPosition != pstack.relx + this.offsetx || slot.yDisplayPosition != pstack.rely + this.offsety || 
          !slot.getHasStack())
          continue; 
        FastTransferManager.clickSlot(gui, slot.slotNumber, 0, 1);
        if (slot.getHasStack())
          return false; 
      } 
    } 
    return true;
  }
  
  private void moveIngredients(GuiContainer gui, List<IngredientDistribution> assignedIngredients, int quantity) {
    for (IngredientDistribution distrib : assignedIngredients) {
      ItemStack pstack = distrib.permutation;
      int transferCap = quantity * pstack.stackSize;
      int transferred = 0;
      if (distrib.distrib.isSaw)
        transferCap = 1; 
      int destSlotIndex = 0;
      Slot dest = distrib.slots[0];
      int slotTransferred = 0;
      int slotTransferCap = pstack.getMaxStackSize();
      for (Slot slot : gui.inventorySlots.inventorySlots) {
        if (!slot.getHasStack() || !(slot.inventory instanceof net.minecraft.entity.player.InventoryPlayer))
          continue; 
        ItemStack stack = slot.getStack();
        if (distrib.distrib.isSaw ? 
          !sameItemStack(stack, pstack) : 
          
          !InventoryUtils.canStack(stack, pstack))
          continue; 
        FastTransferManager.clickSlot(gui, slot.slotNumber);
        int amount = Math.min(transferCap - transferred, stack.stackSize);
        for (int c = 0; c < amount; c++) {
          FastTransferManager.clickSlot(gui, dest.slotNumber, 1);
          transferred++;
          slotTransferred++;
          if (slotTransferred >= slotTransferCap) {
            destSlotIndex++;
            if (destSlotIndex == distrib.slots.length) {
              dest = null;
              break;
            } 
            dest = distrib.slots[destSlotIndex];
            slotTransferred = 0;
          } 
        } 
        FastTransferManager.clickSlot(gui, slot.slotNumber);
        if (transferred >= transferCap || dest == null)
          break; 
      } 
    } 
  }
  
  public static boolean sameItemStack(ItemStack stack1, ItemStack stack2) {
    return (stack1 == null || stack2 == null || (stack1.getItem() == stack2.getItem() && (!stack2.getHasSubtypes() || stack2.getItemDamage() == stack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack2, stack1)));
  }
  
  private int calculateRecipeQuantity(List<IngredientDistribution> assignedIngredients) {
    int quantity = Integer.MAX_VALUE;
    for (IngredientDistribution distrib : assignedIngredients) {
      DistributedIngred istack = distrib.distrib;
      if (distrib.distrib.isSaw)
        continue; 
      if (istack.numSlots == 0)
        return 0; 
      int allSlots = istack.invAmount;
      if (allSlots / istack.numSlots > istack.stack.getMaxStackSize())
        allSlots = istack.numSlots * istack.stack.getMaxStackSize(); 
      quantity = Math.min(quantity, allSlots / istack.distributed);
    } 
    return quantity;
  }
  
  private Slot[][] assignIngredSlots(GuiContainer gui, List<PositionedStack> ingredients, List<IngredientDistribution> assignedIngredients) {
    Slot[][] recipeSlots = mapIngredSlots(gui, ingredients);
    HashMap<Slot, Integer> distribution = new HashMap<Slot, Integer>();
    for (int i = 0; i < recipeSlots.length; i++) {
      for (Slot slot : recipeSlots[i]) {
        if (!distribution.containsKey(slot))
          distribution.put(slot, Integer.valueOf(-1)); 
      } 
    } 
    HashSet<Slot> avaliableSlots = new HashSet<Slot>(distribution.keySet());
    HashSet<Integer> remainingIngreds = new HashSet<Integer>();
    ArrayList<LinkedList<Slot>> assignedSlots = new ArrayList<LinkedList<Slot>>();
    int j;
    for (j = 0; j < ingredients.size(); j++) {
      remainingIngreds.add(Integer.valueOf(j));
      assignedSlots.add(new LinkedList<Slot>());
    } 
    while (avaliableSlots.size() > 0 && remainingIngreds.size() > 0) {
      for (Iterator<Integer> iterator = remainingIngreds.iterator(); iterator.hasNext(); ) {
        int k = ((Integer)iterator.next()).intValue();
        boolean assigned = false;
        DistributedIngred istack = ((IngredientDistribution)assignedIngredients.get(k)).distrib;
        for (Slot slot : recipeSlots[k]) {
          if (avaliableSlots.contains(slot)) {
            avaliableSlots.remove(slot);
            if (!slot.getHasStack()) {
              istack.numSlots++;
              ((LinkedList<Slot>)assignedSlots.get(k)).add(slot);
              assigned = true;
              break;
            } 
          } 
        } 
        if (!assigned || istack.numSlots * istack.stack.getMaxStackSize() >= istack.invAmount)
          iterator.remove(); 
      } 
    } 
    for (j = 0; j < ingredients.size(); j++)
      ((IngredientDistribution)assignedIngredients.get(j)).slots = (Slot[])((LinkedList)assignedSlots.get(j)).toArray((Object[])new Slot[0]); 
    return recipeSlots;
  }
  
  private List<IngredientDistribution> assignIngredients(List<PositionedStack> ingredients, List<DistributedIngred> ingredStacks) {
    ArrayList<IngredientDistribution> assignedIngredients = new ArrayList<IngredientDistribution>();
    for (PositionedStack posstack : ingredients) {
      DistributedIngred biggestIngred = null;
      ItemStack permutation = null;
      int biggestSize = 0;
      for (ItemStack pstack : posstack.items) {
        boolean isSaw = isSaw(pstack);
        for (int j = 0; j < ingredStacks.size(); j++) {
          DistributedIngred istack = ingredStacks.get(j);
          if (isSaw == istack.isSaw)
            if (isSaw) {
              if (sameSaw(pstack, istack.stack) && istack.invAmount - istack.distributed >= pstack.stackSize) {
                biggestSize = 1;
                biggestIngred = istack;
                permutation = pstack;
                break;
              } 
            } else if (InventoryUtils.canStack(pstack, istack.stack) && istack.invAmount - istack.distributed >= pstack.stackSize) {
              int relsize = (istack.invAmount - istack.invAmount / istack.recipeAmount * istack.distributed) / pstack.stackSize;
              if (relsize > biggestSize) {
                biggestSize = relsize;
                biggestIngred = istack;
                permutation = pstack;
                break;
              } 
            }  
        } 
      } 
      if (biggestIngred == null)
        return null; 
      biggestIngred.distributed += permutation.stackSize;
      assignedIngredients.add(new IngredientDistribution(biggestIngred, permutation));
    } 
    return assignedIngredients;
  }
  
  public static boolean isSaw(ItemStack a) {
    return (!a.isStackable() && a.getItem() instanceof Saw);
  }
  
  public boolean sameSaw(ItemStack a, ItemStack b) {
    return (a.getItem() == b.getItem() && a.getItem() instanceof Saw && ((Saw)a.getItem()).getCuttingStrength(a) == ((Saw)a.getItem()).getCuttingStrength(b));
  }
  
  private void findInventoryQuantities(GuiContainer gui, List<DistributedIngred> ingredStacks) {
    for (Slot slot : gui.inventorySlots.inventorySlots) {
      if (slot.getHasStack() && slot.inventory instanceof net.minecraft.entity.player.InventoryPlayer) {
        ItemStack pstack = slot.getStack();
        DistributedIngred istack = findIngred(ingredStacks, pstack);
        if (istack != null)
          istack.invAmount += pstack.stackSize; 
      } 
    } 
  }
  
  private List<DistributedIngred> getPermutationIngredients(List<PositionedStack> ingredients) {
    ArrayList<DistributedIngred> ingredStacks = new ArrayList<DistributedIngred>();
    for (PositionedStack posstack : ingredients) {
      for (ItemStack pstack : posstack.items) {
        DistributedIngred istack = findIngred(ingredStacks, pstack);
        if (istack == null)
          ingredStacks.add(istack = new DistributedIngred(pstack)); 
        istack.recipeAmount += pstack.stackSize;
      } 
    } 
    return ingredStacks;
  }
  
  public Slot[][] mapIngredSlots(GuiContainer gui, List<PositionedStack> ingredients) {
    Slot[][] recipeSlotList = new Slot[ingredients.size()][];
    for (int i = 0; i < ingredients.size(); i++) {
      LinkedList<Slot> recipeSlots = new LinkedList<Slot>();
      PositionedStack pstack = ingredients.get(i);
      for (Slot slot : gui.inventorySlots.inventorySlots) {
        if (slot.xDisplayPosition == pstack.relx + this.offsetx && slot.yDisplayPosition == pstack.rely + this.offsety) {
          recipeSlots.add(slot);
          break;
        } 
      } 
      recipeSlotList[i] = recipeSlots.<Slot>toArray(new Slot[0]);
    } 
    return recipeSlotList;
  }
  
  public DistributedIngred findIngred(List<DistributedIngred> ingredStacks, ItemStack pstack) {
    for (DistributedIngred istack : ingredStacks) {
      if (istack.isSaw) {
        if (sameSaw(pstack, istack.stack))
          return istack; 
        continue;
      } 
      if (InventoryUtils.canStack(pstack, istack.stack))
        return istack; 
    } 
    return null;
  }
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\com\rwtema\extrautils\nei\FMPMicroBlocksOverlayHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */