package invtweaks.api;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public interface IItemTree {
  void registerOre(String paramString1, String paramString2, String paramString3, int paramInt);
  
  boolean matches(List<IItemTreeItem> paramList, String paramString);
  
  boolean isKeywordValid(String paramString);
  
  Collection<IItemTreeCategory> getAllCategories();
  
  IItemTreeCategory getRootCategory();
  
  IItemTreeCategory getCategory(String paramString);
  
  boolean isItemUnknown(String paramString, int paramInt);
  
  List<IItemTreeItem> getItems(String paramString, int paramInt);
  
  List<IItemTreeItem> getItems(String paramString);
  
  IItemTreeItem getRandomItem(Random paramRandom);
  
  boolean containsItem(String paramString);
  
  boolean containsCategory(String paramString);
  
  void setRootCategory(IItemTreeCategory paramIItemTreeCategory);
  
  IItemTreeCategory addCategory(String paramString1, String paramString2) throws NullPointerException;
  
  void addCategory(String paramString, IItemTreeCategory paramIItemTreeCategory) throws NullPointerException;
  
  IItemTreeItem addItem(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2) throws NullPointerException;
  
  void addItem(String paramString, IItemTreeItem paramIItemTreeItem) throws NullPointerException;
  
  int getKeywordDepth(String paramString);
  
  int getKeywordOrder(String paramString);
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\invtweaks\api\IItemTree.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */