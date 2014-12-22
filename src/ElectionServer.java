/*
 * The MIT License
 *
 * Copyright 2014 Vasilis Bankov, George Peppas, Maria Theodoraki.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.derby.drda.NetworkServerControl;
import SecurityController.PasswordHandler;        
import java.io.PrintWriter;
import java.util.Collections;

public class ElectionServer {
    private static final String DB_CONNECTION = "jdbc:derby://localhost:1527/DBElection";
    private static final String DB_USER = "dsws";
    private static final String DB_PASSWORD = "dsws";
    private static  NetworkServerControl server = null;
    public static void initializeDB() throws SQLException, UnknownHostException, Exception{
        /* For password-enabled login add a column named `PASSWORD` of type VarChar in the `VOTER` table and uncomment all lines where noted */
        server = new NetworkServerControl(InetAddress.getByName("localhost"),1527);
        
        // start the db server
         server.start(new PrintWriter(System.out));
        
        // initialize connection        
        Connection conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        Statement stmt = conn.createStatement();
        PreparedStatement pstmt = null;
        
        // empty candidates table
        String emptyTableCands = "TRUNCATE TABLE CANDIDATE"; // empty candidates table
        stmt.execute(emptyTableCands);
        // the candidates list is
        List<String> listCandidates = new ArrayList<>();
        listCandidates.add("Donald Duck");
        listCandidates.add("Duffy Duck");
        listCandidates.add("Goofy");
        listCandidates.add("Mickey Mouse");
        listCandidates.add("Pluto");
        // shuffle the list
        Collections.shuffle(listCandidates);
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
            
            /* to enable PasswordEnabledLogin, remove comment from next line */
            // PasswordHandler.savePassword((i+1), "password");
            
        }
        // close statement and db connection
        stmt.close();
        conn.close();
    }
    
    public static void main (String args[]) throws RemoteException, SQLException, Exception {
        System.out.println("=== \tElection Server starting \t==");
        // set up a security manager that can handle remote stubs
        System.setSecurityManager (new TheRMISecurityManager()); 
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

        // things shoulb be awesome now. inform the humans
        System.out.println("Election service started: \tRMI magic happens on port 18300");
        
    } // main
} // ElectionServer
