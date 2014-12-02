//package calculatorNB;
import java.sql.*;
public class testClass{
    public static void main(String[] args){
        String host = "jdbc:derby://localhost:1527/DBElection";
        String user = "dsws";
        String pass = "dsws";
        try {
            Connection con = DriverManager.getConnection(host,user,pass);
        }
        catch ( SQLException err ) {
            System.out.println( err.getMessage( ) );
        }
    }
}