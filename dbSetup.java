package com.company;

import java.sql.*;
import java.util.Scanner;

public class dbSetup {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://atmdbid.c17htg3hsf6i.eu-central-1.rds.amazonaws.com:3306";
    static final String USER = "atm_db_username";
    static final String PASS = "atmpassword";
    static double oldBalance, newBalance, deposit, withdraw;
    static String accId;
    static boolean isLogged = false;
    static Connection conn = null;
    static Statement stmt = null;
    static Scanner sc = new Scanner(System.in); // Creates the sc object to read user input


    public static String login(String account_id, String pinCode) throws SQLException{
        String msg = "";
        String query = "SELECT * FROM account WHERE account_id = " + account_id + " AND pinCode = " + pinCode + ";";
        stmt = conn.createStatement();
        // execute the query, and get a java resultset
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()) {
            accId = rs.getString("account_id");
            String pin = rs.getString("pinCode");
            oldBalance = rs.getDouble("balance");
            if(accId.equals(account_id) && pin.equals(pinCode)) {
                isLogged = true;
                msg = "Login Successfully!, and your balance is " + oldBalance + " $";
                System.out.println(isLogged);
            } else {
                isLogged = false;
                msg = "Login Failed!";
            }
        }
        return msg;
    }

    public static void openConn() throws SQLException {
        System.out.println("Load MySQL JDBC driver");
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
            return;
        }

        System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);

        System.out.println("Creating statement...");
        stmt = conn.createStatement();
        // use database
        String sql_stmt;
        System.out.println("Use ATM Database...");
        sql_stmt = 	"use atmdb;";
        stmt.executeUpdate(sql_stmt);
    }

    public static void closeConn() throws SQLException {
        System.out.println("Closing connection...");
        stmt.close();
    }

    public static void displayMenu() throws SQLException {
        System.out.println("\nATM Menu: \n \n"
                + "1. Deposit Money \n"
                + "2. Withdraw Money \n"
                + "3. End Session\n \n"
                + "Enter selection: ");
        int selection = sc.nextInt(); // assign the user's input to the selection variable
        switch (selection){
            case 1:
                deposit(accId);
                displayMenu();
            case 2:
                withdraw(accId);
                displayMenu();
            case 3:
                System.exit(0);
                break;
        }
    }

    public static void createTable() {
//        String sql;
//        System.out.println("Creating table...");
//        sql = 	"CREATE TABLE IF NOT EXISTS account (account_id INT, pinCode INT, balance DECIMAL, UNIQUE (account_id));";
//        stmt.executeUpdate(sql);
//        System.out.println("Inserting values...");
//        sql = "INSERT INTO account VALUES(1111, 1111, 0), (2222, 2222, 0)";
//        stmt.executeUpdate(sql);

    }

    public static void deposit(String accId) throws SQLException {
        System.out.println("How much would you like to deposit?");
        deposit = sc.nextDouble();
        newBalance = oldBalance + deposit;

        String sql = "UPDATE account SET balance = " + newBalance + " WHERE account_id = " + accId + ";";
        stmt.executeUpdate(sql);

        System.out.println("Your balance now is: " + newBalance);// deposit money into balance
    }

    public static void withdraw(String accId) throws SQLException {
        System.out.println("How much would you like to withdraw?");
        withdraw = sc.nextDouble();
        if (withdraw > oldBalance) {
            System.out.println("You can NOT!!");
        } else {
            newBalance = oldBalance - withdraw;
            String sql = "UPDATE account SET balance = " + newBalance + " WHERE account_id = " + accId + ";";
            stmt.executeUpdate(sql);
            System.out.println("Your balance now is: " + newBalance);// withdraw money into balance
        }
    }

    public static void enterUserInfo()  throws SQLException{
        System.out.println("PLease Login ");
        System.out.println("Enter your Accounr number:  ");
        accId = sc.next();
        System.out.println("Enter your pin Code:  ");
        String pin = sc.next();
        String login_msg = login(accId, pin);
        System.out.println(login_msg);
    }

    public static void main(String[] args) throws SQLException {
        openConn();
        enterUserInfo();
        while (!isLogged) {
            System.out.println("Please try again ..");
            enterUserInfo();
        }
        displayMenu();
    }
}