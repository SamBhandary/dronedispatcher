package com.musala.dronedispatcher.services.impl;

import com.musala.dronedispatcher.entity.DroneEntity;
import com.musala.dronedispatcher.entity.MedicationEntity;
import com.musala.dronedispatcher.enums.DroneState;
import com.musala.dronedispatcher.exceptions.DroneDispatcherException;
import com.musala.dronedispatcher.repository.DroneRepository;
import com.musala.dronedispatcher.repository.MedicationRepository;
import com.musala.dronedispatcher.services.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class DispatchServiceImpl implements DispatchService {

	@Autowired
	private DroneRepository droneRepository;

	@Autowired
	private MedicationRepository medicationRepository;

	@Override
	public String loadMedicationsInADrone(String droneSerialNumber, List<String> medicineCodes) {
		DroneEntity drone = droneRepository.findById(droneSerialNumber).orElseThrow(
				() -> DroneDispatcherException.raiseException("Invalid Drone Serial Number"));
		if (!Arrays.asList(DroneState.IDLE, DroneState.LOADING).contains(drone.getState())) {
			throw DroneDispatcherException.raiseException(
					"Invalid Drone state, drone must be in Idle or Loading state to load Medication");
		}
		//Prevent Drone being load if battery percent is below 25 percentage
		if (drone.getBatteryCapacity() < 25) {
			throw DroneDispatcherException.raiseException("Drone Battery less then 25 Percentage");
		}
		int medicationWeight = 0;
		if (drone.getState() == DroneState.LOADING) {
			for (MedicationEntity medication : drone.getMedications()) {
				medicationWeight += medication.getWeight();
			}
		}
		for (String medicineCode : medicineCodes) {
			MedicationEntity medication = medicationRepository.findById(medicineCode).orElseThrow(
					() -> DroneDispatcherException.raiseException(
							"Invalid Medication code: " + medicineCode));
			medicationWeight += medication.getWeight();
			//prevents drone being loaded more then it's weightLimit
			if (drone.getWeightLimit().intValue() < medicationWeight) {
				throw DroneDispatcherException.raiseException(
						"Medication weight exceeds drone limit");
			}
			if (drone.getMedications() != null) {
				drone.getMedications().add(medication);
			} else {
				List<MedicationEntity> medications = new ArrayList<>();
				medications.add(medication);
				drone.setMedications(medications);
			}
		}
		if (medicationWeight == drone.getWeightLimit().intValue()) {
			drone.setState(DroneState.LOADED);
		} else {
			drone.setState(DroneState.LOADING);
		}
		droneRepository.save(drone);
		return "Medicine loaded successfully";
	}

	@Override
	public List<MedicationEntity> checkMedicationsForADrone(String droneSerialNumber) {
		DroneEntity drone = droneRepository.findById(droneSerialNumber).orElseThrow(
				() -> DroneDispatcherException.raiseException("Invalid Drone Serial Number"));
		return drone.getMedications();
	}

	@Override
	public List<DroneEntity> checkAvailableDroneForLoading() {
		List<DroneEntity> idleDrones = droneRepository.findByState(DroneState.IDLE);
		List<DroneEntity> loadingDrones = droneRepository.findByState(DroneState.LOADING);
		List<DroneEntity> droneEntities = new ArrayList<>();
		droneEntities.addAll(idleDrones);
		droneEntities.addAll(loadingDrones);
		return droneEntities;
	}

	@Override
	public Integer getBatteryLevelForDrone(String droneSerialNumber) {
		DroneEntity drone = droneRepository.findById(droneSerialNumber).orElseThrow(
				() -> DroneDispatcherException.raiseException("Invalid Drone Serial Number"));
		return drone.getBatteryCapacity();
	}

	@Override
	public String updateDroneState(String droneSerialNumber, int batteryPercentage,
			DroneState droneState) {
		DroneEntity drone = droneRepository.findById(droneSerialNumber).orElseThrow(
				() -> DroneDispatcherException.raiseException("Invalid Drone Serial Number"));
		switch (droneState) {
		case IDLE:
			if (drone.getMedications() != null) {
				drone.getMedications().clear();
			}
			if (!Arrays.asList(DroneState.IDLE, DroneState.LOADING, DroneState.LOADED,
					DroneState.RETURNING).contains(drone.getState())) {
				throw DroneDispatcherException.raiseException(
						"Only idle, loading, loaded and returning drones can be set to Idle");
			}
			break;
		case LOADED:
			if (!drone.getState().equals(DroneState.LOADING)) {
				throw DroneDispatcherException.raiseException(
						"Only loading drones can be set to Loaded");
			}
			break;
		case DELIVERING:
			if (!drone.getState().equals(DroneState.LOADED)) {
				throw DroneDispatcherException.raiseException(
						"Only loaded drones can be set to delivering");
			}
			break;
		case DELIVERED:
			if (!drone.getState().equals(DroneState.DELIVERING)) {
				throw DroneDispatcherException.raiseException(
						"Only delivering drones can be set to delivered");
			}
			if (drone.getMedications() != null) {
				drone.getMedications().clear();
			}
			break;
		case RETURNING:
			if (!drone.getState().equals(DroneState.DELIVERED)) {
				throw DroneDispatcherException.raiseException(
						"Only delivered drones can be set to returning");
			}
			break;
		default:
			throw DroneDispatcherException.raiseException("Invalid Drone status");
		}
		drone.setState(droneState);
		drone.setBatteryCapacity(batteryPercentage);
		droneRepository.save(drone);
		return "Drone updated";
	}
}
