package invtweaks.api;

import java.util.Collection;
import java.util.List;

public interface IItemTreeCategory {
  boolean contains(IItemTreeItem paramIItemTreeItem);
  
  void addCategory(IItemTreeCategory paramIItemTreeCategory);
  
  void addItem(IItemTreeItem paramIItemTreeItem);
  
  Collection<IItemTreeCategory> getSubCategories();
  
  Collection<List<IItemTreeItem>> getItems();
  
  String getName();
  
  int getCategoryOrder();
  
  int findCategoryOrder(String paramString);
  
  int findKeywordDepth(String paramString);
}


/* Location:              C:\Users\Emanuel\src\` decompile\mods\extrautilities-1.2.12-deobf.jar!\invtweaks\api\IItemTreeCategory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */