package trainticketvm;

import java.util.Scanner;
import java.sql.*;
import java.util.Date;
import java.util.Calendar;

public class TrainTicketVM {

  private static Scanner scanner = new Scanner(System.in);
  private static SysConnectMySQL dbconnection = new SysConnectMySQL();
  private static boolean mainLoop = true;
  private static final int CURRENT_STATION = 17;
  private static final String TRAIN_ROUTE = "SOUTHBOUND"; // NORTHBOUND OR SOUTHBOUND
  private static final double BASE_PRICE = 15.00;
  
  public static void main(String[] args) {

    while (mainLoop) {
      displayCurrentStation();
      landingPage();
    }
  }

  private static void landingPage() {
    System.out.println("Train Ticket Vending Machine\n");
    System.out.println("Select Options:");
    System.out.println("1 - Buy Ticket/s");
    System.out.println("2 - Check Ticket Validity");
    System.out.println("3 - Exit");
    System.out.print("Choice : ");
    int choice = scanner.nextInt();

    switch (choice) {
      case 1: // Buy Ticket
        buyTicket();
        System.out.println("Ticket Sold!\n\n");
        break;
      case 2: // Check Ticket Validity
        System.out.println("Ticket Valid!\n\n");
        break;
      case 3: // exit NOTE: this will be transferred in Admin Access later
        mainLoop = false;
        break;
      case 4: // Admin Access to Program

        break;
      default:
        System.out.println("Invalid input!");
        return;
    }//switch
  }

  private static void displayCurrentStation() {
    dbconnection.connectToMachineDatabase();
    String query = "SELECT * FROM stations WHERE stationID = " + CURRENT_STATION;

    try (Connection con = dbconnection.con;
            PreparedStatement prep = con.prepareStatement(query)) {
      ResultSet result = prep.executeQuery();
      if (result.next()) {
        String stationName = result.getString("stationName");
        System.out.println("Current Station: " + stationName);
      }

    } catch (Exception e) {
      System.out.println("Something went wrong with displaying Current Station");
      e.printStackTrace();
    }
  }

  private static void displayStations() {
    dbconnection.connectToMachineDatabase();
    char route = (TRAIN_ROUTE.equalsIgnoreCase("NORTHBOUND")) ? '>' : '<';
    String query = "SELECT * FROM stations WHERE stationID " + route + " " + CURRENT_STATION;

    try (Connection con = dbconnection.con;
            PreparedStatement prep = con.prepareStatement(query)) {
      ResultSet result = prep.executeQuery();
      System.out.println("Station ID \t Station Name");
      while(result.next()){
        int stationID = result.getInt("stationID");
        String stationName = result.getString("stationName");
        System.out.println(stationID + "\t\t " + stationName);
      }

    } catch (Exception e) {
      System.out.println("Something went wrong with displaying Stations");
      e.printStackTrace();
    }
  }

  private static void buyTicket() {
    scanner.nextLine(); // resets scanner from int to string
    
    System.out.print("Enter Ticket Type : ");
    String ticketType = scanner.nextLine();
    
    // Generate issue and expiryDate
    Date issueDate = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(issueDate);
    cal.add(Calendar.DATE, 14); // sets ticket validity for 14 days
    Date expiryDate = cal.getTime();
    
    displayStations();
    displayCurrentStation();
    System.out.print("Enter Destination : ");
    int destination = scanner.nextInt();
    
    // Creates Ticket Object
    Ticket ticket = new Ticket(ticketType, issueDate, expiryDate, CURRENT_STATION, destination);
    ticket.insertTicket();
    
    System.out.print("The Price for the ticket is ");
    System.out.printf("%.2f\n", BASE_PRICE + Math.abs(destination - CURRENT_STATION));
  }
}
