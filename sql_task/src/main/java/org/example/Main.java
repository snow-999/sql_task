package org.example;
import operations.GeneralItemManager;
import strings.Columns;
import strings.Operations;

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
//                    case Operations.INSERT -> GeneralItemManager.insertIntoTable(connection);
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