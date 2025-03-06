package org.example;
import java.sql.*;
import java.util.Scanner;


public class Main {
    public static String insertIntoTable() {
        Scanner scan = new Scanner(System.in);
        System.out.println("enter table name");
        String tableName = scan.nextLine();
        StringBuilder query = new StringBuilder();
        switch (tableName) {
            case "stores" -> {
                System.out.println("enter store name");
                String name = scan.nextLine();

                System.out.println("enter store code");
                int code = scan.nextInt();
                query.append(String.format("INSERT INTO %s (storeCode, storeName) VALUES (%d, '%s')",tableName,code, name));
            }
            case "items" -> {
                System.out.println("enter item name");
                String name = scan.nextLine();

                System.out.println("enter item code");
                int code = scan.nextInt();
                query.append(String.format("INSERT INTO %s (itemCode, itemName) VALUES (%d, '%s')",tableName,code, name));
            }
            default -> {
                query.append("no table with that name");
            }
        }
        return query.toString();
    }

    public static String selectFromTable() {
        Scanner scan = new Scanner(System.in);
        System.out.println("enter table name");
        String tableName = scan.nextLine();
        StringBuilder query = new StringBuilder();
        switch (tableName) {
            case "stores" -> {
                System.out.println("chose to select: storeCode, storeName or * for all");
                String column = scan.nextLine();
                query.append(String.format("SELECT %s FROM %s",column, tableName));
            }
            case "items" -> {
                System.out.println("chose to select: itemCode, itemName or * for all");
                String column = scan.nextLine();
                query.append(String.format("SELECT %s FROM %s",column, tableName));
            }
        }
        return query.toString();
    }

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/shop";
        String username = "root";
        String password = "1230459078150@khaled";

        Scanner scan = new Scanner(System.in);
        System.out.println("chose an operation{insert, select}");
        String operation = scan.nextLine();

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            Statement statement = connection.createStatement();
            if (operation.equals("insert")) {
                statement.executeUpdate(insertIntoTable());
            }
            else {
                ResultSet resultSet = statement.executeQuery(selectFromTable());
                while (resultSet.next()) {
                    String storeName = resultSet.getString("storeName");
                    int storeId = resultSet.getInt("storeCode");
                    System.out.println(storeName + " " + storeId);
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