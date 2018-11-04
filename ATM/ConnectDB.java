package com.company.ATM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConnectDB {

    static Connection conn = null;
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    //atmdbid.c17htg3hsf6i.eu-central-1.rds.amazonaws.com
    static final String DB_URL = "jdbc:mysql://localhost:3306";
    static final String USER = "admin";
    static final String PASS = "Abo_Salah93";
    static Statement stmt = null;

    public static Connection connect() {
        try {
            String sql;
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            return conn;
        } catch (Exception e) {
//            System.out.println("Connection Failed!");
            e.printStackTrace();
            return null;
        }
    }

}
