package trainticketvm;

import java.util.Scanner;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Calendar;

public class TrainTicketVM {

  private static Scanner scanner = new Scanner(System.in);
  private static SysConnectMySQL dbConnect = new SysConnectMySQL();
  private static DisplayInfo disp = new DisplayInfo();
  private static boolean mainLoop = true;
  private static String formatAmt = "%.2f";

  // CHANGE THIS VALUE FOR TESTING
  private static final double BASE_PRICE = 15.00;
  private static final int TICKET_VALIDITY = 1;

  private void landingPage() {
    int choice = -1;

    System.out.println("Train Ticket Vending Machine\n");
    System.out.println("Select Options:");
    System.out.println("1 - Buy Ticket/s");
    System.out.println("2 - Check Ticket Validity");
    System.out.print("Choice : ");
    if (scanner.hasNextInt()) {
      choice = scanner.nextInt();
    } else {
      System.out.println("Please enter a valid Input!");
      scanner.nextLine();
    }

    switch (choice) {
      case 0: // exit NOTE: this will be transferred in Admin Access later
        mainLoop = false;
        break;
      case 1: // Buy Ticket
        buyTicket();
        System.out.println("\n");
        break;
      case 2: // Check Ticket Validity
        validateTicket();
        break;
      default:
        System.out.println("Invalid input! Please select options between 1 and 2!");
        return;
    }//switch
  }

  private void buyTicket() {
    Payment payment = new Payment();
    scanner.nextLine(); // resets scanner from int to string
    String ticketType = "";
    int departure = 0;
    int destination = 0;
    boolean paymentSuccessful = false;
    String paymentMethod = "";

    // Asks the user to choose ticket Type
    ticketType = selectTicketType();

    // Generate issue and expiryDate
    Date issueDate = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(issueDate);
    cal.add(Calendar.DATE, TICKET_VALIDITY);
    Date expiryDate = cal.getTime();

    // Prompts the user to choose departure and destination
    disp.displayStations(ticketType);
    while (departure == 0) {
      System.out.print("Enter Departure Station : ");
      departure = validateStation(ticketType);
    }
    System.out.println("You have selected " + disp.displaySelectedStation(departure) + " Station.\n");

    disp.displayStations(ticketType, departure);
    while (destination == 0) {
      System.out.print("Enter Destination Station : ");
      destination = validateStation(ticketType);
      if (destination == departure) {
        System.out.println("Invalid Input! Please enter a valid number!");
        destination = 0;
      }
    }
    System.out.println("You have selected " + disp.displaySelectedStation(destination) + " Station\n");

    // Calculate ticket amount
    double tixType = (ticketType.equalsIgnoreCase("LIMITED")) ? 1.75
            : (ticketType.equalsIgnoreCase("COMMUTERX")) ? 1.50 : 1.00;
    double ticketAmount = BASE_PRICE + Math.abs(destination - departure) * tixType;

    System.out.println(disp.displaySelectedStation(departure) + " Station to " + disp.displaySelectedStation(destination) + " Station");
    System.out.print("The Price for the ticket is P");
    System.out.printf(formatAmt, ticketAmount);
    System.out.println();

    // Get number of tickets
    int numOfTickets = getNumberOfTickets();
    double totalAmount = ticketAmount * numOfTickets;
    System.out.print("Total Price for " + numOfTickets + " ticket(s) is P");
    System.out.printf(formatAmt, totalAmount);
    System.out.println();

    if (confirmTransaction()) {
      if (payment.paymentMethod(totalAmount)) {
        for (int i = 0; i < numOfTickets; i++) {
          Ticket ticket = new Ticket(ticketType.toUpperCase(), issueDate, expiryDate, departure, destination, ticketAmount, paymentMethod);
        }
        System.out.println("\nThank You & have a safe trip!");
      }
    }

  }

  private void validateTicket() {
    int ticketNum = 0;
    while (true) {
      System.out.print("Enter Ticket Number : ");
      if (scanner.hasNextInt()) {
        ticketNum = scanner.nextInt();
        if (ticketNum <= 12340000) {
          System.out.println("Invalid Ticket Number! Please enter your ticket number correctly!");
        } else {
          break;
        }
      } else {
        System.out.println("Invalid Ticket Number! Please enter a valid ticket number!");
        scanner.nextLine();
      }
    }
    dbConnect.connectToMachineDatabase();
    String query = "SELECT * FROM tickets WHERE ticketID = ?;";
    Connection con = null;
    PreparedStatement prep = null;
    ResultSet result = null;
    try {
      con = dbConnect.con;
      prep = con.prepareStatement(query);
      prep.setInt(1, ticketNum);
      result = prep.executeQuery();
      if (result.next()) {
        System.out.println();
        System.out.println("********** Ticket **********");
        System.out.println("Ticket ID No  :  " + result.getInt("ticketID"));
        System.out.println("Issue Date    :  " + result.getString("issueDate"));
        System.out.println("Expiry Date   :  " + result.getString("expiryDate"));
        System.out.println("****************************");
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate expiryDate = LocalDate.parse(result.getString("expiryDate"), formatter);
        if (today.isBefore(expiryDate) || today.isEqual(expiryDate)) {
          System.out.println("Your ticket is valid!");
        } else {
          System.out.println("Your ticket is expired.");
        }
      } else {
        System.out.println("Ticket Invalid! No ticket/s found.\n\n");
      }
    } catch (Exception e) {
      System.out.println("Something went wrong with getting ticket information");
      e.printStackTrace();
    } finally {
      dbConnect.closeResources(con, prep, result);
    }
  }

  private String selectTicketType() {
    while (true) {
      System.out.print("Select Ticket Type (COMMUTER, COMMUTERX, LIMITED): ");
      String ticketType = scanner.nextLine().toUpperCase();

      if (ticketType.equals("COMMUTER")
              || ticketType.equals("COMMUTERX")
              || ticketType.equals("LIMITED")) {
        return ticketType;
      } else {
        System.out.println("Invalid input! Please enter a valid ticket type.");
      }// if else
    }// while
  }

  private int validateStation(String ticketType) {
    if (!scanner.hasNextInt()) {
      System.out.println("Invalid input! Please enter a number!");
      scanner.nextLine(); // clear the invalid input
      return 0;
    }

    int station = scanner.nextInt();
    dbConnect.connectToMachineDatabase();
    String selectedTrain = disp.selectedTrainQuery(ticketType);
    String query = "SELECT * FROM stations WHERE stationID = ?";

    try (Connection con = dbConnect.con; PreparedStatement prep = con.prepareStatement(query)) {
      prep.setInt(1, station);
      ResultSet result = prep.executeQuery();
      boolean hasResult = result.next();
      if (hasResult && result.getBoolean(selectedTrain)) {
        return station;
      } else {
        System.out.println(hasResult ? "Station not available for " + ticketType : "No Station Found!");
      }
    } catch (Exception e) {
      System.out.println("Something went wrong with validating station.");
      e.printStackTrace();
    }
    return 0;
  }

  private boolean confirmTransaction() {
    scanner.nextLine();
    System.out.print("Confirm Transaction? (Y/N): ");
    char choice = scanner.next().toUpperCase().charAt(0);
    while (choice != 'Y' && choice != 'N') {
      System.out.println("Invalid input! Please enter a valid input!");
      choice = scanner.next().toUpperCase().charAt(0);
    }
    if (choice == 'Y') {
      return true;
    } else {
      System.out.println("Transaction Cancelled!");
      return false;
    }
  }

  private int getNumberOfTickets() {
    System.out.print("Enter number of tickets: ");
    int numOfTickets = 1;
    if (scanner.hasNextInt()) {
      numOfTickets = scanner.nextInt();
    } else {
      System.out.println("Invalid input! Defaulting to 1 ticket.");
      scanner.nextLine(); // clear invalid input
    }
    return numOfTickets;
  }

  public static void main(String[] args) {
    TrainTicketVM vendingMachine = new TrainTicketVM();
    while (mainLoop) {
      vendingMachine.landingPage();
    }
    System.out.println("Powering OFF!");
  }
}
