package operations;

import strings.Queries;
import tables.Shop;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DatBaseManager<T> {

    final static String URL = "jdbc:mysql://localhost:3306/SHOP";
    final static String USER_NAME = "root";
    final static String PASSWORD = "1230459078150@khaled";
    static Connection connection;

    static {
        try {
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

// field is tables fieldName gets table columns

    public void insertIntoTable(T object) throws SQLException, IllegalAccessException {

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

    public void insertBy(String tableName) {
        Scanner scan = new Scanner(System.in);
        String ans = scan.next().toLowerCase();
        Shop shop = new Shop();
        if (ans.equals("id")) {
            System.out.println("Shop Id: ");
            int shopId = scan.nextInt();
            shop.setStoreId(shopId);
            shop.setStoreName("");
            System.out.println("You Have Inserted " + shopId + " As Id");
        } else if (ans.equals("shopname")) {
            System.out.println("Shop Name: ");
            String shopName = scan.next();
            shop.setStoreName(shopName);
            System.out.println("You Have Inserted " + shopName + " As Shop Name");
        } else {
            System.out.println("Shop Id: ");
            int shopId = scan.nextInt();
            System.out.println("Shop Name: ");
            String shopName = scan.next();
            shop.setStoreId(shopId);
            shop.setStoreName(shopName);
            System.out.println("You Have Inserted " + shopName + " As Shop Name And It's Id is " + shopId);
        }
    }

    public static int getTotalRecords() throws SQLException {
        String countQuery = Queries.countQuery;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(countQuery);

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    public List<T> showPagination(T object, int limit, int currentPage) throws SQLException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        int totalRecords = getTotalRecords();

        int totalPages = (int) Math.ceil((double) totalRecords / limit);
        List<T> resultList = new ArrayList<>();
        Class<?> clazz = object.getClass();
        String tableName = clazz.getSimpleName().toLowerCase();
        Field[] fields = clazz.getDeclaredFields();
        int offset = (currentPage - 1) * limit;

        System.out.println("you are currently on page " + currentPage + " out of " + totalPages);
        System.out.println("total pages: " + totalPages);
        String query = "select * from "+ tableName + " limit "+ limit +" offset "+ offset +" ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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

    public String search(String tableName, int limit, int currentPage) {
        Scanner scan = new Scanner(System.in);
        String column = scan.nextLine().toLowerCase();
        int offset = (currentPage - 1) * limit;
        if (!column.equals("all")) {
            System.out.println("enter the value");
            String value = scan.nextLine();
            return "SELECT * FROM " + tableName +
                    " where " + column + " = " + value +
                    " limit " + limit +" offset "+ offset + " ";
        } else {
            return "select * from "+ tableName + " limit "+ limit +" offset "+ offset +" ";
        }
    }

    public List<T> selectTable(T obj, int limit, int currentPage) throws SQLException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        List<T> resultList = new ArrayList<>();
        Class<?> clazz = obj.getClass();

        String tableName = clazz.getSimpleName().toLowerCase();

        Field[] fields = clazz.getDeclaredFields();

        System.out.print("you can select from ");

        for (Field field: fields) {
            System.out.print(field.getName()+" ");
        }
        System.out.println("Or All");
        String query = search(tableName, limit, currentPage);
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
}