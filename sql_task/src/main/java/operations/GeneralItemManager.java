package operations;

import strings.Operations;
import strings.Queries;
import strings.Tables;
import tables.Shop;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

        String insertQuery = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + values + ")";
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

    public List<T> showPagination(Connection connection, T object, int limit, int currentPage) throws SQLException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        int totalRecords = getTotalRecords(connection);

        int totalPages = (int) Math.ceil((double) totalRecords / limit);

        System.out.println("total pages: " + totalPages);
        System.out.println("you are currently on page " + currentPage + " out of " + totalPages);
        return selectTable(connection, object, limit, currentPage);
    }

    public void startPagination (Connection connection, T object, int limit, int startPage, int totalPages) throws SQLException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        while (true) {
            Scanner scan = new Scanner(System.in);

            System.out.println("go next/previous or exit");

            String option = scan.next().toLowerCase();

            if (option.equals(Operations.EXIT)) break;

            switch (option) {
                case Operations.CONTINUE -> {
                    if (totalPages > startPage){
                        showPagination(connection, object, limit, ++startPage);
                    }  else {
                        System.out.println(Operations.UNAVAILABLE);
                    }
                }
                case Operations.PREVIOUS -> {
                    if (startPage <= totalPages && startPage > 1) {
                        showPagination(connection, object, limit, --startPage);
                    } else {
                        System.out.println(Operations.UNAVAILABLE);
                    }
                }
            }
        }
    }


    public List<T> selectTable(Connection connection ,T obj, int limit, int pageNumber) throws SQLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        int offset = (pageNumber - 1) * limit;
        List<T> resultList = new ArrayList<>();
        Class<?> clazz = obj.getClass();

        String tableName = clazz.getSimpleName().toLowerCase();

        Field[] fields = clazz.getDeclaredFields();




        String sql = "SELECT * FROM " + tableName +
        " limit "+ limit +" offset "+ offset +" ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {


            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Object resultObj = clazz.getDeclaredConstructor().newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object fieldValue = resultSet.getObject(field.getName());
                    field.set(resultObj, fieldValue);
                }
                resultList.add((T) resultObj);
            }
        }

        return resultList;
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
