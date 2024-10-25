package trainticketvm;

import java.util.Scanner;
import java.util.InputMismatchException;

public class Payment {

  public boolean makeCashPayment(double amount) {

    Scanner scanner = new Scanner(System.in);
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
  
  public boolean makeCashlessPayment(double amount, String method){
    
    return true;
  }
}
