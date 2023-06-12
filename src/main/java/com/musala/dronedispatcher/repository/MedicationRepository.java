package com.musala.dronedispatcher.repository;

import com.musala.dronedispatcher.entity.MedicationEntity;
import org.springframework.data.repository.CrudRepository;

public interface MedicationRepository extends CrudRepository<MedicationEntity, String> {
}
