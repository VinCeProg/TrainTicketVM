package trainticketvm;

import java.util.Scanner;

public class TrainTicketVM {
  
  private static Scanner scanner = new Scanner(System.in);
  private static boolean mainLoop = true;
  
  public static void main(String[] args) {
    
    while(mainLoop){
      landingPage();
    }
  }
  
  private static void landingPage(){
    System.out.println("Train Ticket Vending Machine\n");
    System.out.println("Select Options:");
    System.out.println("1 - Buy Ticket/s");
    System.out.println("2 - Check Ticket Validity");
    System.out.println("3 - Exit");
    selectOptionLP();
  }
  
  private static void selectOptionLP(){
    System.out.print("Choice : ");
    int choice = scanner.nextInt();
    
    switch(choice){
      case 1: // Buy Ticket
        System.out.println("Ticket Sold!");
        break;
      case 2: // Check Ticket Validity
        System.out.println("Ticket Valid!");
        break;
      case 3: // exit NOTE: this will be transferred in Admin Access later
        mainLoop = false;
        break;
      case 4: // Admin Access to Program
        
        break;
      default:
        
        break;
    }//switch
  }
  
  public static void buyTicket(){
    
  }
}
