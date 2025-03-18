package org.example;
import strings.Columns;
import strings.Operations;

import strings.Queries;
import strings.Tables;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void insertIntoTable(Connection connection) throws SQLException {
        Scanner scan = new Scanner(System.in);

        System.out.println("Tables:" + Tables.SHOP + Tables.ITEMS + Tables.STORES_ITEMS);
        System.out.println("enter table name");
        String tableName = scan.nextLine().toLowerCase();

        switch (tableName) {
            case Tables.SHOP -> {
                System.out.println("enter store name");
                String storeName = scan.next().toLowerCase();
                String query = "insert into "+ Tables.SHOP +" (storeName) values (?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                preparedStatement.setString(1, storeName);
                preparedStatement.executeUpdate();
            }
            case Tables.ITEMS -> {
                System.out.println("enter item name");
                String itemName = scan.next().toLowerCase();

                String query = "insert into " +Tables.ITEMS +" (itemName) values (?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                preparedStatement.setString(1, itemName);
                preparedStatement.executeUpdate();
            }
            case Tables.STORES_ITEMS -> {
                System.out.println("enter item code");
                int itemID = scan.nextInt();

                System.out.println("enter store code");
                int storeID = scan.nextInt();

                String query = "insert into "+ Tables.STORES_ITEMS +" (storeID, itemId) values (?,?)";

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1,storeID);
                preparedStatement.setInt(2,itemID);
                preparedStatement.executeUpdate();
            }
            default -> System.out.println("no table with that name");
        }
    }

    public static int getTotalRecords(Connection connection) throws SQLException {
        String countQuery = Queries.countQuery;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(countQuery);

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static void showPagination(Connection connection, int limit, int currentPage, String col) throws SQLException {
        int totalRecords = getTotalRecords(connection);

        int totalPages = (int) Math.ceil((double) totalRecords / limit);

        System.out.println("total pages: " + totalPages);
        System.out.println("you are currently on page " + currentPage + " out of " + totalPages);
        selectByColName(connection, limit, currentPage, col);
    }

    private static void selectByColName(Connection connection, int limit, int pageNumber, String col) throws SQLException {
        int offset = (pageNumber - 1) * limit;

        Scanner scan = new Scanner(System.in);
        switch (col) {
            case Columns.ITEM_ID -> {
                System.out.println("Enter item id");
                String query = Queries.itemIdQuery + " limit "+ limit +" offset "+ offset ;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int itemId = resultSet.getInt("itemId");
                    System.out.println("item is: "+ itemId);
                }
            }

            case Columns.ITEM_NAME -> {
                String query = Queries.itemNameQuery + " limit "+ limit +" offset "+ offset ;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String itemName = resultSet.getString("itemName");
                    System.out.println("item name: " + itemName);
                }
            }

            case Columns.STORE_ID  -> {
                String query = Queries.StoreIdQuery + " limit "+ limit +" offset "+ offset ;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String storeId = resultSet.getString("storeId");
                    System.out.println("stores ids: "+ storeId);
                }
            }

            case Columns.STORE_NAME -> {
                System.out.println("Enter Store Name");

                String query = Queries.StoreNameQuery + " limit "+ limit +" offset "+ offset ;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String storeNme = resultSet.getString("storeName");
                    String itemNme = resultSet.getString("itemName");
                    System.out.println("store name: " + storeNme+ " has item Name "+ itemNme);
                }
            }
        }
    }

    public static void select(Connection connection, int limit, int pageNumber) throws SQLException {
        int offset = (pageNumber - 1) * limit;

        String query = "select "+ Tables.SHOP +".storeName, ITEMS.itemName from " + Tables.STORES_ITEMS +
                       " join " + Tables.SHOP + " on STORES_ITEMS.storeId = "+ Tables.SHOP +".storeId" +
                       " join " + Tables.ITEMS + " on STORES_ITEMS.itemId = "+ Tables.ITEMS +".itemId " +
                       "limit "+ limit +" offset "+ offset +" ";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String storeNme = resultSet.getString("storeName");
            String itemNme =  resultSet.getString("itemName");
            System.out.println("store name: " + storeNme + " has " + itemNme);
        }
    }

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
                    case Operations.INSERT -> insertIntoTable(connection);
                    case Operations.SELECT -> {
                        System.out.println("select a method to select by search by: " + Columns.ITEM_ID + ", "+ Columns.ITEM_NAME + ", "+Columns.STORE_ID +", "+Columns.STORE_NAME);
                        String col = scan.next();

                        int totalRecords = getTotalRecords(connection);
                        int limit = 2;
                        int startPage = 1;
                        int totalPages = (int) Math.ceil((double) totalRecords / limit);
                        showPagination(connection, limit, startPage, col);
                        while (true) {

                            System.out.println("go next/previous or exit");
                            String option = scan.next().toLowerCase();

                            if (option.equals(Operations.EXIT)) break;

                            switch (option) {
                                case Operations.CONTINUE -> {
                                    if (totalPages > startPage){
                                        showPagination(connection, limit, ++startPage, col);
                                    }  else {
                                        System.out.println(Operations.UNAVAILABLE);
                                    }
                                }
                                case Operations.PREVIOUS -> {
                                    if (startPage <= totalPages && startPage > 1) {
                                        showPagination(connection, limit, --startPage, col);
                                    } else {
                                        System.out.println(Operations.UNAVAILABLE);
                                    }
                                }
                            }
                        }
                    }
                }
                statement.close();
                connection.close();
            } catch (SQLException e) {
                System.err.println("SQL Error: "
                                   + e.getMessage());
            }
        }
    }
}