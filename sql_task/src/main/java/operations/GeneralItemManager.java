package operations;

import strings.Columns;
import strings.Queries;
import strings.Tables;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GeneralItemManager<T> {
    public void insertIntoTable(Connection connection, T object) throws SQLException, IllegalAccessException {

        Class<?> clazz = object.getClass();
        String tableName = clazz.getSimpleName().toLowerCase();
        Field[] fields = clazz.getDeclaredFields();

        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        List<Object> valueList = new ArrayList<>();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(object);

            if (value != null) {
                columns.append(field.getName()).append(", ");
                values.append("?, ");
                valueList.add(value);
            }
        }

        if (!columns.isEmpty()) columns.setLength(columns.length() - 2);
        if (!values.isEmpty()) values.setLength(values.length() - 2);

        String insertQuery = "INSERT INTO " + tableName + " (" + columns.toString() + ") VALUES (" + values.toString() + ")";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            for (int i = 0; i < valueList.size(); i++) {
                preparedStatement.setObject(i + 1, valueList.get(i));
            }
            preparedStatement.executeUpdate();
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
}
