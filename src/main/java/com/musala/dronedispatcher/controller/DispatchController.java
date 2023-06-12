package com.musala.dronedispatcher.controller;

import com.musala.dronedispatcher.entity.DroneEntity;
import com.musala.dronedispatcher.entity.MedicationEntity;
import com.musala.dronedispatcher.enums.DroneState;
import com.musala.dronedispatcher.services.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dispatch")
public class DispatchController {

	@Autowired
	DispatchService dispatchService;

	@PostMapping("/load/{serialNumber}")
	public ResponseEntity<String> loadMedicineOnDrone(
			@PathVariable("serialNumber")
			String serialNumber,
			@RequestBody
			List<String> medicineCodes) {
		return ResponseEntity.ok(
				dispatchService.loadMedicationsInADrone(serialNumber, medicineCodes));
	}

	@GetMapping("/medications/{serialNumber}")
	public ResponseEntity<List<MedicationEntity>> checkMedicationsForADrone(
			@PathVariable("serialNumber")
			String serialNumber) {
		return ResponseEntity.ok(dispatchService.checkMedicationsForADrone(serialNumber));
	}

	@GetMapping("/drones/available")
	public ResponseEntity<List<DroneEntity>> checkAvailableDroneForLoading() {
		return ResponseEntity.ok(dispatchService.checkAvailableDroneForLoading());
	}

	@GetMapping("/drone/battery/{serialNumber}")
	public ResponseEntity<Integer> getBatteryLevelForDrone(
			@PathVariable("serialNumber")
			String serialNumber) {
		return ResponseEntity.ok(dispatchService.getBatteryLevelForDrone(serialNumber));
	}

	/**
	 * Drone States ideal FLOW :: IDLE->LOADING->LOADED->DELIVERING->DELIVERED->RETURNING->IDLE
	 * Battery Percent must be passed When changing state to Every State.
	 */
	@PostMapping("/drone/update/state/{serialNumber}")
	public ResponseEntity<String> updateDroneState(
			@PathVariable("serialNumber") String serialNumber,
			@RequestParam int batteryPercantage, @RequestParam DroneState droneState) {
		return ResponseEntity.ok(dispatchService.updateDroneState(serialNumber, batteryPercantage, droneState));
	}

}
