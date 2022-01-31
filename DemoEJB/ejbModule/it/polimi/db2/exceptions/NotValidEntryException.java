package it.polimi.db2.exceptions;

public class NotValidEntryException extends Exception {
	private static final long serialVersionUID = 1L;

	public NotValidEntryException(String message) {
		super(message);
	}
}
