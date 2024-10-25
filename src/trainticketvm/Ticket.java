package trainticketvm;

import java.util.Date;
import java.sql.*;

public class Ticket {

  private SysConnectMySQL dbConnect = new SysConnectMySQL();
  private String ticketID;
  private String ticketType;
  private Date issueDate;
  private Date expiryDate;
  private int departureID;
  private int destinationID;

  public Ticket(String ticketType, Date issueDate, Date expiryDate, int departureID, int destinationID) {
    this.ticketType = ticketType;
    this.issueDate = issueDate;
    this.expiryDate = expiryDate;
    this.departureID = departureID;
    this.destinationID = destinationID;
  }

  public void insertTicket() {
    dbConnect.connectToMachineDatabase();
    String query = "INSERT INTO tickets (ticketType, issueDate, expiryDate, departureID, destinationID) VALUES(?, ?, ?, ?, ?)";
    try (Connection con = dbConnect.con;
            PreparedStatement prep = con.prepareStatement(query)) {
      prep.setString(1, ticketType);
      prep.setDate(2, new java.sql.Date(issueDate.getTime()));
      prep.setDate(3, new java.sql.Date(expiryDate.getTime()));
      prep.setInt(4, departureID);
      prep.setInt(5, destinationID);
      prep.executeUpdate();
    } catch (Exception e) {
      System.out.println("Something went wrong with inserting ticket into database!");
      e.printStackTrace();
    }
  }

  private static void displayTicket() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private static void dispenseTicket() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public String getTicketID() {
    return ticketID;
  }

  public void setTicketID(String ticketID) {
    this.ticketID = ticketID;
  }

  public String getTicketType() {
    return ticketType;
  }

  public void setTicketType(String ticketType) {
    this.ticketType = ticketType;
  }

  public Date getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(Date issueDate) {
    this.issueDate = issueDate;
  }

  public Date getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }
}
