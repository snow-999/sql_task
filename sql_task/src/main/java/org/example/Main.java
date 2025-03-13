package org.example;
import strings.Operations;
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

                preparedStatement.setString(1,storeName);
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
        String countQuery = "SELECT COUNT(*) FROM " + Tables.STORES_ITEMS +
                            " JOIN " + Tables.SHOP + " ON STORES_ITEMS.storeId = " + Tables.SHOP + ".storeId " +
                            " JOIN " + Tables.ITEMS + " ON STORES_ITEMS.itemId = " + Tables.ITEMS + ".itemId";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(countQuery);

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static void showPagination(Connection connection, int limit, int currentPage) throws SQLException {
        int totalRecords = getTotalRecords(connection);

        int totalPages = (int) Math.ceil((double) totalRecords / limit);

        System.out.println("total records: " + totalRecords);
        System.out.println("total pages: " + totalPages);
        System.out.println("you are currently on page " + currentPage + " out of " + totalPages);
        System.out.println("displaying records from " + ((currentPage - 1) * limit + 1) + " to " + Math.min(currentPage * limit, totalRecords));

        select(connection, limit, currentPage);
    }

    // change stores table to "shops" {done}
    // read and learn about database pagination what is it and in which cases is it used? and apply it in the select function here {done}
    // user should be able to see all the count of available pages and select a specific page then they can go to the next page or the previous
    // edge cases should be handled {done}

    public static void select(Connection connection, int limit, int pageNumber) throws SQLException {
        int offset = (pageNumber - 1) * limit;

        String query = "select "+ Tables.SHOP +".storeName, ITEMS.itemName from " + Tables.STORES_ITEMS +
                            " join " + Tables.SHOP + " on STORES_ITEMS.storeId = "+ Tables.SHOP +".storeId" +
                            " join " + Tables.ITEMS + " on STORES_ITEMS.itemId = "+ Tables.ITEMS +".itemId " +
                            " limit "+ limit +" offset "+ offset +" ";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String storeNme = resultSet.getString("storeName");
            String itemNme =  resultSet.getString("itemName");
            System.out.println("store name: "+storeNme +" has "+ itemNme);
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
                        int totalRecords = getTotalRecords(connection);
                        int limit = 2;
                        int startPage = 1;
                        int totalPages = (int) Math.ceil((double) totalRecords / limit);

                        while (true) {
                            showPagination(connection, limit, startPage);

                            System.out.println("go next/previous or exit");
                            String option = scan.next().toLowerCase();

                            if (option.equals(Operations.EXIT)) break;

                            switch (option) {
                                case Operations.CONTINUE -> {
                                    if (startPage <= totalPages){
                                        showPagination(connection, limit, startPage++);
                                    }  else {
                                        System.out.println(Operations.UNAVAILABLE);
                                    }
                                }
                                case Operations.PREVIOUS -> {
                                    if (startPage <= totalPages) {
                                        showPagination(connection, limit, startPage--);
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