import java.rmi.*;
public class TheRMISecurityManager extends RMISecurityManager {	
	public TheRMISecurityManager () { super(); }
        @Override
	public void checkAccept(String host, int port) {}
        @Override
	public void checkConnect(String host, int port) {}
        @Override
	public void checkConnect(String host, int port, Object executionContext) {}
        @Override
	public void checkListen(int port) {}
} // TheRMISecurityManager
