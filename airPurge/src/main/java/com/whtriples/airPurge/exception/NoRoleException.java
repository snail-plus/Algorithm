package com.whtriples.airPurge.exception;

import org.apache.shiro.authc.ConcurrentAccessException;

public class NoRoleException extends ConcurrentAccessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoRoleException() {
		super();
	}

	public NoRoleException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoRoleException(String message) {
		super(message);
	}

	public NoRoleException(Throwable cause) {

		super(cause);

	}
}
