package com.musala.dronedispatcher.exceptions;

public class DroneDispatcherException extends RuntimeException {

	private final String message;

	public  DroneDispatcherException( String message){
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public static DroneDispatcherException raiseException(String message){
		return new DroneDispatcherException(message);
	}


}
