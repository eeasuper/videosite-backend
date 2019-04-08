package com.nano.videosite.exceptions;

public class ElementNotFoundException extends RuntimeException{
	
	public ElementNotFoundException(){
		super("could not find element");
	}
}
