// a servant class
import java.rmi.*;
import java.rmi.server.*;
import java.sql.*;
import java.util.*;
public class ElectionController extends UnicastRemoteObject implements Election {
    private static List<Integer> loggedInIDs = new ArrayList<Integer>();
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
    };
    @Override
    public void logout(int voterID) throws RemoteException{
        if(this.loggedInIDs.contains(voterID)) {this.loggedInIDs.remove((Integer)voterID);}
        System.out.println("[Info]\tUser "+voterID+" has disconnected");
    }
    @Override
    public boolean validateVoteRight(int voterID) throws RemoteException, SQLException{
        String q = "SELECT HASVOTED FROM VOTER WHERE ID="+voterID;
        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DBElection", "dsws", "dsws");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(q);
        int hasVoted = 1;   // return NOvote if voter not in db
        while(rs.next()){
            hasVoted = rs.getInt("HASVOTED");
        }
        return (hasVoted==0);
    }
    @Override
    public void vote(int voterID, int candidateID) throws RemoteException, SQLException{
        String q = "SELECT VOTES FROM CANDIDATE WHERE ID="+candidateID;
        Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DBElection", "dsws", "dsws");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(q);
        int currentVotes=-1;
        while(rs.next()){
            currentVotes = rs.getInt("VOTES");
        }
        int newVotes = currentVotes+1;
        q = "UPDATE CANDIDATE SET VOTES="+newVotes+" WHERE ID="+candidateID;
        String q2 = "UPDATE VOTER SET HASVOTED=1 WHERE ID="+voterID; 
        int ex1 = stmt.executeUpdate(q2);     // SYNCHRONIZE THESE
        int ex2 = stmt.executeUpdate(q);      // SYNCHRONIZE THESE
    }
} // ElectionController