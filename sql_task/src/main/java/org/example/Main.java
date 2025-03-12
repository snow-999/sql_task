package org.example;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void insertIntoTable(Connection connection) throws SQLException {
        Scanner scan = new Scanner(System.in);

        System.out.println("Tables: stores, items, stores_items");
        System.out.println("enter table name");
        String tableName = scan.nextLine();

        switch (tableName) {
            case "stores" -> {
                System.out.println("enter store name");
                String storeName = scan.next();
                String query = "insert into stores (storeName) values (?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                preparedStatement.setString(1,storeName);
                preparedStatement.executeUpdate();
            }
            case "items" -> {
                System.out.println("enter item name");
                String itemName = scan.next();

                String query = "insert into items (itemName) values (?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                preparedStatement.setString(1, itemName);
                preparedStatement.executeUpdate();
            }
            case "stores_items" -> {
                System.out.println("enter item code");
                int itemID = scan.nextInt();

                System.out.println("enter store code");
                int storeID = scan.nextInt();

                String query = "insert into stores_items (storeID, itemId) values (?,?)";

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1,storeID);
                preparedStatement.setInt(2,itemID);
                preparedStatement.executeUpdate();
            }
            default -> System.out.println("no table with that name");
        }
    }

    public static void selectFromTable(Connection connection) throws SQLException {
        String query = "select stores.storeName, items.itemName from stores_items " +
                            " join stores on stores_items.storeId = stores.storeId" +
                            " join items on stores_items.itemId = items.itemId";
//                            "where stores_items.storeId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
//        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String storeNme = resultSet.getString("storeName");
            String itemNme =  resultSet.getString("itemName");
            System.out.println(storeNme +" "+ itemNme);
        }
    }

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/shop";
        String username = "root";
        String password = "1230459078150@khaled";

        while (true) {
            Scanner scan = new Scanner(System.in);
            System.out.println("chose an operation insert, select or exit if you finished");
            String operation = scan.nextLine();

            if (operation.equals("exit")) break;

            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();

                switch (operation) {
                    case "insert" -> insertIntoTable(connection);
                    case "select" -> selectFromTable(connection);
                    default -> System.out.println("no operation selected");
                }
                statement.close();
                connection.close();


            } catch (SQLException e) {
                System.err.println("SQL Error: "
                                   + e.getMessage());
            }
        }
        System.out.println("loop out");

    }
}