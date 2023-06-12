package com.musala.dronedispatcher.services;

import com.musala.dronedispatcher.entity.MedicationEntity;

import java.util.List;

public interface MedicationService {

	List<MedicationEntity> listAllMedications();
}
