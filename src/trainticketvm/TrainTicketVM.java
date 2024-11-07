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
    System.out.println("2 - Check Ticket Validity / Extend Validity");
    System.out.print("Choice : ");
    if (scanner.hasNextInt()) {
      choice = scanner.nextInt();
    } else {
      System.out.println("Please enter a valid Input!");
      scanner.nextLine();
    }

    switch (choice) {
      case 0: // Admin Controls 
        logInAdmin();
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
    scanner.nextLine(); // resets scanner from int to string
    String ticketType = "";
    int departure = 0;
    int destination = 0;

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
      Payment payment = new Payment();
      if (payment.paymentMethod(totalAmount)) {
        for (int i = 0; i < numOfTickets; i++) {
          Ticket ticket = new Ticket(ticketType.toUpperCase(), issueDate, expiryDate, departure, destination, ticketAmount);
        }
        String description = "Purchased " + numOfTickets + " " + ticketType.toUpperCase() + " ticket(s) (" + departure + "-" + destination + ")";
        payment.setAmount(totalAmount);
        payment.setDescription(description);
        payment.setTransactionType("BUY");
        payment.insertPaymentToDB();
        System.out.println("\nThank You & have a safe trip!");
      }
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
    char choice;
    while (true) {
      System.out.print("Confirm Transaction? (Y/N): ");
      choice = scanner.next().toUpperCase().charAt(0);
      if (choice == 'Y' || choice == 'N') {
        break;
      } else {
        System.out.println("Invalid input! Please enter 'Y' or 'N'.");
      }
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

  private int selectTicketNum(){
    int ticketNum;
    while (true) {
      System.out.print("Enter Ticket Number : ");
      if (scanner.hasNextInt()) {
        ticketNum = scanner.nextInt();
        if (ticketNum <= 12340000) {
          System.out.println("Invalid Ticket Number! Please enter your ticket number correctly!");
          scanner.nextLine();
        } else {
          return ticketNum;
        }
      } else {
        System.out.println("Invalid Ticket Number! Please enter a valid ticket number!");
        scanner.nextLine();
      }
      return 0;
    }
  }
  
  private void validateTicket() {
    int ticketNum = selectTicketNum();
    
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
          System.out.println("Your ticket is valid until " + result.getString("expiryDate") + ".");
          extendTicketValidity(ticketNum);
        } else {
          System.out.println("Your ticket is expired and cannot be extended.");
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

  private void extendTicketValidity(int TicketNum) {
    char choice;
    while (true) {
      System.out.print("Would you like to extend your ticket validity? (Y/N) : ");
      choice = scanner.next().toUpperCase().charAt(0);
      if (choice == 'Y' || choice == 'N') {
        break;
      } else {
        System.out.println("Invalid input! Please enter 'Y' or 'N'.");
      }
    }
    if (choice == 'Y') {
      processExtendTicket(TicketNum);
    } else {
      System.out.println("Transaction aborted.\n");
    }
  }

  private void processExtendTicket(int ticketNum) {
    scanner.nextLine(); // Clear scanner buffer
    int extensionDays = 0;

    // Loop until a valid extension period is entered
    while (true) {
      System.out.print("Enter extension period (1/3/7) days: ");
      if (scanner.hasNextInt()) {
        extensionDays = scanner.nextInt();
        if (extensionDays == 1 || extensionDays == 3 || extensionDays == 7) {
          break;
        } else {
          System.out.println("Invalid extension period. Please enter 1, 3, or 7.");
        }
      } else {
        System.out.println("Invalid input! Please enter a valid number.");
        scanner.nextLine(); // Clear invalid input
      }
    }

    // Determine the price of the extension
    double extensionCost = (extensionDays == 1) ? 20 : (extensionDays == 3) ? 40 : 60;
    System.out.printf("The price for extension period of %d days is P%.2f\n", extensionDays, extensionCost);

    // Process payment
    Payment payment = new Payment();
    if (!payment.paymentMethod(extensionCost)) {
      System.out.println("Payment Failed. Transaction Aborted.");
      return;
    }

    // inserts the payment to db
    String description = "Extended ticket #" + ticketNum + " for " + extensionDays + " day(s)";
    payment.setAmount(extensionCost);
    payment.setDescription(description);
    payment.setTransactionType("EXT");
    payment.insertPaymentToDB();

    // Fetch ticket and update expiry date
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
        LocalDate expiryDate = LocalDate.parse(result.getString("expiryDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Calendar cal = Calendar.getInstance();
        cal.setTime(java.sql.Date.valueOf(expiryDate));
        cal.add(Calendar.DATE, extensionDays);
        Date newExpiryDate = cal.getTime();

        String updateQuery = "UPDATE tickets SET expiryDate = ? WHERE ticketID = ?";
        try (PreparedStatement updatePrep = con.prepareStatement(updateQuery)) {
          updatePrep.setDate(1, new java.sql.Date(newExpiryDate.getTime()));
          updatePrep.setInt(2, ticketNum);
          updatePrep.executeUpdate();
          System.out.println("Ticket validity extended successfully!");
          disp.displayTicketInfo(ticketNum);
          System.out.println("\n");
        }
      }
    } catch (Exception e) {
      System.out.println("Something went wrong with extending ticket validity");
      e.printStackTrace();
    } finally {
      dbConnect.closeResources(con, prep, result);
    }
  }

  private void accessControl() {
    int choice = 0;
    while (true) {

      System.out.println("\n\nMaintenance");
      System.out.println("Select Options:");
      System.out.println("1 - View Expired Tickets");
      System.out.println("2 - Delete Expired Tickets");
      System.out.println("3 - Shutdown");
      System.out.println("4 - Exit Maintenance");
      System.out.print("Choice : ");
      if (scanner.hasNextInt()) {
        choice = scanner.nextInt();
      } else {
        System.out.println("Please enter a valid Input!");
        scanner.nextLine();
      }

      switch (choice) {
        case 1: // Views Expired Tickets
          viewExpiredTickets();
          break;
        case 2: // Deletes Expired Tickets
          if (confirmTransaction()) {
            deleteExpiredTickets();
          }
          System.out.println();
          break;
        case 3: // Shutdown
          mainLoop = false;
          return;
        case 4:
          return;
        default:
          System.out.println("Invalid input! Please select a valid option!");
          scanner.nextLine();
          break;
      }//switch
    }
  }

  private void viewExpiredTickets() {
    dbConnect.connectToMachineDatabase();
    String query = "SELECT * FROM tickets WHERE expiryDate < CURRENT_DATE";
    Connection con = null;
    PreparedStatement prep = null;
    ResultSet result = null;
    int tixCount = 0;
    try {
      con = dbConnect.con;
      prep = con.prepareStatement(query);
      result = prep.executeQuery();
      System.out.println("--------------------------------");
      System.out.println("Ticket No. \t Expiry Date");
      System.out.println("--------------------------------");
      while (result.next()) {
        int ticketNum = result.getInt("ticketID");
        String expiryDate = result.getString("expiryDate");
        System.out.println(ticketNum + "\t " + expiryDate);
        tixCount++;
      }

      if (tixCount > 0) {
        System.out.println ("\n" + tixCount + " ticket(s) found!");
      }else{
        System.out.println("No Expired Tickets Found!");
      }
    } catch (Exception e) {
      System.out.println("Something went wrong with viewing expired tickets.");
      e.printStackTrace();
    } finally {
      dbConnect.closeResources(con, prep, result);
    }
  }
  
  private boolean logInAdmin(){
    scanner.nextLine();
    System.out.print("Enter Password : ");
    if(verifyPassword(scanner.nextLine())){
      accessControl();
      return true;
    }else{
      System.out.println("Password Incorrect! Returning to Main Menu.");
    } 
    return false;
  }
  
  private boolean verifyPassword(String password){
    final String adminPassword = "Java@2024";
    if(password.equals(adminPassword)){
      return true;
    }
    return false;
  }
  
  private void deleteExpiredTickets() {
    dbConnect.connectToMachineDatabase();
    String deleteQuery = "DELETE FROM tickets WHERE expiryDate < CURRENT_DATE";
    Connection con = null;
    PreparedStatement prep = null;

    try {
      con = dbConnect.con;
      prep = con.prepareStatement(deleteQuery);
      int rowsDeleted = prep.executeUpdate();
      System.out.println("Total Expired Tickets Deleted: " + rowsDeleted);
    } catch (Exception e) {
      System.out.println("Something went wrong with deleting expired tickets.");
      e.printStackTrace();
    } finally {
      dbConnect.closeResources(con, prep, null);
    }
  }

  public static void main(String[] args) {
    TrainTicketVM vendingMachine = new TrainTicketVM();
    while (mainLoop) {
      vendingMachine.landingPage();
    }
    System.out.println("Powering OFF!");
  }
}
