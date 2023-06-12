package com.musala.dronedispatcher.services;

import com.musala.dronedispatcher.entity.DroneEntity;
import com.musala.dronedispatcher.entity.MedicationEntity;
import com.musala.dronedispatcher.enums.DroneState;

import java.util.List;

public interface DispatchService {

	String loadMedicationsInADrone(String droneSerialNumber, List<String> medicineCodes);

	List<MedicationEntity> checkMedicationsForADrone(String droneSerialNumber);

	List<DroneEntity> checkAvailableDroneForLoading();

	Integer getBatteryLevelForDrone(String droneSerialNumber);

	String updateDroneState(String serialNumber, int batteryPercentage, DroneState droneState);




}
