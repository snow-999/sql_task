package strings;

public class Queries {

    final public static String StoreNameQuery = "select shops.storeName, items.itemName from " + Tables.STORES_ITEMS + " as si " +
                                " join " + Tables.SHOP + " as shops on si.storeId = shops.storeId" +
                                " join " + Tables.ITEMS + " as items on si.itemId = items.itemId " +
                                "where shops."+Columns.STORE_NAME +"= ?";

    final public static String StoreIdQuery = "select shop.storeName, items.itemName from stores_items" +
                                              " join shop on stores_items.storeId = shop.storeId" +
                                              " join items on stores_items.itemId = items.itemId" +
                                              " where shop.storeId = ?";

    final public static String itemNameQuery = "select shops.storeName, items.itemName from " + Tables.STORES_ITEMS + " as si " +
                                " join " + Tables.SHOP + " as shops on si.storeId = shops.storeId" +
                                " join " + Tables.ITEMS + " as items on si.itemId = items.itemId " +
                                "where items."+Columns.ITEM_NAME +"= ?";

    final public static String itemIdQuery = "select shop.storeName, items.itemName from stores_items as si" +
                                " join shop on si.storeId = shop.storeId"+
                                " join items on si.itemId = items.itemId"+
                                " where items.itemId = ?";
}
