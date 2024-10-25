package trainticketvm;

import java.util.Date;

public class Payment {
  private String paymentID;
  private double amount;
  private Date paymentDate;
  private String paymentMethod;

  
  
  
  public String getPaymentID() {
    return paymentID;
  }

  public void setPaymentID(String paymentID) {
    this.paymentID = paymentID;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public Date getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(Date paymentDate) {
    this.paymentDate = paymentDate;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }
}
