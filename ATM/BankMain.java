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
    static Connection conn = null;
    public static Statement stmt = null;
    static double amount;
    static int threadNum;

    public static void main(String[] args) throws InterruptedException {
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
        Account acc[] = new Account[atm_num];
        for (int i = 0; i < atm_num; i++){
            threadNum = i;
            acc[i] = new Account(account_id, amount, threadNum);
            acc[i].run();
        }
    }

    public static boolean login(int account_id, int pin_code) {
        boolean is_logged = false;
        try {
            conn = ConnectDB.connect();
            String query;
            query = "use atmdb;";
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            query = "SELECT * FROM account WHERE account_id = " + account_id + " AND pinCode = " + pin_code + ";";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()){
                System.out.println("Welcome!");
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
    }

    public static void enterUserInfo() {
        System.out.println("PLease Login ");
        System.out.println("Enter your Accounr number:  ");
        account_id = Integer.parseInt(sc.next());
        System.out.println("Enter your pin Code:  ");
        pin_code = Integer.parseInt(sc.next());
    }
}
