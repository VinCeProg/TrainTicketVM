package trainticketvm;

import java.util.Date;
import java.sql.*;

public class Ticket {

  private static SysConnectMySQL dbConnect = new SysConnectMySQL();
  private String ticketID;
  private String ticketType;
  private Date issueDate;
  private Date expiryDate;
  private int departureID;
  private int destinationID;
  private double amount;
  private String paymentMethod;

  public Ticket(String ticketType, Date issueDate, Date expiryDate, int departureID, int destinationID, double amount, String paymentMethod) {
    this.ticketType = ticketType;
    this.issueDate = issueDate;
    this.expiryDate = expiryDate;
    this.departureID = departureID;
    this.destinationID = destinationID;
    this.amount = amount;
    this.paymentMethod = paymentMethod;
    insertTicket();
    displayTicket();
  }

  public void insertTicket() {
    dbConnect.connectToMachineDatabase();
    String query = "INSERT INTO tickets (ticketType, issueDate, expiryDate, departureID, destinationID, amount, paymentMethod) VALUES(?, ?, ?, ?, ?, ?, ?)";
    Connection con = null;
    PreparedStatement prep = null;
    try {
        con = dbConnect.con;
        prep = con.prepareStatement(query);
        prep.setString(1, ticketType);
        prep.setDate(2, new java.sql.Date(issueDate.getTime()));
        prep.setDate(3, new java.sql.Date(expiryDate.getTime()));
        prep.setInt(4, departureID);
        prep.setInt(5, destinationID);
        prep.setDouble(6, amount);
        prep.setString(7, paymentMethod);
        prep.executeUpdate();
    } catch (Exception e) {
        System.out.println("Something went wrong with inserting ticket into database!");
        e.printStackTrace();
    } finally {
        dbConnect.closeResources(con, prep, null);
    }
}


  public static void displayTicket() {
    dbConnect.connectToMachineDatabase();
    DisplayInfo disp = new DisplayInfo();
    String query = "SELECT * FROM tickets ORDER BY ticketID DESC LIMIT 1;";
    try (Connection con = dbConnect.con;
            PreparedStatement prep = con.prepareStatement(query)) {
      ResultSet result = prep.executeQuery();
      if (result.next()) {
        System.out.println();
        System.out.println("********** Ticket **********");
        System.out.println("Ticket ID No  :  " + result.getInt("ticketID"));
        System.out.println("Ticket Type   :  " + result.getString("ticketType"));
        System.out.println("Issue Date    :  " + result.getString("issueDate"));
        System.out.println("Expiry Date   :  " + result.getString("expiryDate"));
        System.out.println("Departure     :  " + disp.displaySelectedStation(result.getInt("departureID")));
        System.out.println("Destination   :  " + disp.displaySelectedStation(result.getInt("destinationID")));
        System.out.println("Amount        :  P" + result.getDouble("amount"));
        System.out.println("****************************");
      } else {
        System.out.println("No tickets found.");
      }
    } catch (Exception e) {
      System.out.println("Something went wrong with getting ticket information");
      e.printStackTrace();
    } 
  }
}
