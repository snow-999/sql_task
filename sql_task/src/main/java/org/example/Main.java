package org.example;
import operations.DatBaseManager;
import strings.*;
import tables.*;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InvocationTargetException, InstantiationException, NoSuchMethodException {
        final String URL = "jdbc:mysql://localhost:3306/SHOP";
        final String USER_NAME = "root";
        final String PASSWORD = "1230459078150@khaled";

        while (true) {
            Scanner scan = new Scanner(System.in);
            System.out.println("chose an operation insert, select or exit if you finished");
            String operation = scan.nextLine().toLowerCase();

            if (operation.equals(Operations.EXIT)) break;

            try {
                Connection connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
                Statement statement = connection.createStatement();

                Shop shop = new Shop();
                Items item = new Items();
                Stores_items stores_items = new Stores_items();

                switch (operation) {
                    case Operations.INSERT -> {
                        System.out.println("select a table to insert to: " + TablesNames.STORES_ITEMS + ", "+ TablesNames.ITEMS + ", "+ TablesNames.SHOP +".");
                        String table = scan.next();

                        switch (table) {
                            case TablesNames.SHOP -> {
                                System.out.println("Option To Chose From To Insert: Id, ShopName Or All.");
                                String ans = scan.next().toLowerCase();
                                if (ans.equals("id")) {
                                    System.out.println("Shop Id: ");
                                    int shopId = scan.nextInt();
                                    shop.setStoreId(shopId);
                                    shop.setStoreName("");
                                } else if (ans.equals("shopname")) {
                                    System.out.println("Shop Name: ");
                                    String shopName = scan.next();
                                    shop.setStoreName(shopName);
                                } else {
                                    System.out.println("Shop Id: ");
                                    int shopId = scan.nextInt();
                                    System.out.println("Shop Name: ");
                                    String shopName = scan.next();
                                    shop.setStoreId(shopId);
                                    shop.setStoreName(shopName);
                                }
                                DatBaseManager<Shop> shopGeneralItemManager = new DatBaseManager<>();
                                shopGeneralItemManager.insertIntoTable(shop);
                            }
                            case TablesNames.ITEMS -> {
                                System.out.println("Option To Chose From To Insert: Id, ItemName Or All");
                                String ans = scan.next().toLowerCase();
                                if (ans.equals("id")) {
                                    System.out.println("Item Id: ");
                                    int itemId = scan.nextInt();
                                    item.setItemId(itemId);
                                    item.setItemName("");
                                } else if (ans.equals("itemname")) {
                                    System.out.println("Item Name: ");
                                    String itemName = scan.next();
                                    item.setItemName(itemName);
                                } else {
                                    System.out.println("Item Id: ");
                                    int itemId = scan.nextInt();
                                    System.out.println("Item Name: ");
                                    String itemName = scan.next();
                                    item.setItemId(itemId);
                                    item.setItemName(itemName);
                                }
                                DatBaseManager<Items> itemsGeneralItemManager = new DatBaseManager<>();
                                itemsGeneralItemManager.insertIntoTable(item);
                            }
                            case TablesNames.STORES_ITEMS -> {
                                System.out.println("Item Id: ");
                                int storeItemId = scan.nextInt();
                                System.out.println("Shop id: ");
                                int storeId = scan.nextInt();
                                stores_items.setItemId(storeItemId);
                                stores_items.setStoreId(storeId);
                                DatBaseManager<Stores_items> stores_itemsGeneralItemManager = new DatBaseManager<>();
                                stores_itemsGeneralItemManager.insertIntoTable(stores_items);
                            }
                        }
                    }
                    case Operations.SELECT -> {

                        int totalRecords = DatBaseManager.getTotalRecords();
                        int limit = 2;
                        int startPage = 1;
                        int totalPages = (int) Math.ceil((double) totalRecords / limit);

                        System.out.println("TablesNames To Select From: " + TablesNames.SHOP + ", "+ TablesNames.ITEMS + ", "+ TablesNames.STORES_ITEMS +".");

                        String tableName = scan.next();
                        switch (tableName) {
                            case TablesNames.SHOP -> {
                                DatBaseManager<Shop> generalItemManager = new DatBaseManager<>();
                                List<Shop> shops = generalItemManager.selectTable(shop, limit, startPage);

                                for (Shop shp : shops) {
                                    System.out.println("Shop Name is: "+ shp.getStoreName()+ " Shop Id is : "+ shp.getStoreId());
                                }
                                shop.startShopPagination(shop, limit, totalPages, startPage);
                            }
                            case TablesNames.ITEMS -> {
                                DatBaseManager<Items> generalItemManager = new DatBaseManager<>();
                                List<Items> items = generalItemManager.selectTable(item, limit, startPage);
                                for (Items itm : items) {
                                    System.out.println("item Name is: "+ itm.getItemName()+ " item Id is : "+ itm.getItemId());
                                }
                                item.startItemsPagination(item, limit, totalPages, startPage);
                            }
                            case TablesNames.STORES_ITEMS -> {
                                DatBaseManager<Stores_items> generalItemManager = new DatBaseManager<>();
                                List<Stores_items> stores_item = generalItemManager.selectTable(stores_items, limit, startPage);
                                for (Stores_items itm : stores_item) {
                                    System.out.println("item id: " + itm.getItemId()+"and it exists in shop Id: "+itm.getStoreId());
                                }
                                item.startItemsPagination(item, limit, totalPages, startPage);
                            }
                        }

                    }
                    case Operations.FINISHED -> {return;}
                }
                statement.close();
                connection.close();
            } catch (SQLException | IllegalAccessException e) {
                System.err.println("SQL Error: "
                                   + e.getMessage());
            }
        }
    }
}