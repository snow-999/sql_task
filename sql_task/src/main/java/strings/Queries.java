package strings;

public class Queries {

    final public static String StoreNameQuery = "select shops.storeName, items.itemName from " + Tables.STORES_ITEMS +
                                " join " + Tables.SHOP + " as shops on STORES_ITEMS.storeId = shops.storeId" +
                                " join " + Tables.ITEMS + " as items on STORES_ITEMS.itemId = items.itemId " +
                                "where shops."+Columns.STORE_NAME +"= ?";

    final public static String StoreIdQuery = "select shops.storeName, items.itemName from " + Tables.STORES_ITEMS +
                                " join " + Tables.SHOP + " as shops on STORES_ITEMS.storeId = shops.storeId" +
                                " join " + Tables.ITEMS + " as items on STORES_ITEMS.itemId = items.itemId " +
                                "where shops."+Columns.STORE_ID +"= ?";

    final public static String itemNameQuery = "select shops.storeName, items.itemName from " + Tables.STORES_ITEMS +
                                " join " + Tables.SHOP + " as shops on STORES_ITEMS.storeId = shops.storeId" +
                                " join " + Tables.ITEMS + " as items on STORES_ITEMS.itemId = items.itemId " +
                                "where items."+Columns.ITEM_NAME +"= ?";

    final public static String itemIdQuery = "select shops.storeName, items.itemName from " + Tables.STORES_ITEMS +
                                " join " + Tables.SHOP + " as shops on STORES_ITEMS.storeId = shops.storeId" +
                                " join " + Tables.ITEMS + " as items on STORES_ITEMS.itemId = items.itemId " +
                                "where items."+Columns.ITEM_ID +"= ?";
}
