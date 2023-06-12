package com.musala.dronedispatcher.services;

import com.musala.dronedispatcher.entity.DroneEntity;
import com.musala.dronedispatcher.model.DroneRegistration;

public interface DroneService {

	DroneEntity registerDrone(DroneRegistration drone);

}
