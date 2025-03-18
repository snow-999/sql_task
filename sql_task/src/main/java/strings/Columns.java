package strings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Columns {
    final public static String ITEM_ID = "itemId";
    final public static String ITEM_NAME = "itemName";
    final public static String STORE_NAME = "storeName";
    final public static String STORE_ID = "storeId";

    public void selectByItemId(Connection connection, int limit, int offset) throws SQLException {
        Scanner scan = new Scanner(System.in);

    }
}
