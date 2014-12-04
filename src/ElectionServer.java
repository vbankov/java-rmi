import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ElectionServer {
    public static void initializeDB() throws SQLException{
        try{
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DBElection", "dsws", "dsws");
            Statement stmt = conn.createStatement();
            // empty candidates table
            String query = "TRUNCATE TABLE CANDIDATE"; // empty candidates table
            stmt.execute(query);

            // the candidates dataset is
            List<String> z = new ArrayList<String>();
            z.add("Goofy");
            z.add("Donald Duck");
            z.add("Mickey Mouse");
            z.add("Pluto");
            z.add("Uncle Scrooge");
            // populate db with candidates
            int j;
            for(int i=0;i<5;i++){
                j = i+1;
                query = "INSERT INTO CANDIDATE(ID,NAME,VOTES) VALUES ("+j+",\'"+z.get(i)+"\',0) ";
                stmt.execute(query);
            }
            // truncate and populate db with voters
                query = "TRUNCATE TABLE VOTER"; // empty voters table
                stmt.execute(query);
            for(int i=1;i<11;i++){
                query = "INSERT INTO VOTER(ID,HASVOTED) VALUES ("+i+",0) ";
                stmt.execute(query);
            }
            // close statement and db connection
            stmt.close();
            conn.close();  
        }catch (SQLException se){
            System.out.println("BOOOM ! SQLException");
            se.printStackTrace(); // DEBUG ONLY! DELETE THIS IN PRODUCTION
        } 
    }
    public static void main (String args[]) {
        System.setSecurityManager (new MyRMISecurityManager()); // set up a security manager that can handle remote stubs
        try {
            Registry registry = LocateRegistry.createRegistry(18300); // bind an instance of RMIElection class in Registry 
            String name = "rmi://localhost/ElectionService";
            // initialize database state
            System.out.print("Populating Database: ");
            initializeDB();
            System.out.print("\t\tComplete\n");
            // initialize electionController
            Election c = new ElectionController();
            // bind election to registry
            registry.rebind(name, c);
            // shout-out
            System.out.println("Election service started: \tRMI magic happens on port 18300");
        }
        catch (RemoteException | SQLException e) { 
            System.out.println("Exception caught " + "during: " + e);
            e.printStackTrace(); // DEBUG ONLY! DELETE THIS IN PRODUCTION
        }
    } // main
} // ElectionServer
