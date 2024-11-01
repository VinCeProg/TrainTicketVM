package trainticketvm;

import java.sql.*;

public class SysConnectMySQL {

  final String url = "jdbc:mysql://localhost:3306/trainticketvm";
  final String username = "root";
  final String password = "";

  public PreparedStatement prep;
  public Connection con;
  public Statement state;
  public ResultSet result;

  public void connectToMachineDatabase() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      con = DriverManager.getConnection(url, username, password);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  public void closeResources(Connection con, Statement state, ResultSet result) {
    try {
        if (result != null) result.close();
        if (state != null) state.close();
        if (con != null) con.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}
