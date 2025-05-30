package com.notification.system.exception;

public class OrderNotFoundException extends RuntimeException {
	public OrderNotFoundException ( String message ) {
		super ( message );
	}
}
