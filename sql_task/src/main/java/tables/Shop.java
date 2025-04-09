package tables;

import operations.DatBaseManager;
import strings.Operations;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Shop {

    public String storeName;
    public int storeId;

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreName() {
        return storeName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void startShopPagination(Shop shop, int limit, int totalPages, int startPage) throws SQLException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        DatBaseManager<Shop> shopGeneralItemManager = new DatBaseManager<>();
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("go next/previous or exit");
            String option = scan.next().toLowerCase();

            if (option.equals(Operations.EXIT)) break;

            switch (option) {
                case Operations.CONTINUE -> {
                    if (totalPages > startPage){
                        List<Shop> shops = shopGeneralItemManager.showPagination( shop, limit, ++startPage);
                        for (Shop shop1: shops) {
                            System.out.println("Shop Name is: "+ shop1.getStoreName()+ " Shop Id is : "+ shop1.getStoreId());
                        }
                    }  else {
                        System.out.println(Operations.UNAVAILABLE);
                    }
                }
                case Operations.PREVIOUS -> {
                    if (startPage <= totalPages && startPage > 1) {
                        List<Shop> shops = shopGeneralItemManager.showPagination( shop, limit, --startPage);
                        for (Shop shop1: shops) {
                            System.out.println("Shop Name is: "+ shop1.getStoreName()+ " Shop Id is : "+ shop1.getStoreId());
                        }
                    } else {
                        System.out.println(Operations.UNAVAILABLE);
                    }
                }
            }
        }
    }
}