package com.company.ATM;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import static com.company.ATM.BankMain.selection;

public class Account extends Thread{
    ResultSet rs = null;
    static Connection conn = null;
    public static Statement stmt = null;
    static double balance;
    public static double newBalance;
    static Scanner sc = new Scanner(System.in);
    static int pin_code;
    static int account_id;
    static double amount;
    static boolean is_logged = false;
//    static int selection;
//    static boolean is_transaction = false;


    public Account(int account_id, double amount) {
        this.account_id = account_id;
        this.amount = amount;
        conn = ConnectDB.connect();
    }

    public void run(){
        create_table();
        System.out.println("Your current balance is: " + getBalance(account_id));
//        enterUserInfo();
//        is_logged = login(account_id, pin_code);
//        while (!is_logged) {
//            System.out.println("Please try again ..");
//            enterUserInfo();
//            is_logged = login(account_id, pin_code);
//        }
//        displayMenu();
//        while (is_transaction) {
            switch (selection){
                case 1:
                    try {
//                        System.out.println("How much would you like to deposit?");
//                        double deposit = sc.nextDouble();
                        System.out.println("==== Start Threading!!! ====");
                        deposit(account_id, amount);
//                        displayMenu();
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Transaction Failed!");
                    }
                case 2:
                    try {
//                        System.out.println("How much would you like to withdraw?");
//                        double withdraw = sc.nextDouble();
                        System.out.println("==== Start Threading!!! ====");
                        withdraw(account_id, amount);
//                        displayMenu();
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Transaction Failed!");
                    }
                case 3:
                    try {
                        System.out.println("Closing connection...");
                        stmt.close();
                        System.out.println("Thanks for using our Bank! Good Bye");
                        System.exit(0);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
//        }
    }


    public static void enterUserInfo() {
        System.out.println("PLease Login ");
        System.out.println("Enter your Accounr number:  ");
        account_id = Integer.parseInt(sc.next());
        System.out.println("Enter your pin Code:  ");
        pin_code = Integer.parseInt(sc.next());
    }

//    public static void displayMenu() {
//        System.out.println("\nATM Menu: \n \n"
//                + "1. Deposit \n"
//                + "2. Withdraw \n"
//                + "3. End Session\n \n"
//                + "Enter selection: ");
//        selection = sc.nextInt(); // assign the user's input to the selection variable
//        if (selection == 1 || selection == 2) {
//            is_transaction = true;
//        } else {
//            is_transaction = false;
//        }
//    }

    public void create_table(){
        try {
            String sql;
            sql = 	"use atmdb;";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("Creating table...");
            sql = 	"CREATE TABLE IF NOT EXISTS account (account_id INT, pinCode INT, balance DECIMAL, UNIQUE (account_id));";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
//            System.out.println("Inserting values...");
//            sql = "INSERT INTO account VALUES(1111, 1111, 0), (2222, 2222, 0)";
//            stmt = conn.createStatement();
//            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean login(int account_id, int pin_code) {
        boolean is_logged = false;
        try {
            String query = "SELECT * FROM account WHERE account_id = " + account_id + " AND pinCode = " + pin_code + ";";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()){
                System.out.println("Welcome!");
                System.out.println("Your current balance is: " + getBalance(account_id));
                is_logged = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return is_logged;
    }

    public synchronized void deposit(int account_id, double deposit){
        try {
            balance = getBalance(account_id);
            newBalance = balance + deposit;
            String sql = "UPDATE account SET balance = " + newBalance + " WHERE account_id = " + account_id + ";";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("Transaction Succeeded! Your new balance is: " + getBalance(account_id));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void withdraw(int account_id, double withdraw){
        balance = getBalance(account_id);
        newBalance = balance - withdraw;
        if (newBalance < 0) {
            System.out.println("You Don't have enough money in your account!");
        } else {
            try {
                String sql = "UPDATE account SET balance = " + newBalance + " WHERE account_id = " + account_id + ";";
                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                System.out.println("Transaction Succeeded! Your new balance is: " + getBalance(account_id));
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public double getBalance(int account_id) {
        double current_balance = 0;
        try {
            String sql = "SELECT balance FROM account WHERE account_id = " + account_id + ";";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                current_balance = rs.getDouble("balance");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return current_balance;
    }
}
