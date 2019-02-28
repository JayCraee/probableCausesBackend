package serverConnectorTests;

import java.lang.reflect.Method;

public class RunTests{
	
	public static void main(String[] args){
		// TestServerConnector.java
		TestServerConnector serverConn = new TestServerConnector();
		
		Class sc = serverConn.getClass();
		Method[] tests = sc.getMethods();
		for(Method test : tests){
			try{
				if (! test.getName().contains("test")) continue;
				System.out.println("======================== \n");
				test.invoke(serverConn);
				System.out.println((char)27 + "[32m" + "Passed Test: " + test.getName() + (char)27 +  "[37m\n");
			} catch(AssertionError a) {
				System.out.println((char)27 + "[31m" + "Failed Test: " + test.getName() + " -- " + a.getMessage() + (char)27 + "[37m\n");
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		serverConn.testRejectNoQueries();
	}

}
