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
