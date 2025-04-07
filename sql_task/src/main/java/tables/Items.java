package tables;

import operations.GeneralItemManager;
import strings.Operations;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Items {
    public String itemName;
    public int itemId;

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemId() {
        return itemId;
    }

    public void startItemsPagination(Connection connection, Items item, int limit, int totalPages, int startPage) throws SQLException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        GeneralItemManager<Items> shopGeneralItemManager = new GeneralItemManager<>();
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("go next/previous or exit");
            String option = scan.next().toLowerCase();

            if (option.equals(Operations.EXIT)) break;

            switch (option) {
                case Operations.CONTINUE -> {
                    if (totalPages > startPage){
                        List<Items> items = shopGeneralItemManager.showPagination(connection, item, limit, ++startPage);
                        for (Items itm: items) {
                            System.out.println("Items Name is: "+ itm.getItemName()+ " Items Id is : "+ itm.getItemId());
                        }
                    }  else {
                        System.out.println(Operations.UNAVAILABLE);
                    }
                }
                case Operations.PREVIOUS -> {
                    if (startPage <= totalPages && startPage > 1) {
                        List<Items> items = shopGeneralItemManager.showPagination(connection, item, limit, --startPage);
                        for (Items itm: items) {
                            System.out.println("Items Name is: "+ itm.getItemName()+ " Items Id is : "+ itm.getItemId());
                        }
                    } else {
                        System.out.println(Operations.UNAVAILABLE);
                    }
                }
            }
        }
    }
}
