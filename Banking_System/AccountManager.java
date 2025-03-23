package Bank_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {

    private Connection con;
    private Scanner sc;

    public AccountManager(Connection con, Scanner sc) {

        this.con = con;
        this.sc = sc;
    }

    public void credit_money(long account_number) throws SQLException {
        sc.nextLine();
        System.out.print("Enter amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Security PIN: ");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            String query = "select * from accounts where account_number = ? and security_pin = ? ";
            if (account_number != 0) {

                PreparedStatement preparedstatement = con.prepareStatement(query);
                preparedstatement.setLong(1, account_number);
                preparedstatement.setString(2, security_pin);
                ResultSet rs = preparedstatement.executeQuery();

                if (rs.next()) {
                    String credit_query = "update accounts set balance = balance + ? where account_number = ? ";
                    PreparedStatement preparedstatement1 = con.prepareStatement(credit_query);
                    preparedstatement1.setDouble(1, amount);
                    preparedstatement1.setLong(2, account_number);

                    int affectedrows = preparedstatement1.executeUpdate();
                    if (affectedrows > 0) {
                        System.out.println("Rs." + amount + " Credited Successfully ");
                        con.commit();
                        con.setAutoCommit(true);
                    } else {
                        System.out.println("Transaction Failed ");
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Invalid PIN ");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void debit_money(long account_number) throws SQLException {

        sc.nextLine();
        System.out.print("Enter amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Security PIN: ");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            String debit_query = "select * from accounts where account_number = ? and security_pin = ? ";

            if (account_number != 0) {
                PreparedStatement preparedstatement = con.prepareStatement(debit_query);
                preparedstatement.setLong(1, account_number);
                preparedstatement.setString(2, security_pin);
                ResultSet rs = preparedstatement.executeQuery();

                if (rs.next()) {
                    double current_balance = rs.getDouble("balance");
                    if (amount <= current_balance) {
                        String debitt_query = "update accounts set balance = balance - ? where account_number = ?";
                        PreparedStatement preparedstatement1 = con.prepareStatement(debitt_query);
                        preparedstatement1.setDouble(1, amount);
                        preparedstatement1.setLong(2, account_number);

                        int affectedrows = preparedstatement1.executeUpdate();
                        if (affectedrows > 0) {
                            System.out.println("Rs." + amount + " debited Successfully ");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed ");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    } 
                } else {
                    System.out.println("Invalid PIN ");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }

    public void transfer_money(long sender_account_number) {
        sc.nextLine();
        System.out.print("Enter Receivir Account Number: ");
        long receiver_account_number = sc.nextLong();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();

        
        String query = "select balance from accounts where account_number =  ? and security_pin = ? ";
        try {
            con.setAutoCommit(false);
            if (sender_account_number != 0 && receiver_account_number != 0) {

                PreparedStatement preparedstatement = con.prepareStatement(query);
                preparedstatement.setLong(1, receiver_account_number);
                preparedstatement.setString(2, security_pin);
                ResultSet rs = preparedstatement.executeQuery();

                if (rs.next()) {
                    double current_balance = rs.getDouble("balance");
                    if (amount <= current_balance) {
                        String debit_query = "update  accounts set balance = balance - ? where account_number = ?";
                        String credit_query = "update accounts set balance = balance + ? where account_number = ?";
                        PreparedStatement creditPreparedStatement = con.prepareStatement(credit_query);
                        PreparedStatement debitPreparedStatement = con.prepareStatement(debit_query);
                        creditPreparedStatement.setDouble(1, amount);
                        creditPreparedStatement.setLong(2, receiver_account_number);
                        debitPreparedStatement.setDouble(1, amount);
                        debitPreparedStatement.setLong(2, sender_account_number);

                        int affectedrows1 = debitPreparedStatement.executeUpdate();
                        int affectedrows2 = creditPreparedStatement.executeUpdate();
                        if (affectedrows1 > 0 && affectedrows2 > 0) {
                            System.out.println("Transaction Successfully ");
                            System.out.println("Rs." + amount + " Transfered Successfully ");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed ");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Balance ");
                    }
                } else {
                    System.out.println("Invalid PIN ");
                }

            } else {
                System.out.println("Invalid account number ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void get_balance(long account_number) {
        sc.nextLine();
        System.out.print("Security PIN: ");
        String security_pin = sc.nextLine();

        String query = "select balance from accounts where account_number =  ? and security_pin = ? ";
        try {
            PreparedStatement preparedstatement = con.prepareStatement(query);
            preparedstatement.setLong(1, account_number);
            preparedstatement.setString(2, security_pin);
            ResultSet rs = preparedstatement.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.println("Balance: " + balance);
            } else {
                System.out.println("Invalid PIN ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
