package org.example;
import operations.GeneralItemManager;
import strings.Operations;
import strings.Tables;
import tables.Items;
import tables.Shop;
import tables.Stores_items;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
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
                Items items = new Items();
                Stores_items stores_items = new Stores_items();

                switch (operation) {
                    case Operations.INSERT -> {
                        System.out.println("select a table to insert to: " + Tables.STORES_ITEMS + ", "+ Tables.ITEMS + ", "+Tables.SHOP +".");
                        String table = scan.next();

                        switch (table) {
                            case Tables.SHOP -> {
                                System.out.println("Shop Id: ");
                                int shopId = scan.nextInt();
                                System.out.println("Shop Name: ");
                                String shopName = scan.next();
                                shop.setStoreId(shopId);
                                shop.setStoreName(shopName);
                                GeneralItemManager<Shop> shopGeneralItemManager = new GeneralItemManager<>();
                                shopGeneralItemManager.insertIntoTable(connection, shop);
                            }
                            case Tables.ITEMS -> {
                                System.out.println("Shop Id: ");
                                int itemId = scan.nextInt();
                                System.out.println("Shop Name: ");
                                String itemName = scan.next();
                                items.setItemId(itemId);
                                items.setItemName(itemName);
                                GeneralItemManager<Items> itemsGeneralItemManager = new GeneralItemManager<>();
                                itemsGeneralItemManager.insertIntoTable(connection, items);
                            }
                            case Tables.STORES_ITEMS -> {
                                System.out.println("Item Id: ");
                                int storeItemId = scan.nextInt();
                                System.out.println("Shop id: ");
                                int storeId = scan.nextInt();
                                stores_items.setItemId(storeItemId);
                                stores_items.setShopId(storeId);
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

                        System.out.println("Tables To Select From: " + Tables.SHOP + ", "+ Tables.ITEMS + ", "+Tables.STORES_ITEMS +".");

                        String tableName = scan.next();
                        switch (tableName) {
                            case Tables.SHOP -> {
                                GeneralItemManager<Shop> generalItemManager = new GeneralItemManager<>();
                                generalItemManager.showPagination(connection, shop, limit, startPage);
                                generalItemManager.startPagination(connection, shop, limit, startPage, totalPages);
                            }
                            case Tables.ITEMS -> {
                                GeneralItemManager<Items> generalItemManager = new GeneralItemManager<>();
                                generalItemManager.showPagination(connection, items, limit, startPage);
                                generalItemManager.startPagination(connection, items, limit, startPage, totalPages);
                            }
                            case Tables.STORES_ITEMS -> {
                                GeneralItemManager<Stores_items> generalItemManager = new GeneralItemManager<>();
                                generalItemManager.showPagination(connection, stores_items, limit, startPage);
                                generalItemManager.startPagination(connection, stores_items, limit, startPage, totalPages);
                            }
                        }

                    }
                    default -> {
                        return;
                    }
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