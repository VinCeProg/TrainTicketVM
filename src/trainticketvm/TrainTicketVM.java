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
  private static boolean mainLoop = true;
  private static String formatAmt = "%.2f";

  // CHANGE THIS VALUE FOR TESTING
  private static final double BASE_PRICE = 15.00;
  private static final int TICKET_VALIDITY = 1;

  // MAIN
  public static void main(String[] args) {

    while (mainLoop) {
      landingPage();
    }
    System.out.println("Powering OFF!");
  }

  private static void landingPage() {
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

  private static void buyTicket() {
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
    DisplayInfo.displayStations(ticketType);
    while (departure == 0) {
      System.out.print("Select Departure Station : ");
      departure = validateStation();
    }

    DisplayInfo.displayStations(ticketType, departure);
    while (destination == 0) {
      System.out.print("Select Destination Station : ");
      destination = validateStation();
      if (destination == departure) {
        System.out.println("Invalid Input! Please enter a valid number!");
      }
    }

    // Calculate ticket amount
    double tixType = (ticketType.equalsIgnoreCase("LIMITED")) ? 1.75
            : (ticketType.equalsIgnoreCase("COMMUTERX")) ? 1.50 : 1.00;
    double ticketAmount = BASE_PRICE + Math.abs(destination - departure) * tixType;
    System.out.print("The Price for the ticket is ");
    System.out.printf(formatAmt, ticketAmount);
    System.out.println();

    // Payment
    Payment payment = new Payment();

    while (true) {
      System.out.print("Choose payment method (CASH, CARD, or MOBILE): ");
      paymentMethod = scanner.next().toUpperCase();

      switch (paymentMethod) {
        case "CASH":
          paymentSuccessful = payment.makeCashPayment(ticketAmount);
          break;
        case "CARD":
        case "MOBILE":
          paymentSuccessful = payment.makeCashlessPayment(ticketAmount, paymentMethod);
          break;
        default:
          System.out.println("Invalid Payment Method!");
          continue;
      }
      break;
    }

    // if payment is successful, ticket is generated and uploaded to database
    if (paymentSuccessful) {
      Ticket ticket = new Ticket(ticketType.toUpperCase(), issueDate, expiryDate, departure, destination, ticketAmount, paymentMethod);
    }

    System.out.println();
    System.out.println("Thank You & have a safe trip!");
  }

  public static void validateTicket() {

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
    try (Connection con = dbConnect.con;
            PreparedStatement prep = con.prepareStatement(query)) {
      prep.setInt(1, ticketNum);
      ResultSet result = prep.executeQuery();
      if (result.next()) {
        System.out.println();
        System.out.println("********** Ticket **********");
        System.out.println("Ticket ID No  :  " + result.getInt("ticketID"));
        System.out.println("Issue Date    :  " + result.getString("issueDate"));
        System.out.println("Expiry Date   :  " + result.getString("expiryDate"));
        System.out.println("****************************");

        // Check if the ticket is valid
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate expiryDate = LocalDate.parse(result.getString("expiryDate"), formatter);

        if (today.isBefore(expiryDate) || today.isEqual(expiryDate)) {
          System.out.println("Your ticket is valid!");
        } else {
          System.out.println("Your ticket is invalid. It has expired.");
        }
      } else {
        System.out.println("Ticket Invalid! No ticket/s found.");
      }
    } catch (Exception e) {
      System.out.println("Something went wrong with getting ticket information");
      e.printStackTrace();
    }
  }

// METHODS FOR buyTickets
  private static String selectTicketType() {
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
  }//selectTicketType

  private static int validateStation() {
    int departure = 0;
    if (scanner.hasNextInt()) {
      departure = scanner.nextInt();
      if (departure >= 1 && departure <= 41) {
        return departure;
      } else {
        System.out.println("Invalid Station!");
        return 0;
      }
    } else {
      System.out.println("Invalid Input! Please enter a valid number!");
      scanner.nextLine();
      return 0;
    }
  }
}
