import java.rmi.*;
import java.net.*;
import java.sql.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
public class CalculatorClient {	
	public static void main(String[] args) throws MalformedURLException {	
		try {	
			System.setSecurityManager (new MyRMISecurityManager());
			// Lookup a remote reference to a remote object of Calculator type 
                        Registry registry = LocateRegistry.getRegistry("localhost", 18300);
			String name = "rmi://localhost/CalculatorService";
			Calculator c = (Calculator) registry.lookup(name);
			System.out.println ("Checking db state: ");
                        try{
                            List<Calculator.Candidate> cands = new ArrayList<Calculator.Candidate>();
                            cands = c.getCandidates();
                            System.out.println("ok");
                        }catch (SQLException se){
                           System.out.println("BOOOM ! SQLException");
                           se.printStackTrace();
                        }
		}
		catch (RemoteException re) {
			System.out.println("BOOOM ! Remote Exception");
                        re.printStackTrace();
		}
		catch (NotBoundException e) {
			System.out.println("BOOOM ! NotBoundException exception");
                        e.printStackTrace();
		}
	} // main
} // CalculatorClient
