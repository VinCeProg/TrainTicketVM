package trainticketvm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Date;

public class Payment {

  private double amount;
  private String paymentMethod;
  private String description;

  private Scanner scanner = new Scanner(System.in);
  private static SysConnectMySQL dbConnect = new SysConnectMySQL();

  public boolean paymentMethod(double ticketAmount) {
    String paymentMethod = "";
    while (true) {
      System.out.print("Choose payment method (CASH, CARD, or MOBILE): ");
      paymentMethod = scanner.next().toUpperCase();

      switch (paymentMethod) {
        case "CASH":
          this.paymentMethod = paymentMethod;
          return makeCashPayment(ticketAmount);
        case "CARD":
        case "MOBILE":
          this.paymentMethod = paymentMethod;
          return makeCashlessPayment(ticketAmount, paymentMethod);
        default:
          System.out.println("Invalid Payment Method!");
          continue;
      }
    }
  }

  public void insertPaymentToDB() {
    dbConnect.connectToMachineDatabase();
    String query = "INSERT INTO payments (amount, payment_description, payment_method) VALUES(?, ?, ?)";
    Connection con = null;
    PreparedStatement prep = null;
    try {
      con = dbConnect.con;
      prep = con.prepareStatement(query);
      prep.setDouble(2, getAmount());
      prep.setString(3, getDescription());
      prep.setString(4, getPaymentMethod());
      prep.executeUpdate();
    } catch (Exception e) {
      System.out.println("Something went wrong with inserting payment into database!");
      e.printStackTrace();
    } finally {
      dbConnect.closeResources(con, prep, null);
    }
  }

  public boolean makeCashPayment(double amount) {

    double totalInserted = 0;
    double remainingAmount = amount;

    while (remainingAmount > 0) {
      System.out.printf("\nAmount Due: %.2f\n", remainingAmount);
      System.out.print("Insert Cash/Coin (Enter -1 to cancel): ");
      try {
        double cashCoin = scanner.nextDouble();

        if (cashCoin == -1) {
          System.out.println("Transaction cancelled. Please collect your money.");
          return false;
        }

        if (validateCash(cashCoin)) {
          totalInserted += cashCoin;
          remainingAmount -= cashCoin;
          System.out.printf("Inserted\t\t:  %.2f\n", totalInserted);
          System.out.printf("Amount Remaining\t:  %.2f\n", remainingAmount);
        } else {
          System.out.println("Invalid Denomination! Please insert a valid cash or coin!");
        }
      } catch (InputMismatchException e) {
        System.out.println("Invalid input! Please insert a valid cash/coin.");
        scanner.next();
      }
    }
    if (remainingAmount < 0) {
      System.out.printf("Change: %.2f\n", Math.abs(remainingAmount));
    }
    System.out.println("Payment Complete. Thank you!");
    return true;
  }

  private boolean validateCash(double cashCoin) {
    return (cashCoin == .25 || cashCoin == 1 || cashCoin == 5 || cashCoin == 10
            || cashCoin == 20 || cashCoin == 50 || cashCoin == 100 || cashCoin == 200
            || cashCoin == 500 || cashCoin == 1000);
  }

  public boolean makeCashlessPayment(double amount, String method) {
    System.out.printf("Processing %s payment for amount:  %.2f\n", method, amount);
    if (processCashlessPayment(amount)) {
      System.out.println("Payment Successful. Thank you!");
      return true;
    } else {
      System.out.println("Payment Failed. Please try again.");
      return false;
    }
  }

  private boolean processCashlessPayment(double amount) {
    try {
      Thread.sleep(2000); // Simulate payment processing delay
      return Math.random() * 100 < 97;
    } catch (InterruptedException e) {
      e.printStackTrace();
      return false;
    }
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
