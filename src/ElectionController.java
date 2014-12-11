// a servant class
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
    public int login(int voterID) throws RemoteException, SQLException{
        String q = "SELECT COUNT(*) AS count FROM VOTER WHERE ID="+voterID+"";
        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DBElection", "dsws", "dsws");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(q);
        int k=0;
        while(rs.next()){
            k = rs.getInt("count");
        }
        if(k>0){
            if(!this.loggedInIDs.contains(k)) {
                this.loggedInIDs.add(voterID);
                System.out.println("[Info]\tUser "+voterID+" has connected");
            }
            return 1;
        }
        return 0;
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
    public void vote(int voterID, int candidateID) throws RemoteException, SQLException{
        System.out.println("vote fired for voterID: "+voterID);
        // Get candidate number of votes 
        String q = "SELECT VOTES FROM CANDIDATE WHERE ID="+candidateID;
        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DBElection", "dsws", "dsws");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(q);
        int currentVotes=-1;
        while(rs.next()){
            currentVotes = rs.getInt("VOTES");
        }
        // Check if use canVote
        if(!canVote(voterID)){
            return;
        }
        // Save vote and revoke vote right
        int newVotes = currentVotes+1;
        PreparedStatement pstmtC = null;
        PreparedStatement pstmtV = null;
        try{
            conn.setAutoCommit(false);
            q = "UPDATE CANDIDATE SET VOTES=? WHERE ID = ? ";
            pstmtC  = conn.prepareStatement(q);
            pstmtC.setInt(1, newVotes);
            pstmtC.setInt(2, candidateID);
            int updateVotes = pstmtC.executeUpdate();
            q = "UPDATE VOTER SET HASVOTED=1 WHERE ID = ?";
            pstmtV = conn.prepareStatement(q);
            pstmtV.setInt(1,voterID);
            int updateVoterRight = pstmtV.executeUpdate();
            System.out.println("A vote for "+candidateID+" was casted with status "+updateVotes);
            System.out.println("Voter "+voterID+" voted with status "+updateVoterRight);
            conn.commit();
        }catch (SQLException se){
            System.out.println("It's dead Jim!! An SQLException killed it!");
            se.printStackTrace();
            if (conn != null) {
                try {
                    System.err.print("Transaction is being rolled-back");
                    conn.rollback();
                } catch(SQLException excep) {
                    System.out.println("We are in big trouble. Last transaction can't be rolled-back!");
                }
            }
        }finally{
//            pstmtC.close();
//            pstmtV.close();
            conn.setAutoCommit(true);
        }
        
    }
} // ElectionController