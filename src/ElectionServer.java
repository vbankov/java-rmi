import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.derby.drda.NetworkServerControl;

public class ElectionServer {
    private static final String DB_CONNECTION = "jdbc:derby://localhost:1527/DBElection";
    private static final String DB_USER = "dsws";
    private static final String DB_PASSWORD = "dsws";
    private static  NetworkServerControl server = null;
    public static void initializeDB() throws SQLException, UnknownHostException, Exception{
        server = new NetworkServerControl(InetAddress.getByName("localhost"),1527);
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
        try{
            conn.setAutoCommit(false);
            String insertCandidates = "INSERT INTO CANDIDATE(ID,NAME,VOTES) VALUES(?,?,?)";
            pstmt = conn.prepareStatement(insertCandidates);
            for(int i=0;i<5;i++){
                pstmt.setInt(1, i+1);
                pstmt.setString(2, listCandidates.get(i));
                pstmt.setInt(3, 0);
                int executeUpdate = pstmt.executeUpdate();
                conn.commit();
            } 
        }
        catch(SQLException se){
            se.printStackTrace();
            try {
                System.err.print("Transaction is being rolled back");
                conn.rollback();
            } catch(SQLException sse) {
                sse.printStackTrace();
            }
        }
        finally{
           pstmt.close();
           conn.setAutoCommit(true);
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
    
    public static void main (String args[]) throws RemoteException, SQLException, Exception {
        // set up a security manager that can handle remote stubs
        System.setSecurityManager (new MyRMISecurityManager()); 
        // bind an instance of RMIElection class in Registry 
        Registry registry = LocateRegistry.createRegistry(18300); 
        String name = "rmi://localhost/ElectionService";
        // initialize database state
        System.out.print("Populating Database: ");
        initializeDB();
        System.out.print("\t\tComplete\n");
        // initialize electionController
        Election newElection = new ElectionController();

        // bind election to registry
        registry.rebind(name, newElection);

        // shout out
        System.out.println("Election service started: \tRMI magic happens on port 18300");
        
        
        
        
    } // main
} // ElectionServer
