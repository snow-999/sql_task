package org.example;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static String insertIntoTable(String tableName) {
        Scanner scan = new Scanner(System.in);
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

                System.out.println("enter item StoreCode");
                int StoreCode = scan.nextInt();
                query.append(String.format("INSERT INTO %s (itemCode, itemName, storeCode) VALUES (%d, '%s', %d)",tableName,code, name, StoreCode));
            }
            default -> {
                query.append("no table with that name");
            }
        }
        return query.toString();
    }

    public static String selectFromTable(String tableName) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder query = new StringBuilder();
        switch (tableName) {
            case "items" ->{
                System.out.println("chose item code");
                int code = scanner.nextInt();
                query.append(String.format("select itemName from items join stores on stores.StoreCode = items.StoreCode where %s.itemCode = %d", tableName, code));
            }
            case "stores" -> {
                System.out.println("chose store code");
                int code = scanner.nextInt();
                query.append(String.format("select itemName from items join stores on stores.StoreCode = items.StoreCode where %s.storeCode = %d", tableName, code));
            }
            default -> {
                System.out.println("no table selected");
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

        System.out.println("enter table name");
        String tableName = scan.nextLine();

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement statement = connection.createStatement();

            switch (operation) {
                case "insert" -> {
                    statement.executeUpdate(insertIntoTable(tableName));
                }
                case "select" -> {
                    ResultSet resultSet = statement.executeQuery(selectFromTable(tableName));
                    while (resultSet.next()) {
                        String itemName = resultSet.getString("itemName");
                        System.out.println(itemName);
                    }
                }
                default -> {
                    System.out.println("no operation selected");
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