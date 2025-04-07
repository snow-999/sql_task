package strings;

public class Queries {

    final public static String StoreNameQuery = "select sh.storeName, items.itemName from " + Tables.STORES_ITEMS + " as si " +
                                " join " + Tables.SHOP + " as sh on si.storeId = sh.storeId" +
                                " join " + Tables.ITEMS + " as it on si.itemId = it.itemId ";

    final public static String StoreIdQuery = "select sh.storeId from "+ Tables.STORES_ITEMS + " as si"+
                                              " join " + Tables.SHOP + " as sh on si.storeId = sh.storeId" +
                                              " join " + Tables.ITEMS + " as it on si.itemId = it.itemId";

    final public static String itemNameQuery = "select it.itemName from " + Tables.STORES_ITEMS + " as si " +
                                " join " + Tables.SHOP + " as sh on si.storeId = sh.storeId" +
                                " join " + Tables.ITEMS + " as it on si.itemId = it.itemId ";

    final public static String itemIdQuery = "select it.itemId from Stores_items as si" +
                                " join "+ Tables.SHOP +" as sh on si.storeId = sh.storeId"+
                                " join "+ Tables.ITEMS + " as it on si.itemId = it.itemId";

    final public static String countQuery = "SELECT COUNT(*) FROM " + Tables.STORES_ITEMS +
                        " JOIN " + Tables.SHOP + " ON STORES_ITEMS.storeId = " + Tables.SHOP + ".storeId " +
                        " JOIN " + Tables.ITEMS + " ON STORES_ITEMS.itemId = " + Tables.ITEMS + ".itemId";
}
