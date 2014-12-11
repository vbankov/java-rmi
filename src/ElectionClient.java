import java.rmi.*;
import java.net.*;
import java.sql.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class ElectionClient {
    private static void printCandidates(Election election) throws SQLException, RemoteException{
        List<Election.Candidate> cands = null;
        cands = election.getCandidates();
        System.out.println("Got "+cands.size()+" candidates:");
        for(int i=0; i<cands.size(); i++){
            String t = "\t"+cands.get(i).id+"\t"+cands.get(i).name+"\t";
            System.out.println(t);
        }
    }
    public static Election initRmiElection() throws RemoteException, NotBoundException{
        System.setSecurityManager (new MyRMISecurityManager()); // Lookup a remote reference to a remote object of Calculator type 
        Registry registry = LocateRegistry.getRegistry("localhost", 18300);
        String name = "rmi://localhost/ElectionService";
        Election c = (Election) registry.lookup(name);
        return c;
    }
    private static int voterID;
    public static void main(String[] args) throws MalformedURLException, NotBoundException {
        try {	
            Election c = initRmiElection();
            try{
                Scanner in = new Scanner(System.in);
                String inputHolder;
                System.out.print("Enter your userID: ");
                int s = in.nextInt(); // FIX THIS TO STRING
                System.out.print("Enter your password: ");
                inputHolder = in.next();
                int loggedIn = c.login(s);
                while(loggedIn==0){
                    System.out.print("\nInvalid userID. Enter userID: ");
                    s = in.nextInt();
                    loggedIn = c.login(s);
                }
                voterID = s;
                boolean voteRight = c.canVote(voterID);
                if(voteRight==false){
                    System.out.println("\nWARNING: It seems that you have already voted.\n");
                }
                System.out.println("You are logged-in as "+voterID+"\nOptions: \n[1] Candidates List \t[2] Candidate Result \t[3] Vote \t[0] Logout");
                s = in.nextInt();
                while(s!=0){ // Exit when 0 is entered                               
                    if(s==1){   // Print candidates when 1 is entered 
                        printCandidates(c);   
                    }else if(s==2){ // Get candidate results when 2 is entered
                        System.out.print("Insert Candidate id: ");
                        s = in.nextInt();
                        Election.ElectionResult er = null;
                        er = c.getResults(s);
                        System.out.println("\nResults:\b\t ID: "+er.candidateId+" \tName:"+er.name+"\tVotes: "+er.votes+"\n");
                    }else if(s==3){
                        if(voteRight==false){
                            System.out.println("You have already voted.");
                        }else{
                            System.out.print("[VOTE] Insert candidateID: ");
                            s = in.nextInt();
                            c.vote(voterID,s);
                            voteRight = false;
                            System.out.println("Your vote was casted.");
                        }
                    }
                    System.out.println("Options:\n[1] Candidates List \t[2] Candidate Result \t[3] Vote \t[0] Logout");
                    s = in.nextInt();
                }
                if(s==0){
                    c.logout(voterID);
                    System.out.println("Bye!");
                }
            }catch (SQLException se){
                System.out.println("BOOOM ! SQLException");
                se.printStackTrace();
            }
        }catch (RemoteException r){
            System.out.println("BOOOM ! RemoteException");
            System.out.println("You should check if server is running.");
            r.printStackTrace();
        }
    } // main
} // ElectionClient
