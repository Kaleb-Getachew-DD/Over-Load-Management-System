
package otapp;
  import java.sql.Connection;
 import java.sql.DriverManager;
import java.sql.SQLException;

public class database {
    public static Connection connect() throws SQLException{
      
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/java", "admin", "");
            return connect;
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        
        return null;
    }
}
