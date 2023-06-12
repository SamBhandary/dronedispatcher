package com.musala.dronedispatcher.repository;

import com.musala.dronedispatcher.entity.DroneEntity;
import org.springframework.data.repository.CrudRepository;

public interface DroneRepository extends CrudRepository<DroneEntity, String> {
}
