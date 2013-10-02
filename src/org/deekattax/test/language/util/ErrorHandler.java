package org.deekattax.test.language.util;

public class ErrorHandler {

	public static String handle(String message) {
		System.err.println(message);
		return message;
	}
}
