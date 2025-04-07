package org.example;
import operations.GeneralItemManager;
import strings.Columns;
import strings.Operations;
import strings.Tables;
import tables.Items;
import tables.Shop;
import tables.Stores_items;

import java.sql.*;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
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

                switch (operation) {
                    case Operations.INSERT -> {
                        System.out.println("select a table to insert to: " + Tables.STORES_ITEMS + ", "+ Tables.ITEMS + ", "+Tables.SHOP +".");
                        String table = scan.next();

                        switch (table) {
                            case Tables.SHOP -> {
                                Shop shop = new Shop();
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
                                Items items = new Items();
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
                                Stores_items stores_items = new Stores_items();
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
                        System.out.println("select a method to select by search by: " + Columns.ITEM_ID + ", "+ Columns.ITEM_NAME + ", "+Columns.STORE_ID +", "+Columns.STORE_NAME);
                        String col = scan.next();

                        int totalRecords = GeneralItemManager.getTotalRecords(connection);
                        int limit = 2;
                        int startPage = 1;
                        int totalPages = (int) Math.ceil((double) totalRecords / limit);
                        GeneralItemManager.showPagination(connection, limit, startPage, col);
                        while (true) {

                            System.out.println("go next/previous or exit");
                            String option = scan.next().toLowerCase();

                            if (option.equals(Operations.EXIT)) break;

                            switch (option) {
                                case Operations.CONTINUE -> {
                                    if (totalPages > startPage){
                                        GeneralItemManager.showPagination(connection, limit, ++startPage, col);
                                    }  else {
                                        System.out.println(Operations.UNAVAILABLE);
                                    }
                                }
                                case Operations.PREVIOUS -> {
                                    if (startPage <= totalPages && startPage > 1) {
                                        GeneralItemManager.showPagination(connection, limit, --startPage, col);
                                    } else {
                                        System.out.println(Operations.UNAVAILABLE);
                                    }
                                }
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