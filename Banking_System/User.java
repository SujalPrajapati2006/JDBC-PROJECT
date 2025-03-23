package Bank_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

    private Connection con;
    private Scanner sc;
    
    public User(Connection con,Scanner sc){
           this.con = con;
           this.sc = sc;
    }
    public void register(){
         sc.nextLine();
         System.out.print("Full name: ");
         String full_name = sc.nextLine();
         System.out.print("Email: ");
         String email = sc.nextLine();
         System.out.print("Password: ");
         String password = sc.nextLine();

         if(user_exists(email)){
              System.out.println("User already exists for this Email Address");
              return;
         }

         String register_query = "insert into user(full_name,email,password) values (?,?,?)";
         
         try{
            PreparedStatement preparedstatement = con.prepareStatement(register_query);
            preparedstatement.setString(1,full_name);
            preparedstatement.setString(2, email);
            preparedstatement.setString(3, password);

            int affectedrows = preparedstatement.executeUpdate();
            if(affectedrows>0){
                 System.out.println("Registration Successfully. ");
            }else{
                 System.out.println("Registration Failed. ");
            }
         }catch(SQLException e){
             e.printStackTrace();
         }
                   
    }
    public String login(){
          sc.nextLine();
          System.out.print("Email: ");
          String email = sc.nextLine();
          System.out.print("Password: ");
          String password = sc.nextLine();

          String login_query = "select * from user where email = ? and password = ? ";
          try{
            PreparedStatement preparedstatement = con.prepareStatement(login_query);
            preparedstatement.setString(1, email);
            preparedstatement.setString(2, password);
            
            ResultSet rs = preparedstatement.executeQuery();
              if(rs.next()){
                return email;
              }else{
                return null;
              }
          }catch(SQLException e){
              e.printStackTrace();
          }
          return null;
    }
    public boolean user_exists(String email){
          
        String query = "select * from user where email = ? ";

        try{
            PreparedStatement preparedstatement = con.prepareStatement(query);
            preparedstatement.setString(1, email);
            ResultSet rs = preparedstatement.executeQuery();
            if(rs.next()){
                 return true;
            }
            else{
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
