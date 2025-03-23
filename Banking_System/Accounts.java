package Bank_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Accounts {

    private Connection con;
    private Scanner sc;

    public Accounts(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    public long open_account(String email) {
        if (!account_exists(email)) {

            String open_account_query = "insert into accounts(account_number,full_name,email,balance,security_pin) values (?,?,?,?,?)";
            sc.nextLine();
             System.out.print("Full name: ");
             String full_name = sc.nextLine();
             System.out.print("Initial amount: ");
             double balance = sc.nextDouble();
             sc.nextLine();
             System.out.print("Security PIN: ");
             String security_pin = sc.nextLine();

             try{
                long account_number = generateAccount_number();
                PreparedStatement preparedstatement = con.prepareStatement(open_account_query);
                preparedstatement.setLong(1, account_number);
                preparedstatement.setString(2,full_name);
                preparedstatement.setString(3, email);
                preparedstatement.setDouble(4, balance);
                preparedstatement.setString(5, security_pin);
                int affectedrows = preparedstatement.executeUpdate();
                if(affectedrows>0){
                     return account_number;
                }else{
                    throw new RuntimeException("Account Creation failed.");
                }
             }catch(SQLException e){
                 e.printStackTrace();
             }
        }
         throw new RuntimeException("Accouunt Already Exist ");
    }

    private long generateAccount_number() {

        String accountNumber_query = "select account_number from accounts  order by account_number DESC LIMIT 1";
        try {
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(accountNumber_query);
            if (rs.next()) {
                long last_account_number = rs.getLong("account_number");
                return last_account_number+1;
            }else{
                return 10000100;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 10000100;
    }

    public long getAccount_number(String email){
           String query = "select account_number from accounts where email = ? ";
           try{
             PreparedStatement preparedstatement = con.prepareStatement(query);
             preparedstatement.setString(1, email);
             ResultSet rs  = preparedstatement.executeQuery();

             if(rs.next()){
                 return rs.getLong("account_number");
             }
           }catch(SQLException e){
             e.printStackTrace();
           }

           throw new RuntimeException("Account number does not exist");
    }

    public boolean account_exists(String email) {

        String query = "select account_number from accounts where email = ? ";
        try {
            PreparedStatement preparedstatement = con.prepareStatement(query);
            preparedstatement.setString(1, email);
            ResultSet rs = preparedstatement.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
