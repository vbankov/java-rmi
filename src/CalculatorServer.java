import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
public class CalculatorServer {
	public static void main (String args[]) {
		// set up a security manager that can handle remote stubs 
		System.setSecurityManager (new MyRMISecurityManager());
		try {
                        Registry registry = LocateRegistry.createRegistry(18300);
			// bind an instance of RMICalculatorImpl class in Registry 
			String name = "rmi://localhost/CalculatorService";
			Calculator c = new CalculatorImpl();
			registry.rebind(name, c);
                        // Naming.rebind(name, c);
			System.out.println("CalculatorService started ...");
		}
		catch (Exception e) { 
			System.out.println("Exception caught " + "during: " + e);
		}
	} // main
} // CalculatorServer
