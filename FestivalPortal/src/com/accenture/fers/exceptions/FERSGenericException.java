package com.accenture.fers.exceptions;

import org.apache.log4j.Logger;

/**
 * Custom Exception class for overriding SQL and CLASSNOTFOUND exceptions
 */
@SuppressWarnings("serial")
public class FERSGenericException extends Exception {

	//LOGGER to handle custom exceptions
	private static final Logger LOGGER = Logger.getLogger(FERSGenericException.class);

	public FERSGenericException(String message, Throwable object){
		super(message,object);
		LOGGER.info("Exception Message is :"+message);
	}

}
