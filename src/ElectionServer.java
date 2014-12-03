import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
public class ElectionServer {
	public static void main (String args[]) {
		// set up a security manager that can handle remote stubs 
		System.setSecurityManager (new MyRMISecurityManager());
		try {
                        Registry registry = LocateRegistry.createRegistry(18300); // bind an instance of RMIElection class in Registry 
			String name = "rmi://localhost/ElectionService";
			Election c = new ElectionController();
			registry.rebind(name, c);   // Naming.rebind(name, c);
			System.out.println("Magic Happens on port 18300 \n\tElection service was started.");
		}
		catch (Exception e) { 
			System.out.println("Exception caught " + "during: " + e);
		}
	} // main
} // ElectionServer
