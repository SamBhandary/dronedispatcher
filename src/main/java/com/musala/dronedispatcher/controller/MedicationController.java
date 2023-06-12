package com.musala.dronedispatcher.controller;

import com.musala.dronedispatcher.entity.MedicationEntity;
import com.musala.dronedispatcher.services.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/medication")
public class MedicationController {
	@Autowired
	private MedicationService medicationService;

	@GetMapping("/list")
	public ResponseEntity<List<MedicationEntity>> listMedication() {
		return ResponseEntity.ok(medicationService.listAllMedications());
	}

}
