import java.rmi.*;
import java.net.*;
import java.sql.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
public class ElectionClient {	
        public static void printCandidates(Election c) throws SQLException, RemoteException{
                try{
                    List<Election.Candidate> cands = null;
                    cands = c.getCandidates();
                    int candsNumber = cands.size();
                    System.out.println("Got "+candsNumber+" candidates:");
                    for(int i=0; i<candsNumber;i++){
                        String t = "\t"+cands.get(i).id+"\t"+cands.get(i).name+"\t";
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
        };
	public static Election initRmiElection(){
            try{
                System.setSecurityManager (new MyRMISecurityManager()); // Lookup a remote reference to a remote object of Calculator type 
                Registry registry = LocateRegistry.getRegistry("localhost", 18300);
                String name = "rmi://localhost/ElectionService";
                Election c = (Election) registry.lookup(name);
                return c;
            }catch (RemoteException re) {
                    System.out.println("BOOOM ! Remote Exception");
                    re.printStackTrace();
            }catch (NotBoundException e) {
                    System.out.println("BOOOM ! NotBoundException exception");
                    e.printStackTrace();
            }
            return null;
        }
        public static void main(String[] args) throws MalformedURLException {	
		try {	
			Election c = initRmiElection();
			try{
                            Scanner in = new Scanner(System.in);
                            System.out.println("Use a number then press Enter. \n[1] Candidates List\t[0] Exit");
                            int s = in.nextInt();
                            while(s!=0){ // Exit when 0 is entered                               
                                if(s==1){   // Print candidates when 1 is entered 
                                    printCandidates(c);   
                                }else if(s==2){
                                    
                                }
                                System.out.println("Options:\t[1] Candidates List\t[0] Exit");
                                s = in.nextInt();
                            }
                        }catch (SQLException se){
                            System.out.println("BOOOM ! SQLException");
                            se.printStackTrace();
                        }
		}catch (RemoteException r){
                    System.out.println("BOOOM ! RemoteException");
                    r.printStackTrace();
                }
	} // main
} // ElectionClient
