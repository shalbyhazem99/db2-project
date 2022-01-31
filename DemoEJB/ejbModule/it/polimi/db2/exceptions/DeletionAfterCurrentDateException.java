package it.polimi.db2.exceptions;

public class DeletionAfterCurrentDateException extends Exception {
	private static final long serialVersionUID = 1L;

	public DeletionAfterCurrentDateException(String message) {
		super(message);
	}
}
