package com.musala.dronedispatcher.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DroneDispatcherExceptionHandler {

	@ExceptionHandler(DroneDispatcherException.class)
	public ResponseEntity<ErrorResponse> handleDroneDispatcherException(DroneDispatcherException e){
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(e.getMessage()));
	}


}
