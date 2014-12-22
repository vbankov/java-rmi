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
import SecurityController.PasswordHandler;
import java.rmi.*;
import java.rmi.server.*;
import java.sql.*;
import java.util.*;
public class ElectionController extends UnicastRemoteObject implements Election {
    
    private static List<Integer> loggedInIDs = new ArrayList<>();
   
    public ElectionController() throws RemoteException, SQLException {
        super(); 
    }
    
    @Override
    public List<Candidate> getCandidates() throws RemoteException, SQLException  {
        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DBElection", "dsws", "dsws");
        String q = "SELECT ID,NAME FROM CANDIDATE";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(q);
        List<Candidate> cands = new ArrayList<Candidate>();
        while (rs.next()) {  
            String cName = rs.getString("NAME");
            int i = rs.getInt("ID");
            Candidate tempCand = new Candidate();
            tempCand.id = i;
            tempCand.name = cName;
            cands.add(tempCand);
        }
        conn.close();
        return cands;
    }
    
    @Override
    public ElectionResult getResults(int id) throws RemoteException, SQLException{
        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DBElection", "dsws", "dsws");
        String q = "SELECT * FROM CANDIDATE WHERE ID="+id;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(q);
        ElectionResult r = new ElectionResult();
        while (rs.next()) {
            r.candidateId = rs.getInt("ID");
            r.name = rs.getString("NAME");
            r.votes = rs.getInt("VOTES");
        }
        return r;
    }
    
    @Override
    public boolean login(int voterID, String providedPassword) throws RemoteException, SQLException{
        // Check if the voterID exists in our db and if the provided password is legit
        String q = "SELECT COUNT(*) AS count FROM VOTER WHERE ID="+voterID+"";
        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DBElection", "dsws", "dsws");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(q);
        /* to enable PasswordEnabledLogin, remove comment from next line */
        //boolean legitPassword = false;
        int k=0;
        while(rs.next()){
            k = rs.getInt("count");
        }
        if(k>0){    // The provided userID exists in our DB
            if(!this.loggedInIDs.contains(k)) {     // Append ID to loggedIn people array
                this.loggedInIDs.add(voterID);
                System.out.println("[Info]\tUser "+voterID+" has connected");
            }
            /* to enable PasswordEnabledLogin, add comment to next line */
            return true;
            /* to enable PasswordEnabledLogin, remove comment from next line */
            //legitPassword = PasswordHandler.checkPassword(voterID, providedPassword);
        }else{
            return false;
        }
        /* to enable PasswordEnabledLogin, remove comment from next line */
        //return legitPassword;
    }
    
    @Override
    public void logout(int voterID) throws RemoteException{
        if(this.loggedInIDs.contains(voterID)) {this.loggedInIDs.remove((Integer)voterID);}
        System.out.println("[Info]\tUser "+voterID+" has disconnected");
    }
    
    @Override
    public boolean canVote(int voterID) throws RemoteException,SQLException{
        String q = "SELECT HASVOTED FROM VOTER WHERE ID = "+voterID;
        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DBElection", "dsws", "dsws");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(q);
        int h=1; // Initialize hasVoted to true to make sure that they can't vote if things break
        while(rs.next()){
            h = rs.getInt("HASVOTED");
        }
        stmt.close();
        conn.close();
        return (h==0);
    }
    @Override
    public boolean vote(int voterID, int candidateID) throws RemoteException, SQLException{
        // Get candidate number of votes 
        String q = "SELECT VOTES FROM CANDIDATE WHERE ID="+candidateID;     // query for the vote count
        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DBElection", "dsws", "dsws");    //connect to db
        Statement stmt = conn.createStatement();    // create a new statement
        ResultSet rs = stmt.executeQuery(q);        // execute the query
        boolean votedStatus = false;
        int currentVotes=-1;
        while(rs.next()){
            currentVotes = rs.getInt("VOTES");
        }
        // If NOT canVote return false
        if(!canVote(voterID)){  
            return false;
        }
        // If canVote Save vote and revoke vote right
        // We will use an SQL prepared statement to sync the queries and assure data integrity using a rollback in any case of a problem
        int newVotes = currentVotes+1;
        PreparedStatement pstmtC = null; // prepared statement for Candidate
        PreparedStatement pstmtV = null; // prepared statement for Voter
        try{
            conn.setAutoCommit(false); // disable auto commit and do it manually
            q = "UPDATE CANDIDATE SET VOTES=? WHERE ID = ? ";   // new candidate votes value
            pstmtC  = conn.prepareStatement(q);
            pstmtC.setInt(1, newVotes);
            pstmtC.setInt(2, candidateID);
            int updateVotes = pstmtC.executeUpdate();
            q = "UPDATE VOTER SET HASVOTED=1 WHERE ID = ?";     // new user hasVoted status
            pstmtV = conn.prepareStatement(q);
            pstmtV.setInt(1,voterID);
            int updateVoterRight = pstmtV.executeUpdate();      // try to execute the above
            System.out.println("A vote for candidate "+candidateID+" was casted with status "+updateVotes);
            System.out.println("Voter "+voterID+" voted with status "+updateVoterRight);
            conn.commit();  // Manually commit the above changes
            votedStatus = true;
        }catch (SQLException se){   // SQLExceptions: Gotta catch em all !
            System.out.println("It's dead Jim!! An SQLException killed it!");
            se.printStackTrace();
            try {
                System.err.print("Transaction is being rolled-back");
                conn.rollback();
            } catch(SQLException excep) {
                System.out.println("We are in big trouble. Last transaction can't be rolled-back!"); // This is why we use a license
            }
        }finally{   // Connections & Statements: Gotta close em all !
            pstmtC.close();
            pstmtV.close(); 
            conn.setAutoCommit(true);   // re-enable auto commit
            conn.close();
        }
        return votedStatus;
    }
} // ElectionController