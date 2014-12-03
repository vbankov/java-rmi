import java.rmi.*;
import java.sql.*;
import java.util.List;
import java.io.Serializable;
public interface Election extends Remote {
    public class Candidate implements Serializable{
        public int id;
        public String name;
        public int votes;
    }
    public List<Candidate> getCandidates() throws RemoteException, SQLException;
    // PREDEFINED EXAMPLES
    public long add (long a, long b) throws RemoteException;
    public long sub (long a, long b) throws RemoteException;
    public long mul (long a, long b) throws RemoteException;
    public long div (long a, long b) throws RemoteException;
} // Calculator
