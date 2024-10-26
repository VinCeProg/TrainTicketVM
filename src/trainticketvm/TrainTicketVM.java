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
    if(scanner.hasNextInt()){
      choice = scanner.nextInt();
    }else{
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
        System.out.println("Ticket Valid!\n\n");
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

    // Asks the user to choose ticket Type
    while (true) {
      System.out.print("Select Ticket Type (COMMUTER, COMMUTERX, LIMITED): ");
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
    cal.add(Calendar.DATE, TICKET_VALIDITY);
    Date expiryDate = cal.getTime();

    // Prompts the user to choose departure and destination
    while (true) {
      DisplayInfo.displayStations(ticketType);
      System.out.print("Select Departure Station : ");
      if (scanner.hasNextInt()) {
        departure = scanner.nextInt();
        if (departure >= 1 && departure <= 41) {
          break;
        } else {
          System.out.println("Invalid Departure Station!");
        }
      } else {
        System.out.println("Invalid Input! Please enter a valid number!");
        scanner.nextLine();
      }
    }

    while (true) {
      DisplayInfo.displayStations(ticketType, departure);
      System.out.print("Select Destination Station : ");
      if (scanner.hasNextInt()) {
        destination = scanner.nextInt();
        if (destination == departure || !(destination >= 1 || destination <= 41)) {
          System.out.println("Invalid Input! Please enter a valid number!");
        } else {
          break;
        }
      } else {
        System.out.println("Invalid Input! Please enter a valid number!");
        scanner.nextLine();
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
    boolean paymentSuccessful = false;
    Payment payment = new Payment();
    String paymentMethod = "";

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
}
