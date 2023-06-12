package com.musala.dronedispatcher.controller;

import com.musala.dronedispatcher.entity.DroneEntity;
import com.musala.dronedispatcher.model.DroneRegistration;
import com.musala.dronedispatcher.services.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/drone")
public class DroneController {

	@Autowired
	private DroneService droneService;

	@PostMapping("/register")
	public ResponseEntity<DroneEntity> register(
			@Validated
			@RequestBody
			DroneRegistration drone) {
		return ResponseEntity.ok(droneService.registerDrone(drone));
	}

}
