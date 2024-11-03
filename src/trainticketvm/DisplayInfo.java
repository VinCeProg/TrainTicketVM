package trainticketvm;

import java.sql.*;

public class DisplayInfo {

  private SysConnectMySQL dbConnect = new SysConnectMySQL();

  public void displayStations(String ticketType) {
    dbConnect.connectToMachineDatabase();
    String selectedTrain = selectedTrainQuery(ticketType);
    String query = "SELECT * FROM stations WHERE " + selectedTrain + " = true";
    Connection con = null;
    PreparedStatement prep = null;
    ResultSet result = null;
    try {
      con = dbConnect.con;
      prep = con.prepareStatement(query);
      result = prep.executeQuery();
      System.out.println("--------------------------------");
      System.out.println("Station ID \t Station Name");
      System.out.println("--------------------------------");
      while (result.next()) {
        int stationID = result.getInt("stationID");
        String stationName = result.getString("stationName");
        System.out.println(stationID + "\t\t " + stationName);
      }
    } catch (Exception e) {
      System.out.println("Something went wrong with displaying Stations");
      e.printStackTrace();
    } finally {
      dbConnect.closeResources(con, prep, result);
    }
  }

  public String selectedTrainQuery(String ticketType) {

    if (ticketType.equalsIgnoreCase("COMMUTER")) {
      return "onRoute_Commuter";
    } else if (ticketType.equalsIgnoreCase("COMMUTERX")) {
      return "onRoute_CommuterX";
    } else if (ticketType.equalsIgnoreCase("LIMITED")) {
      return "onRoute_Limited";
    } else {
      return null;
    }
  }

  public void displayStations(String ticketType, int departureStation) {
    dbConnect.connectToMachineDatabase();
    String selectedTrain = selectedTrainQuery(ticketType);
    String query = "SELECT * FROM stations WHERE " + selectedTrain + " = true AND stationID != " + departureStation;
    Connection con = null;
    PreparedStatement prep = null;
    ResultSet result = null;
    try {
      con = dbConnect.con;
      prep = con.prepareStatement(query);
      result = prep.executeQuery();
      System.out.println("--------------------------------");
      System.out.println("Station ID \t Station Name");
      System.out.println("--------------------------------");
      while (result.next()) {
        int stationID = result.getInt("stationID");
        String stationName = result.getString("stationName");
        System.out.println(stationID + "\t\t " + stationName);
      }
    } catch (Exception e) {
      System.out.println("Something went wrong with displaying Stations");
      e.printStackTrace();
    } finally {
      dbConnect.closeResources(con, prep, result);
    }
  }

  public String displaySelectedStation(int station) {
    dbConnect.connectToMachineDatabase();
    String query = "SELECT * FROM stations WHERE stationID = ?";
    Connection con = null;
    PreparedStatement prep = null;
    ResultSet result = null;
    try {
      con = dbConnect.con;
      prep = con.prepareStatement(query);
      prep.setInt(1, station);
      result = prep.executeQuery();
      if (result.next()) {
        return result.getString("stationName");
      }
    } catch (Exception e) {
      System.out.println("Something went wrong with displaying selected station!");
      e.printStackTrace();
    } finally {
      dbConnect.closeResources(con, prep, result);
    }
    return "Error!";
  }

  public void displayTicketInfo(int ticketNum) {

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
      }
    } catch (Exception e) {
      System.out.println("Something went wrong with getting ticket information");
      e.printStackTrace();
    } finally {
      dbConnect.closeResources(con, prep, result);
    }
  }
}
