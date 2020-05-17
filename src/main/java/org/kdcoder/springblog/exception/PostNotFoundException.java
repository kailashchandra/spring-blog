package org.kdcoder.springblog.exception;

public class PostNotFoundException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PostNotFoundException(String messages) {
		super(messages);
	}
}
