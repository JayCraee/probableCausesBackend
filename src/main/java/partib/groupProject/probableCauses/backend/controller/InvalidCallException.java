package partib.groupProject.probableCauses.backend.controller;

/***
 * Exception class that handles any invalid calls of BQL query.
 *
 */
public class InvalidCallException extends Exception{
	public InvalidCallException(String msg){
		super(msg);
	}
}
