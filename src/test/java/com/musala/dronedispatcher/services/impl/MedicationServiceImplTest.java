package com.musala.dronedispatcher.services.impl;

import com.musala.dronedispatcher.entity.MedicationEntity;
import com.musala.dronedispatcher.repository.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class MedicationServiceImplTest {
	@InjectMocks
	MedicationServiceImpl medicationService;

	@Mock
	MedicationRepository medicationRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void listAllMedications() {
		List<MedicationEntity> medications = new ArrayList<>();
		MedicationEntity entity = new MedicationEntity();
		entity.setName("med_1");
		entity.setCode("AA1");
		entity.setWeight(50);
		entity.setImage("img.png");
		medications.add(entity);
		when(medicationRepository.findAll()).thenReturn(medications);
		List<MedicationEntity> results = medicationService.listAllMedications();
		assertEquals(1, results.size());

	}
}