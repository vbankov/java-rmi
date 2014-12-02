// a servant class
import java.rmi.*;
import java.rmi.server.*;
import java.sql.*;
import java.util.*;
public class CalculatorImpl extends UnicastRemoteObject implements Calculator {
	public CalculatorImpl() throws RemoteException {
		super(); 
	}
        @Override
        public List<Candidate> getCandidates() throws RemoteException, SQLException  {
            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/DBElection", "dsws", "dsws");
            String q = "SELECT * FROM CANDIDATE";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(q);
            List<Candidate> cands = new ArrayList<Candidate>();
            while (rs.next()) {  
                String cName = rs.getString("NAME");
                int i = rs.getInt("ID");
                int votes = rs.getInt("VOTES");
                Candidate tempCand = new Candidate();
                tempCand.id = i;
                tempCand.name = cName;
                tempCand.votes = votes;
                cands.add(tempCand);
            }
            conn.close();
            return cands;
        }
        // PREDEFINED EXAMPLES
        @Override
	public long add (long a, long b) throws RemoteException {
		return a + b; 
	}
        @Override
	public long sub (long a, long b) throws RemoteException {
		return a - b; 
	}
        @Override
	public long mul (long a, long b) throws RemoteException {
		return a * b; 
	}
        @Override
	public long div (long a, long b) throws RemoteException {
		return a / b; 
	}
} // CalculatorImpl
