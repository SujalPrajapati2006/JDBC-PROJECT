package Bank_System;

import java.sql.*;
import java.util.Scanner;

public class Banking_System {

    private static final String url = "jdbc:mysql://localhost:3306/Bank";
    private static final String username = "root";
    private static final String password = "sujal@#12";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            Connection con = DriverManager.getConnection(url, username, password);

            Scanner sc = new Scanner(System.in);
            User user = new User(con, sc);
            Accounts accounts = new Accounts(con, sc);
            AccountManager accountmanager = new AccountManager(con, sc);

            String email;
            long account_number;

            while (true) {
                System.out.println("*** WELCOME TO BANKING SYSTEM *** ");
                System.out.println();
                System.out.println("1. Register ");
                System.out.println("2. Login ");
                System.out.println("3. Exit ");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                switch (choice) {

                    case 1:
                        user.register();
                        break;

                    case 2:
                        email = user.login();
                        if (email != null) {
                            System.out.println();
                            System.out.println("User logged in ");
                            if (!accounts.account_exists(email)) {
                                System.out.println();
                                System.out.println("1. Opena new Bank Account ");
                                System.out.println("2. Exit ");
                                if (sc.nextInt() == 1) {
                                    account_number = accounts.open_account(email);
                                    System.out.println("Account Created Successfully ");
                                    System.out.println("Your Account number is: " + account_number);
                                } else {
                                    break;
                                }

                            }
                            account_number = accounts.getAccount_number(email);
                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1. Debit money ");
                                System.out.println("2. Credit money ");
                                System.out.println("3. Transfer money ");
                                System.out.println("4. Check Balance ");
                                System.out.println("5. Log Out ");
                                System.out.print("Enter your choice: ");
                                choice2 = sc.nextInt();

                                switch (choice2) {
                                    case 1:
                                        accountmanager.debit_money(account_number);
                                        break;

                                    case 2:
                                        accountmanager.credit_money(account_number);
                                        break;

                                    case 3:
                                        accountmanager.transfer_money(account_number);
                                        break;

                                    case 4:
                                        accountmanager.get_balance(account_number);
                                        break;

                                    case 5:
                                        break;

                                    default:
                                        System.out.println("Enter valid Choice ");
                                        break;
                                }
                            }
                        }
                        else{
                            System.out.println("Incorrect Email or Password ");
                        }

                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM ");
                        System.out.println("Exiting System ");
                        return;

                    default:
                        System.out.println("Enter valid Choice ");
                        break;
                }
            }

        } catch (SQLException e) {
            System.out.println(e);
        }

    }
}
