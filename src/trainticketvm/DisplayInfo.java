package trainticketvm;

import java.sql.*;

public abstract class DisplayInfo {

  private static SysConnectMySQL dbconnection = new SysConnectMySQL();

  public static void displayStations(String ticketType) {
    dbconnection.connectToMachineDatabase();
    String selectedTrain = "";
    if (ticketType.equalsIgnoreCase("COMMUTER")) {
      selectedTrain = "onRoute_Commuter";
    } else if (ticketType.equalsIgnoreCase("COMMUTERX")) {
      selectedTrain = "onRoute_CommuterX";
    } else if (ticketType.equalsIgnoreCase("LIMITED")) {
      selectedTrain = "onRoute_Limited";
    }
    // query to be passed onto database
    String query = "SELECT * FROM stations WHERE " + selectedTrain + " = true";

    try (Connection con = dbconnection.con;
            PreparedStatement prep = con.prepareStatement(query)) {
      ResultSet result = prep.executeQuery();
      System.out.println("Station ID \t Station Name");
      while (result.next()) {
        int stationID = result.getInt("stationID");
        String stationName = result.getString("stationName");
        System.out.println(stationID + "\t\t " + stationName);
      }

    } catch (Exception e) {
      System.out.println("Something went wrong with displaying Stations");
      e.printStackTrace();
    }
  }

  public static void displayStations(String ticketType, int departureStation) {
    dbconnection.connectToMachineDatabase();
    String selectedTrain = "";
    if (ticketType.equalsIgnoreCase("COMMUTER")) {
      selectedTrain = "onRoute_Commuter";
    } else if (ticketType.equalsIgnoreCase("COMMUTERX")) {
      selectedTrain = "onRoute_CommuterX";
    } else if (ticketType.equalsIgnoreCase("LIMITED")) {
      selectedTrain = "onRoute_Limited";
    }

    String query = "SELECT * FROM stations WHERE " + selectedTrain + " = true AND stationID != " + departureStation;

    try (Connection con = dbconnection.con;
            PreparedStatement prep = con.prepareStatement(query)) {
      ResultSet result = prep.executeQuery();
      System.out.println("Station ID \t Station Name");
      while (result.next()) {
        int stationID = result.getInt("stationID");
        String stationName = result.getString("stationName");
        System.out.println(stationID + "\t\t " + stationName);
      }

    } catch (Exception e) {
      System.out.println("Something went wrong with displaying Stations");
      e.printStackTrace();
    }
  }
}
