package serverConnectorTests;

import partib.groupProject.probableCauses.backend.controller.ServerConnector;
import partib.groupProject.probableCauses.backend.controller.InvalidCallException;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class TestServerConnector{

	public void testDeprSingleQuery(){
		
		System.out.println("Testing deprecated single query call method.");

		boolean threwException = false;
		String testQuery = "DROP *;";
		try{
			ServerConnector.singleQueryCaller(testQuery);
		} catch (InvalidCallException inv) {
			threwException = true;
		}
		assert threwException : "Failed test: Did not throw Exception when calling invalidated method singleQueryCaller(String).";
	}

	public void testDeprMultQuery(){
		
		System.out.println("Testing deprecated multiple query call method.");

		boolean threwException = false;
		String testQuery = "DROP *;";
		List<String> testQueries= new ArrayList();
		testQueries.add(testQuery);
		try{
			ServerConnector.queryCaller(testQueries);
		} catch (InvalidCallException inv) {
			threwException = true;
		}
		assert threwException : "Failed test: Did not throw Exception when calling invalidated method queryCaller(List<String>).";
	}

	public void testRejectNoQueries(){
		
		System.out.println("Testing whether rejecting empty queries fails.");
		
		/* Tests whether the class rejects the follwoing scenarios: 
			empty list given, 
			only empty strings given,
			list with only spaces given, 
			list with only tabs given, 
			list with only newlines given, 
			tests with mixed whitespace given.
		*/
		
		boolean threwExceptionsOnAll = true;

		List<String> emptyList=new ArrayList();
		try{
			threwExceptionsOnAll = false;
			ServerConnector.queryCaller("foo.bdb", emptyList);
		} catch (InvalidCallException inv) {
			threwExceptionsOnAll = true;
		}

		assert threwExceptionsOnAll : "Did not throw Exception on empty list.";

		List<String> onlyEmpty = Arrays.asList("", "", "");
		try{
			threwExceptionsOnAll = false;
			ServerConnector.queryCaller("foo.bdb", onlyEmpty);
		} catch (InvalidCallException inv) {
			threwExceptionsOnAll = true;
		}

		assert threwExceptionsOnAll : "Did not throw Exception on list of empty Strings.";

		List<String> onlySpaces = Arrays.asList("  ", "   ", " ");
		try{
			threwExceptionsOnAll = false;
			ServerConnector.queryCaller("foo.bdb", onlySpaces);
		} catch (InvalidCallException inv) {
			threwExceptionsOnAll = true;
		}

		assert threwExceptionsOnAll : "Did not throw Exception on list of space Strings.";

		List<String> onlyTabs = Arrays.asList("\t\t\t", "\t", "\t\t");
		try{
			threwExceptionsOnAll = false;
			ServerConnector.queryCaller("foo.bdb", onlyTabs);
		} catch (InvalidCallException inv) {
			threwExceptionsOnAll = true;
		}

		assert threwExceptionsOnAll : "Did not throw Exception on list of tab Strings.";

		List<String> onlyNewline = Arrays.asList("\n\n\n", "\n", "\n\n");
		try{
			threwExceptionsOnAll = false;
			ServerConnector.queryCaller("foo.bdb", onlyNewline);
		} catch (InvalidCallException inv) {
			threwExceptionsOnAll = true;
		}

		assert threwExceptionsOnAll : "Did not throw Exception on list of newline Strings.";

		List<String> mixedWhiteSpace = Arrays.asList(" \t\n \n\n", "  \t\t\t \n","     \t");
		try{
			threwExceptionsOnAll = false;
			ServerConnector.queryCaller("foo.bdb", mixedWhiteSpace);
		} catch (InvalidCallException inv) {
			threwExceptionsOnAll = true;
		}

		assert threwExceptionsOnAll : "Did not throw Exception on list of whitespace Strings.";

	}

	public void testConnection(){
		
		System.out.println("Testing that a connection works.");
		String res = "";
		String expected = "[[{\"y\": 2, \"x\": 1, \"name\": \"A\"}, {\"y\": 4, \"x\": 3, \"name\": \"B\"}]]";
		List<String> queries = Arrays.asList("SQL CREATE TABLE t (name TEXT, x INT, y INT);", "	SQL INSERT INTO t VALUES(\"A\", 1, 2);", "SQL INSERT INTO t VALUES(\"B\", 3, 4);", "SELECT * FROM t;", "DROP TABLE t;");
		try {
			res = ServerConnector.queryCaller("foo.bdb", queries);
		} catch (Exception e) {
			e.printStackTrace();
			assert false : "Caught Exception in method execution.";
		}
		assert res.equals(expected) : "Got unexpected result from server!";
	}
}
