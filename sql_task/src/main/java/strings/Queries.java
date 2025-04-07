package strings;

public class Queries {

    final public static String StoreNameQuery = "select sh.storeName, items.itemName from " + TablesNames.STORES_ITEMS + " as si " +
                                " join " + TablesNames.SHOP + " as sh on si.storeId = sh.storeId" +
                                " join " + TablesNames.ITEMS + " as it on si.itemId = it.itemId ";

    final public static String StoreIdQuery = "select sh.storeId from "+ TablesNames.STORES_ITEMS + " as si"+
                                              " join " + TablesNames.SHOP + " as sh on si.storeId = sh.storeId" +
                                              " join " + TablesNames.ITEMS + " as it on si.itemId = it.itemId";

    final public static String itemNameQuery = "select it.itemName from " + TablesNames.STORES_ITEMS + " as si " +
                                " join " + TablesNames.SHOP + " as sh on si.storeId = sh.storeId" +
                                " join " + TablesNames.ITEMS + " as it on si.itemId = it.itemId ";

    final public static String itemIdQuery = "select it.itemId from Stores_items as si" +
                                " join "+ TablesNames.SHOP +" as sh on si.storeId = sh.storeId"+
                                " join "+ TablesNames.ITEMS + " as it on si.itemId = it.itemId";

    final public static String countQuery = "SELECT COUNT(*) FROM " + TablesNames.STORES_ITEMS +
                        " JOIN " + TablesNames.SHOP + " ON STORES_ITEMS.storeId = " + TablesNames.SHOP + ".storeId " +
                        " JOIN " + TablesNames.ITEMS + " ON STORES_ITEMS.itemId = " + TablesNames.ITEMS + ".itemId";
}
