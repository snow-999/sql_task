package org.example;
import operations.GeneralItemManager;
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
                                System.out.println("Shop Id: ");
                                int shopId = scan.nextInt();
                                System.out.println("Shop Name: ");
                                String shopName = scan.next();
                                shop.setStoreId(shopId);
                                shop.setStoreName(shopName);
                                GeneralItemManager<Shop> shopGeneralItemManager = new GeneralItemManager<>();
                                shopGeneralItemManager.insertIntoTable(connection, shop);
                            }
                            case TablesNames.ITEMS -> {
                                System.out.println("Shop Id: ");
                                int itemId = scan.nextInt();
                                System.out.println("Shop Name: ");
                                String itemName = scan.next();
                                item.setItemId(itemId);
                                item.setItemName(itemName);
                                GeneralItemManager<Items> itemsGeneralItemManager = new GeneralItemManager<>();
                                itemsGeneralItemManager.insertIntoTable(connection, item);
                            }
                            case TablesNames.STORES_ITEMS -> {
                                System.out.println("Item Id: ");
                                int storeItemId = scan.nextInt();
                                System.out.println("Shop id: ");
                                int storeId = scan.nextInt();
                                stores_items.setItemId(storeItemId);
                                stores_items.setStoreId(storeId);
                                GeneralItemManager<Stores_items> stores_itemsGeneralItemManager = new GeneralItemManager<>();
                                stores_itemsGeneralItemManager.insertIntoTable(connection, stores_items);
                            }
                        }
                    }
                    case Operations.SELECT -> {

                        int totalRecords = GeneralItemManager.getTotalRecords(connection);
                        int limit = 2;
                        int startPage = 1;
                        int totalPages = (int) Math.ceil((double) totalRecords / limit);

                        System.out.println("TablesNames To Select From: " + TablesNames.SHOP + ", "+ TablesNames.ITEMS + ", "+ TablesNames.STORES_ITEMS +".");

                        String tableName = scan.next();
                        switch (tableName) {
                            case TablesNames.SHOP -> {
                                GeneralItemManager<Shop> generalItemManager = new GeneralItemManager<>();
                                List<Shop> shops = generalItemManager.showPagination(connection, shop, limit, startPage);
                                for (Shop shp : shops) {
                                    System.out.println("Shop Name is: "+ shp.getStoreName()+ " Shop Id is : "+ shp.getStoreId());
                                }
                                shop.startShopPagination(connection, shop, limit, totalPages, startPage);
                            }
                            case TablesNames.ITEMS -> {
                                GeneralItemManager<Items> generalItemManager = new GeneralItemManager<>();
                                List<Items> items = generalItemManager.showPagination(connection, item, limit, startPage);
                                for (Items itm : items) {
                                    System.out.println("Shop Name is: "+ itm.getItemName()+ " Shop Id is : "+ itm.getItemId());
                                }
                                item.startItemsPagination(connection, item, limit, totalPages, startPage);
                            }
                            case TablesNames.STORES_ITEMS -> {
                                GeneralItemManager<Stores_items> generalItemManager = new GeneralItemManager<>();
                                generalItemManager.showPagination(connection, stores_items, limit, startPage);
                                generalItemManager.startPagination(connection, stores_items, limit, startPage, totalPages);
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