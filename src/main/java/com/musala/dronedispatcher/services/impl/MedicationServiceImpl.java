package com.musala.dronedispatcher.services.impl;

import com.musala.dronedispatcher.entity.MedicationEntity;
import com.musala.dronedispatcher.repository.MedicationRepository;
import com.musala.dronedispatcher.services.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationServiceImpl implements MedicationService {

	@Autowired
	private MedicationRepository medicationRepository;

	@Override
	public List<MedicationEntity> listAllMedications() {
		return (List<MedicationEntity>) medicationRepository.findAll();
	}
}
