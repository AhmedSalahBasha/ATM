package com.company.ATM;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.company.ATM.BankMain.selection;

public class Account extends Thread{
    ResultSet rs = null;
    static Connection conn = null;
    public static Statement stmt = null;
    static double balance;
    public static double newBalance;
    static int account_id;
    static double amount;

    public Account(int account_id, double amount) {
        this.account_id = account_id;
        this.amount = amount;
        conn = ConnectDB.connect();
    }

    public void run(){
        create_table();
        System.out.println("==== Start Threading!!! ====");
        System.out.println("Your current balance is: " + getBalance(account_id));
        switch (selection){
            case 1:
                try {
                    deposit(account_id, amount);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Transaction Failed!");
                }
            case 2:
                try {
                    withdraw(account_id, amount);
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
    }

    public void create_table(){
        try {
            String sql;
            sql = 	"use atmdb;";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
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
