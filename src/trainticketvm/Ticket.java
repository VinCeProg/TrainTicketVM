package trainticketvm;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.*;

public class Ticket {

  private SysConnectMySQL dbConnect = new SysConnectMySQL();
  private int ticketID;
  private String ticketType;
  private Date issueDate;
  private Date expiryDate;

  public Ticket(int ticketID, String ticketType, String issueDateStr, String expiryDateStr) throws Exception {
    this.ticketID = ticketID;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    this.issueDate = sdf.parse(issueDateStr);
    this.expiryDate = sdf.parse(expiryDateStr);
    this.ticketType = ticketType;
  }

  public void saveToDatabase() {
    String query = "INSERT INTO Tickets (ticketID,ticketType, issueDate, expiryDate) VALUES (?, ?, ?, ?)";
    try (Connection con = SysConnectMySQL.getConnection();
            PreparedStatement pst = con.prepareStatement(query)) {
      pst.setInt(1, ticketID);
      pst.setString(2, ticketType);
      pst.setDate(3, new java.sql.Date(issueDate.getTime()));
      pst.setDate(4, new java.sql.Date(expiryDate.getTime()));
      pst.executeUpdate();
    } catch (SQLException ex) {
      System.out.println("Oh Naur! Something went wrong!");
      ex.printStackTrace();
    }
  }
  
  public int getTicketID() {
    return ticketID;
  }

  public void setTicketID(int ticketID) {
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
