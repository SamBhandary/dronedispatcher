package com.musala.dronedispatcher.services.impl;

import com.musala.dronedispatcher.entity.DroneEntity;
import com.musala.dronedispatcher.enums.DroneState;
import com.musala.dronedispatcher.exceptions.DroneDispatcherException;
import com.musala.dronedispatcher.model.DroneRegistration;
import com.musala.dronedispatcher.repository.DroneRepository;
import com.musala.dronedispatcher.services.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DroneServiceImpl implements DroneService {

	private Integer FLEET_LIMIT = 10;

	@Autowired
	private DroneRepository droneRepository;

	@Override
	public DroneEntity registerDrone(DroneRegistration drone) {
		if (droneRepository.count() == FLEET_LIMIT) {
			throw DroneDispatcherException.raiseException("Drone fleet size exceeded");
		}
		DroneEntity droneEntity = new DroneEntity(drone.getSerialNumber(), drone.getModel(),
				drone.getWeightLimit(), drone.getBatteryCapacity(), DroneState.IDLE);
		DroneEntity entity = droneRepository.save(droneEntity);
		return entity;
	}

}
