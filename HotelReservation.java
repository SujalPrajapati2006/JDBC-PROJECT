package JDBC_Project;

import java.sql.*;
import java.util.Scanner;

public class HotelReservation {

   private static final String url = "jdbc:mysql://localhost:3306/test";
   private static final String username = "root";
   private static final String password = "sujal@#12";

   public static void main(String[] args) throws SQLException, ClassNotFoundException {

      try {

         Class.forName("com.mysql.jdbc.Driver");

      } catch (ClassNotFoundException e) {
         System.out.println(e);
      }

      try {
         Connection con = DriverManager.getConnection(url, username, password);

         while (true) {
            System.out.println();
            System.out.println("HOTEL MANAGEMENT SYSTEM ");
            System.out.println("1. Reserve a room ");
            System.out.println("2. View Reservation ");
            System.out.println("3. Get Room Number ");
            System.out.println("4. Update Reservation ");
            System.out.println("5. Delete Reservation ");
            System.out.println("6. EXIT ");
            System.out.println("Select an option ");
            Scanner sc = new Scanner(System.in);
            int choice = sc.nextInt();

            switch (choice) {
               case 1:
                  reserveRoom(con, sc);
                  break;
               case 2:
                  viewReservation(con);
                  break;
               case 3:
                  getRoomNumber(con, sc);
                  break;
               case 4:
                  updateReservation(con, sc);
                  break;
               case 5:
                  deleteReservation(con, sc);
                  break;
               case 6:
                  exit();
                  sc.close();
                  return;
               default:
                  System.out.println("Invalid Choice ");
            }
         }
      } catch (SQLException e) {
         System.out.println(e);
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      }

   }

   public static void reserveRoom(Connection con, Scanner sc) throws SQLException {

      try {
         System.out.print("Enter guest name: ");
         String name = sc.next();
         sc.nextLine();

         System.out.print("Enter room number: ");
         int roomNumber = sc.nextInt();
         sc.nextLine();

         System.out.print("Enter contact number: ");
         String contactNumber = sc.next();

         String query = "insert into hotel (guest_name,room_number,contact_number) " +
               "values ('" + name + "', " + roomNumber + ", '" + contactNumber + "')";

         try (Statement st = con.createStatement()) {
            int affectedrows = st.executeUpdate(query);

            if (affectedrows > 0) {
               System.out.println("Reservation Successful ");
            } else {
               System.out.println("Reservation failed ");
            }
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public static void viewReservation(Connection con) throws SQLException {

      String query = "select * from hotel;";

      try (Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query)) {

         System.out.println("Current Reservation ");
         System.out.println("+----------------+-------------------+-------------+----------------+-----------------+");
         System.out.println("| Reservation ID | Guest Name        | Room Number | Contact Number | Reservation Date ");
         System.out.println("+----------------+-------------------+-------------+----------------+-----------------+");

         while (rs.next()) {
            int reservationId = rs.getInt("reservation_id");
            String guestName = rs.getString("guest_name");
            int roomNumber = rs.getInt("room_number");
            String contactNumber = rs.getString("contact_number");
            String reservationDate = rs.getString("reservation_date").toString();

            System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s  |\n",
                  reservationId, guestName, roomNumber, contactNumber, reservationDate);
         }
         System.out.println("+----------------+-------------------+-------------+----------------+-----------------+");

      }
   }

   public static void getRoomNumber(Connection con, Scanner sc) throws SQLException {

      System.out.print("Enter Reservation ID: ");
      int reservationId = sc.nextInt();
      sc.nextLine();

      System.out.print("Enter Guest Name: ");
      String guestName = sc.next();

      String query = "select room_number from hotel where reservation_id = " + reservationId +
            " and  guest_name = '" + guestName + "'";

      try (Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query)) {

         if (rs.next()) {

            int roomNumber = rs.getInt("room_number");
            System.out.println("Room number for reservation ID " + reservationId + " and Guest Name " + guestName +
                  " is: " + roomNumber);
         } else {
            System.out.println("Reservation not found for the given ID and guest name. ");
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public static void updateReservation(Connection con,Scanner sc){
       
      try{
         System.out.print("Enter Reservation id to update: ");
         int reservationId = sc.nextInt();
         sc.nextLine();

         if(!reservationExists(con,reservationId)){
             System.out.print("Reservation not found for the given ID ");
             return;
         }
         System.out.print("Enter guest name: ");
         String NewguestName = sc.next();
         sc.nextLine();

         System.out.print("Enter room number: ");
         int NewroomNumber = sc.nextInt();
         sc.nextLine();

         System.out.println("Enter Contact Number: ");
         String NewcontactNumber = sc.next();

         String query = "update hotel set guest_name = '"+ NewguestName + "', room_number = "+ NewroomNumber +
                         ", contact_number = '"+ NewcontactNumber + "' where reservation_id = "+reservationId;
                         
           try(Statement st = con.createStatement()){
            int affectedrows = st.executeUpdate(query);

            if(affectedrows>0){
                System.out.println("Reservation updated Successfully ");
            }
            else{
               System.out.println("Reservation  failed ");
            }

           }
           }catch(SQLException e){
            e.printStackTrace();
           }
   }
   public static void deleteReservation(Connection con,Scanner sc){
         
      System.out.print("Enter reservation Id to delete: ");
      int reservationId = sc.nextInt();

      if(!reservationExists(con,reservationId)){
           System.out.println("Reservation not found for the given ID. ");
           return;
      }
      String query = "delete from hotel where reservation_id = "+reservationId;

       try(Statement st = con.createStatement()){
           int affectedrows = st.executeUpdate(query);
                
           if(affectedrows>0){
            System.out.println("Reservation deleted Successfully ");
        }
        else{
           System.out.println("Reservation  failed ");
        }
           
       }catch(SQLException e){
          e.printStackTrace();;
       }
   }
   public static boolean reservationExists(Connection con,int reservationId){
         
      try{
         String query = "select reservation_id from hotel where reservation_id = "+ reservationId;

         try(Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)){
                
               return rs.next();
             }
      }catch(SQLException e){
          e.printStackTrace();
          return false;
      }
   }
   public static void exit()throws InterruptedException{
       System.out.println("Exiting System ");
       int i=5;
       while(i!=0){
           System.out.print(".");
           Thread.sleep(1000);
           i--;
       }
       System.out.println();
       System.out.println("Thank you for using Hotel Reservation System ");
   }
   }

