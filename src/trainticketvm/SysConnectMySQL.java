package trainticketvm;

import java.sql.*;

public class SysConnectMySQL {

  private static final String URL = "jdbc:mysql://localhost:3306/";
  private static final String USERNAME = "root";
  private static final String PASSWORD = "";

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(URL, USERNAME, PASSWORD);
  }
}
