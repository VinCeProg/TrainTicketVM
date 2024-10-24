package trainticketvm;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.*;

public class Ticket {

  private SysConnectMySQL dbConnect = new SysConnectMySQL();
  private String ticketID;
  private String ticketType;
  private Date issueDate;
  private Date expiryDate;

  public Ticket(String ticketID, String issueDateStr, String expiryDateStr, String ticketType) throws Exception {
    this.ticketID = ticketID;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    this.issueDate = sdf.parse(issueDateStr);
    this.expiryDate = sdf.parse(expiryDateStr);
    this.ticketType = ticketType;
  }

  public void saveToDatabase() {
    String query = "INSERT INTO Tickets (ticketID, issueDate, expiryDate, ticketType) VALUES (?, ?, ?, ?)";
    try (Connection con = SysConnectMySQL.getConnection();
            PreparedStatement pst = con.prepareStatement(query)) {
      pst.setString(1, ticketID);
      pst.setDate(2, new java.sql.Date(issueDate.getTime()));
      pst.setDate(3, new java.sql.Date(expiryDate.getTime()));
      pst.setString(4, ticketType);
      pst.executeUpdate();
    } catch (SQLException ex) {
      System.out.println("Oh Naur! Something went wrong!");
      ex.printStackTrace();
    }
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
