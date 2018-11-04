package com.company.ATM;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class BankMain {
    public static int account_id;
    static int pin_code;
    static ResultSet rs = null;
    static Scanner sc = new Scanner(System.in);
    public static int selection;
    static boolean is_logged = false;
//    public static boolean is_transaction = false;
    static Connection conn = null;
    public static Statement stmt = null;
    static double amount;

    public static void main(String[] args) {
        System.out.println("How many ATMs would you like to run?");
        int atm_num = sc.nextInt();
        enterUserInfo();
        displayMenu();
        is_logged = login(account_id, pin_code);
        while (!is_logged) {
            System.out.println("Please try again ..");
            enterUserInfo();
            displayMenu();
            is_logged = login(account_id, pin_code);
        }
        System.out.println("atm_num >> " + atm_num);
        Account acc[] = new Account[atm_num];
        for (int i = 0; i < atm_num; i++){
            System.out.println("i >> " + i);
            acc[i] = new Account(account_id, amount);
            acc[i].start();
        }
//        acc.create_table();
//        enterUserInfo();
//        is_logged = acc.login(account_id, pin_code);
//        while (!is_logged) {
//            System.out.println("Please try again ..");
//            enterUserInfo();
//            is_logged = acc.login(account_id, pin_code);
//        }
//        displayMenu();
//        while (is_transaction) {
//            switch (selection){
//                case 1:
//                    try {
//                        System.out.println("How much would you like to deposit?");
//                        double deposit = sc.nextDouble();
//                        System.out.println("==== Start Threading!!! ====");
//                        acc.start();
//                        acc.deposit(account_id, deposit);
//                        acc2.start();
//                        acc2.deposit(account_id, deposit);
//                        displayMenu();
//                        break;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        System.out.println("Transaction Failed!");
//                    }
//                case 2:
//                    try {
//                        System.out.println("How much would you like to withdraw?");
//                        double withdraw = sc.nextDouble();
//                        System.out.println("==== Start Threading!!! ====");
//                        acc.withdraw(account_id, withdraw);
//                        acc2.withdraw(account_id, withdraw);
//                        displayMenu();
//                        break;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        System.out.println("Transaction Failed!");
//                    }
//                case 3:
//                    try {
//                        System.out.println("Closing connection...");
//                        acc.stmt.close();
//                        System.out.println("Thanks for using our Bank! Good Bye");
//                        System.exit(0);
//                        break;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//            }
//        }
    }

    public static boolean login(int account_id, int pin_code) {
        boolean is_logged = false;
        try {
            conn = ConnectDB.connect();
            String query;
            query = 	"use atmdb;";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            query = "SELECT * FROM account WHERE account_id = " + account_id + " AND pinCode = " + pin_code + ";";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()){
                System.out.println("Welcome!");
//                System.out.println("Your current balance is: " + getBalance(account_id));
                is_logged = true;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return is_logged;
    }

    public static void displayMenu() {
        System.out.println("\nATM Menu: \n \n"
                + "1. Deposit \n"
                + "2. Withdraw \n"
                + "3. End Session\n \n"
                + "Enter selection: ");
        selection = sc.nextInt(); // assign the user's input to the selection variable
        if (selection == 1) {
            System.out.println("How much would you like to deposit?");
            amount = sc.nextDouble();
        } else if (selection == 2) {
            System.out.println("How much would you like to withdraw?");
            amount = sc.nextDouble();
        }
//        if (selection == 1 || selection == 2) {
//            is_transaction = true;
//        } else {
//            is_transaction = false;
//        }
    }

    public static void enterUserInfo() {
        System.out.println("PLease Login ");
        System.out.println("Enter your Accounr number:  ");
        account_id = Integer.parseInt(sc.next());
        System.out.println("Enter your pin Code:  ");
        pin_code = Integer.parseInt(sc.next());
    }
}
