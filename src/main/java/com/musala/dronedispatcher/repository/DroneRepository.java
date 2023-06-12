package com.musala.dronedispatcher.repository;

import com.musala.dronedispatcher.entity.DroneEntity;
import com.musala.dronedispatcher.enums.DroneState;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DroneRepository extends CrudRepository<DroneEntity, String> {

	List<DroneEntity> findByState(DroneState state);
}
