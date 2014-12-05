import java.rmi.*;
import java.sql.*;
import java.util.List;
import java.io.Serializable;
public interface Election extends Remote {
    public class Candidate implements Serializable{
        public int id;
        public String name;
    }
    public class ElectionResult implements Serializable{
        public int candidateId;
        public String name;
        public int votes;
    }
    public class Voter implements Serializable{
        public int id;
        public boolean hasVoted;
    }
    public List<Candidate> getCandidates() throws RemoteException, SQLException;
    public ElectionResult getResults(int id) throws RemoteException, SQLException;
    public int login(int voterID) throws RemoteException, SQLException;
    public void logout(int voterID) throws RemoteException;
    public boolean validateVoteRight(int voterID) throws RemoteException, SQLException;
    public void vote(int voterID, int candidateID) throws RemoteException, SQLException;
} // Election