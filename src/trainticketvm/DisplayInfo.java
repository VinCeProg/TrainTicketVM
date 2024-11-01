package trainticketvm;

import java.sql.*;

public class DisplayInfo {

  private SysConnectMySQL dbConnect = new SysConnectMySQL();

  public void displayStations(String ticketType) {

    dbConnect.connectToMachineDatabase();
    String selectedTrain = selectedTrainQuery(ticketType);

    // query to be passed onto database
    String query = "SELECT * FROM stations WHERE " + selectedTrain + " = true";

    try (Connection con = dbConnect.con;
            PreparedStatement prep = con.prepareStatement(query)) {
      ResultSet result = prep.executeQuery();
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

    try (Connection con = dbConnect.con;
            PreparedStatement prep = con.prepareStatement(query)) {
      ResultSet result = prep.executeQuery();
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
    }
  }

  public String displaySelectedStation(int station) {
    dbConnect.connectToMachineDatabase();
    String query = "SELECT * FROM stations WHERE stationID = ?";

    try (Connection con = dbConnect.con; PreparedStatement prep = con.prepareStatement(query)) {
      prep.setInt(1, station);
      ResultSet result = prep.executeQuery();
      if(result.next()){
        return result.getString("stationName");
      }
    } catch (Exception e) {
      System.out.println("Something went wrong with displaying selected station!");
      e.printStackTrace();
    }
    return "Error!";
  }
}
