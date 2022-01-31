package it.polimi.db2.exceptions;

public class NonActiveUserException extends Exception {
	private static final long serialVersionUID = 1L;

	public NonActiveUserException(String message) {
		super(message);
	}
}
