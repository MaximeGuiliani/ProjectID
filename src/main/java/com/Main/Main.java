package com.Main;

public class Main {
    public static void main(String[] args) {
        WrapperCSV wcsv = new WrapperCSV("dbName","tableName","/Users/max/Desktop/ID/src/main/java/com/WrapperCSV/marseille-stations-taxis-2017.csv", ',',false );
        System.out.println( wcsv.convertToSQL());
    }


    
}
