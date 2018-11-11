package com.company.ATM;

import java.sql.*;

import static com.company.ATM.BankMain.selection;

public class Account implements Runnable {
    ResultSet rs = null;
    static Connection conn = null;
    public static Statement stmt = null;
    static double balance;
    public static double newBalance;
    static int account_id;
    static double amount;
    static int threadNum;

    public Account(int account_id, double amount, int threadNum) {
        this.account_id = account_id;
        this.amount = amount;
        this.threadNum = threadNum;
        conn = ConnectDB.connect();
    }

    public void run(){
        create_table();
        System.out.println("==== Thread Number: "+ threadNum +" is Running ====");
        switch (selection){
            case 1:
                try {
                    System.out.println("Thread Number: " + threadNum + " || Your current balance is: " + displayBalance(account_id));
                    deposit(account_id, amount);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Transaction Failed!");
                }
            case 2:
                try {
                    System.out.println("Thread Number: " + threadNum + " || Your current balance is: " + displayBalance(account_id));
                    withdraw(account_id, amount);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Transaction Failed!");
                }
            case 3:
                try {
                    System.out.println("Thread Number: " + threadNum + " || Your current balance is: " + displayBalance(account_id));
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            case 4:
                try {
                    stmt.close();
                    System.out.println("Thank You For Banking With Us!");
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
            //System.out.println("Inserting values...");
            //sql = "INSERT INTO account VALUES(1111, 1111, 0), (2222, 2222, 0)";
            //stmt = conn.createStatement();
            //stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void deposit(int account_id, double deposit) throws SQLException {
        try {
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(false);
            String sql;
            sql = "use atmdb;";
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            balance = getBalance(account_id);
            newBalance = balance + deposit;
            sql = "UPDATE account SET balance = " + newBalance + " WHERE account_id = " + account_id + ";";
            PreparedStatement preStmt = conn.prepareStatement(sql);
            preStmt.execute();
            conn.commit();
            System.out.println("Thread Number: "+ threadNum + " || Transaction Succeeded! Your new balance is: " + getBalance(account_id));
            conn.close();
        }
        catch (SQLException e) {
            conn.rollback();
            conn.close();
            e.printStackTrace();
        }
    }

    public synchronized void withdraw(int account_id, double withdraw) throws SQLException{
        balance = getBalance(account_id);
        newBalance = balance - withdraw;
        if (newBalance < 0) {
            System.out.println("You Don't have enough money in your account!");
        } else {
            try {
                conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                conn.setAutoCommit(false);
                String sql;
                sql = "use atmdb;";
                stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                sql = "UPDATE account SET balance = " + newBalance + " WHERE account_id = " + account_id + ";";
                PreparedStatement preStmt = conn.prepareStatement(sql);
                preStmt.execute();
                conn.commit();
                System.out.println("Thread Number: "+ threadNum + " || Transaction Succeeded! Your new balance is: " + getBalance(account_id));
                conn.close();
            }
            catch (SQLException e) {
                conn.rollback();
                conn.close();
                e.printStackTrace();
            }
        }
    }

    public double getBalance(int account_id) {
        double current_balance = 0;
        try {
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(false);
            String sql = "SELECT balance FROM account WHERE account_id = " + account_id + " FOR UPDATE;";
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

    public double displayBalance(int account_id) {
        double current_balance = 0;
        try {
            String sql = "SELECT balance FROM account WHERE account_id = " + account_id + ";";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()){
                current_balance = rs.getDouble("balance");
            }
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return current_balance;
    }
}
