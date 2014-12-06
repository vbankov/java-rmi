import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ElectionServer {
    
        private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String DB_CONNECTION = "jdbc:derby://localhost:1527/DBElection";
	private static final String DB_USER = "dsws";
	private static final String DB_PASSWORD = "dsws";
    
    
    public static void initializeDB() throws SQLException{
       
// initialize connection        
            Connection conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            Statement stmt = conn.createStatement();
            PreparedStatement pstmt = null;
            
            
// empty candidates table
            String emptyTableCands = "TRUNCATE TABLE CANDIDATE"; // empty candidates table
            stmt.execute(emptyTableCands);

// the candidates list is
            List<String> listCandidates = new ArrayList<>();
            listCandidates.add("Goofy");
            listCandidates.add("Donald Duck");
            listCandidates.add("Mickey Mouse");
            listCandidates.add("Pluto");
            listCandidates.add("Uncle Scrooge");
            
// populate db with candidates
           
            String insertCandidates = "INSERT INTO CANDIDATE(ID,NAME,VOTES) VALUES(?,?,?)";
            pstmt = conn.prepareStatement(insertCandidates);
            for(int i=0;i<5;i++){
              //  insertCandidates = "INSERT INTO CANDIDATE(ID,NAME,VOTES) VALUES ("+(i+1)+",\'"+listCandidates.get(i)+"\',0) ";
                pstmt.setInt(1, i+1);
                pstmt.setString(2, listCandidates.get(i));
                pstmt.setInt(3, 0);
                int executeUpdate = pstmt.executeUpdate();
                //stmt.execute(insertCandidates);
            }
            
// truncate and populate db with voters
                String  emptyTableVoters = "TRUNCATE TABLE VOTER"; // empty voters table
                stmt.execute(emptyTableVoters);
                
                
// fill Voters table                
            String insertVoters;
            for(int i=0;i<10;i++){
                insertVoters = "INSERT INTO VOTER(ID,HASVOTED) VALUES ("+(i+1)+",0) ";
                stmt.execute(insertVoters);
            }
            
// close statement and db connection
            stmt.close();
            conn.close();  
        
    }
    
    
    
    
    public static void main (String args[]) throws RemoteException, SQLException {
       
        System.setSecurityManager (new MyRMISecurityManager()); // set up a security manager that can handle remote stubs
        
       
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
        
        
        
        
    } // main
} // ElectionServer
