package trainticketvm;

import java.util.Scanner;
import java.sql.*;
import java.util.Date;
import java.util.Calendar;

public class TrainTicketVM {

  private static Scanner scanner = new Scanner(System.in);
  private static SysConnectMySQL dbconnection = new SysConnectMySQL();
  private static boolean mainLoop = true;
  private static String formatAmt = "%.2f";

  // CHANGE THIS VALUE FOR TESTING
  private static final int CURRENT_STATION = 1;
  private static final String TRAIN_ROUTE = "NORTHBOUND"; // NORTHBOUND OR SOUTHBOUND
  private static final double BASE_PRICE = 15.00;

  public static void main(String[] args) {

    while (mainLoop) {
      displayCurrentStation();
      landingPage();
    }
    System.out.println("Powering OFF!");
  }

  private static void landingPage() {
    System.out.println("Train Ticket Vending Machine\n");
    System.out.println("Select Options:");
    System.out.println("1 - Buy Ticket/s");
    System.out.println("2 - Check Ticket Validity");
    System.out.print("Choice : ");
    int choice = scanner.nextInt();

    switch (choice) {
      case 0: // exit NOTE: this will be transferred in Admin Access later
        mainLoop = false;
        break;
      case 1: // Buy Ticket
        buyTicket();
        System.out.println("\n");
        break;
      case 2: // Check Ticket Validity
        System.out.println("Ticket Valid!\n\n");
        break;
      case 4: // Admin Access to Program

        break;
      default:
        System.out.println("Invalid input! Please select options between 1 and 2!");
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

  private static void displayStations(String ticketType) {
    dbconnection.connectToMachineDatabase();
    String selectedTrain = "";
    if (ticketType.equalsIgnoreCase("COMMUTER")) {
      selectedTrain = "onRoute_Commuter";
    } else if (ticketType.equalsIgnoreCase("COMMUTERX")) {
      selectedTrain = "onRoute_CommuterX";
    } else if (ticketType.equalsIgnoreCase("LIMITED")) {
      selectedTrain = "onRoute_Limited";
    }

    char route = (TRAIN_ROUTE.equalsIgnoreCase("NORTHBOUND")) ? '>' : '<';
    String query = "SELECT * FROM stations WHERE stationID " + route + " " + CURRENT_STATION + " AND " + selectedTrain + " = true";

    try (Connection con = dbconnection.con;
            PreparedStatement prep = con.prepareStatement(query)) {
      ResultSet result = prep.executeQuery();
      System.out.println("Station ID \t Station Name");
      while (result.next()) {
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
    String ticketType = "";
    while (true) {
      System.out.print("Enter Ticket Type (COMMUTER, COMMUTERX, LIMITED): ");
      ticketType = scanner.nextLine().toUpperCase();

      if (ticketType.equals("COMMUTER")
              || ticketType.equals("COMMUTERX")
              || ticketType.equals("LIMITED")) {
        break;
      } else {
        System.out.println("Invalid input! Please enter a valid ticket type.");
      }
    }

    // Generate issue and expiryDate
    Date issueDate = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(issueDate);
    cal.add(Calendar.DATE, 3); // sets ticket validity for 14 days
    Date expiryDate = cal.getTime();

    // Displays stations and prompts user
    displayStations(ticketType);
    displayCurrentStation();
    int destination = 0;
    while (true) {
      System.out.print("Enter Destination : ");
      if (scanner.hasNextInt()) {
        destination = scanner.nextInt();
        if ((TRAIN_ROUTE.equalsIgnoreCase("NORTHBOUND") && destination > CURRENT_STATION)
                || (TRAIN_ROUTE.equalsIgnoreCase("SOUTHBOUND") && destination < CURRENT_STATION && destination > 0)) {
          break;
        } else {
          System.out.println("Invalid Destination!");
        }
      } else {
        System.out.println("Invalid Input! Please enter a valid number.");
        scanner.next(); // Clear the invalid input
      }
    }

    // Calculate ticket amount
    double tixType = (ticketType.equalsIgnoreCase("LIMITED")) ? 1.75
            : (ticketType.equalsIgnoreCase("COMMUTERX")) ? 1.50 : 1.00;
    double ticketAmount = BASE_PRICE + Math.abs(destination - CURRENT_STATION) * tixType;
    System.out.print("The Price for the ticket is ");
    System.out.printf(formatAmt, ticketAmount);
    
    // Payment
    
    
    // Creates Ticket Object
    Ticket ticket = new Ticket(ticketType, issueDate, expiryDate, CURRENT_STATION, destination);
    ticket.insertTicket();
  }
}
