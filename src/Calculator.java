import java.rmi.*;
import java.sql.*;
import java.util.List;
public interface Calculator extends Remote {
    public class Candidate{
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
