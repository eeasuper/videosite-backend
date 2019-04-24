package com.nano.videosite.exceptions;

public class ElementNotFoundException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ElementNotFoundException(){
		super("could not find element");
	}
}
