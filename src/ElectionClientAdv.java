import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.Scanner;
@Deprecated
public class ElectionClientAdv {	
        @Deprecated
	public static void main(String[] args) throws MalformedURLException {	
		try {	
			System.setSecurityManager(new MyRMISecurityManager());
                        Registry registry = LocateRegistry.getRegistry("localhost", 18300);
			// Lookup a remote reference to a remote object of Calculator type 
			String name = "rmi://localhost/CalculatorService";
			Election c = (Election) registry.lookup(name);
                        // Calculator c = (Calculator) Naming.lookup(name);
						
			calculateProcess(c);
		}
		catch (RemoteException | NotBoundException e) {
			System.out.println("Other exception");
		}
	} // main
	@Deprecated
	static void calculateProcess(Election c) {
		int x, y;
		char op, choice;
		try {
			Scanner input = new Scanner(System.in);
			do {
				System.out.print("Select operation '+', '-', '*', '/': ");
				op = input.next().charAt(0);
				System.out.println();
				System.out.print("Input 2 integer numbers: ");
				x = input.nextInt();
				y = input.nextInt();
				System.out.println();
				switch (op) {
					case '+' : 
						System.out.println(x + " " + op + " " + y + " = " + c.add(x, y));
						break;
					case '-' : 
						System.out.println(x + " " + op + " " + y + " = " + c.sub(x, y));
						break;
					case '*' : 
						System.out.println(x + " " + op + " " + y + " = " + c.mul(x, y));
						break;
					case '/' : 
						System.out.println(x + " " + op + " " + y + " = " + c.div(x, y));
						break;
					default:
						System.out.println("Unknown operation");
				}
				System.out.print("Continue (y/n)? : ");	
				choice = input.next().charAt(0);
				System.out.println();
			} while (choice != 'n' && choice != 'N');
		}
		catch (RemoteException re) {
			System.out.println("Remote Exception");
		}
		catch (InputMismatchException e) {
			System.out.println("Input mismatch exception");
		}
		catch (Exception e) {
			System.out.println("Other exception");
		}
	} // calculateProcess
} // CalculatorClient
