package trainticketvm;

import java.util.Scanner;
import java.sql.*;

public class TrainTicketVM {

  private static Scanner scanner = new Scanner(System.in);
  private static SysConnectMySQL dbconnection = new SysConnectMySQL();
  private static boolean mainLoop = true;
  private static final int CURRENT_STATION = 11;

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
    selectOptionLP();
  }

  private static void selectOptionLP() {
    System.out.print("Choice : ");
    int choice = scanner.nextInt();

    switch (choice) {
      case 1: // Buy Ticket
        System.out.println("Ticket Sold!\n\n");
        break;
      case 2: // Check Ticket Validity
        System.out.println("Ticket Valid!");
        break;
      case 3: // exit NOTE: this will be transferred in Admin Access later
        mainLoop = false;
        break;
      case 4: // Admin Access to Program

        break;
      default:

        break;
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
      System.out.println("Oh No! Something went Wrong!");
      e.printStackTrace();
    }
  }
}
