import java.rmi.*;
import java.net.*;
import java.sql.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import com.google.common.collect.Lists;
public class ElectionClient {	
        public static void printCandidates(Election c) throws SQLException, RemoteException{
                try{
                    List<Election.Candidate> cands = new ArrayList<Election.Candidate>();
                    cands = c.getCandidates();
                    int candsNumber = cands.size();
                    System.out.println("Got "+candsNumber+" candidates:");
                    for(int i=0; i<candsNumber;i++){
                        String t = "\t"+cands.get(i).id+"\t"+cands.get(i).name+"\t votes: "+cands.get(i).votes;
                        System.out.println(t);
                    }
                }catch (SQLException se){
                   System.out.println("BOOOM ! SQLException");
                   se.printStackTrace();
                }
		catch (RemoteException re) {
			System.out.println("BOOOM ! Remote Exception");
                        re.printStackTrace();
		}
        }
	public static void main(String[] args) throws MalformedURLException {	
		try {	
			System.setSecurityManager (new MyRMISecurityManager()); // Lookup a remote reference to a remote object of Calculator type 
                        Registry registry = LocateRegistry.getRegistry("localhost", 18300);
			String name = "rmi://localhost/ElectionService";
			Election c = (Election) registry.lookup(name);
			try{
                            printCandidates(c);
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
} // ElectionClient
