package com.musala.dronedispatcher.services.impl;

import com.musala.dronedispatcher.entity.DroneEntity;
import com.musala.dronedispatcher.entity.MedicationEntity;
import com.musala.dronedispatcher.enums.DroneModel;
import com.musala.dronedispatcher.enums.DroneState;
import com.musala.dronedispatcher.exceptions.DroneDispatcherException;
import com.musala.dronedispatcher.repository.DroneRepository;
import com.musala.dronedispatcher.repository.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class DispatchServiceImplTest {

	@InjectMocks
	DispatchServiceImpl dispatchService;

	@Mock
	private DroneRepository droneRepository;

	@Mock
	private MedicationRepository medicationRepository;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test()
	void loadMedicationsInADrone_InvalidDroneState() {
		DroneEntity drone = new DroneEntity("AA1", DroneModel.Cruiserweight, 500, 100,
				DroneState.LOADED);
		when(droneRepository.findById(anyString())).thenReturn(Optional.of(drone));
		try {
			dispatchService.loadMedicationsInADrone("AA1", Arrays.asList("med_1", "med_2"));
		} catch (DroneDispatcherException e) {
			assertEquals(e.getMessage(),
					"Invalid Drone state, drone must be in Idle or Loading state to load Medication");
		}
	}

	@Test()
	void loadMedicationsInADrone_BatteryLessThen25Percent() {
		DroneEntity drone = new DroneEntity("AA1", DroneModel.Cruiserweight, 500, 24,
				DroneState.IDLE);
		when(droneRepository.findById(anyString())).thenReturn(Optional.of(drone));
		try {
			dispatchService.loadMedicationsInADrone("AA1", Arrays.asList("med_1", "med_2"));
		} catch (DroneDispatcherException e) {
			assertEquals(e.getMessage(), "Drone Battery less then 25 Percentage");
		}
	}

	@Test()
	void loadMedicationsInADrone_ShouldExceedsWeightLimit() {
		DroneEntity drone = new DroneEntity("AA1", DroneModel.Cruiserweight, 100, 29,
				DroneState.IDLE);
		when(droneRepository.findById(anyString())).thenReturn(Optional.of(drone));
		MedicationEntity medication = new MedicationEntity();
		medication.setWeight(100);
		medication.setCode("med_*");
		medication.setImage("image.jpg");
		medication.setName("M*");
		when(medicationRepository.findById(anyString())).thenReturn(Optional.of(medication));
		try {
			dispatchService.loadMedicationsInADrone("AA1", Arrays.asList("med_1", "med_2"));
		} catch (DroneDispatcherException e) {
			assertEquals(e.getMessage(), "Medication weight exceeds drone limit");
		}
	}

	@Test()
	void loadMedicationsInADrone_SuccessfullLoad() {
		DroneEntity drone = new DroneEntity("AA1", DroneModel.Cruiserweight, 300, 29,
				DroneState.IDLE);
		when(droneRepository.findById(anyString())).thenReturn(Optional.of(drone));
		MedicationEntity medication = new MedicationEntity();
		medication.setWeight(100);
		medication.setCode("med_*");
		medication.setImage("image.jpg");
		medication.setName("M*");
		when(medicationRepository.findById(anyString())).thenReturn(Optional.of(medication));
		verify(droneRepository, atMost(1)).save(any());
		String resp = dispatchService.loadMedicationsInADrone("AA1",
				Arrays.asList("med_1", "med_2"));
		assertEquals(resp, "Medicine loaded successfully");

	}

	@Test
	void checkMedicationsForADrone_InvalidSerialNumber() {
		when(droneRepository.findById(anyString())).thenReturn(Optional.empty());
		try {
			dispatchService.checkMedicationsForADrone("AA1");
		} catch (DroneDispatcherException e) {
			assertEquals(e.getMessage(), "Invalid Drone Serial Number");
		}

	}

	@Test
	void checkMedicationsForADrone_Success() {
		DroneEntity drone = new DroneEntity("AA1", DroneModel.Cruiserweight, 500, 100,
				DroneState.LOADED);
		List<MedicationEntity> medicationEntityList = new ArrayList<>();

		MedicationEntity medication = new MedicationEntity();
		medication.setWeight(100);
		medication.setCode("med_1");
		medication.setImage("image.jpg");
		medication.setName("M1");

		medicationEntityList.add(medication);
		drone.setMedications(medicationEntityList);
		when(droneRepository.findById(anyString())).thenReturn(Optional.of(drone));
		List<MedicationEntity> medications = dispatchService.checkMedicationsForADrone("AA1");
		assertEquals(medications.size(), 1);
		assertEquals(medications.get(0).getName(), "M1");

	}

	@Test
	void checkAvailableDroneForLoading_Success() {
		List<DroneEntity> idleDrones = new ArrayList<>();
		DroneEntity idle1 = new DroneEntity("AA1", DroneModel.Cruiserweight, 500, 100,
				DroneState.IDLE);
		DroneEntity idle2 = new DroneEntity("AA2", DroneModel.Cruiserweight, 500, 100,
				DroneState.IDLE);
		idleDrones.add(idle1);
		idleDrones.add(idle2);

		List<DroneEntity> loadingDrones = new ArrayList<>();
		DroneEntity loading1 = new DroneEntity("AA3", DroneModel.Cruiserweight, 500, 100,
				DroneState.LOADING);
		DroneEntity loading2 = new DroneEntity("AA4", DroneModel.Cruiserweight, 500, 100,
				DroneState.LOADING);
		loadingDrones.add(loading1);
		loadingDrones.add(loading2);

		when(droneRepository.findByState(DroneState.IDLE)).thenReturn(idleDrones);
		when(droneRepository.findByState(DroneState.LOADING)).thenReturn(loadingDrones);
		List<DroneEntity> drones = dispatchService.checkAvailableDroneForLoading();
		assertEquals(drones.size(), 4);

	}

	@Test
	void getBatteryLevelForDrone_Success() {
		DroneEntity drone = new DroneEntity("AA1", DroneModel.Cruiserweight, 500, 79,
				DroneState.IDLE);
		when(droneRepository.findById("AA1")).thenReturn(Optional.of(drone));
		Integer battery = dispatchService.getBatteryLevelForDrone("AA1");
		assertEquals(battery, 79);

	}

	@Test
	void updateDroneStateToIdle_ErrorWhenInvalidState(){
		DroneEntity drone =  new DroneEntity("AA1", DroneModel.Cruiserweight, 500, 79,
				DroneState.DELIVERING);
		when(droneRepository.findById("AA1")).thenReturn(Optional.of(drone));
		try{
			dispatchService.updateDroneState("AA1", 96, DroneState.IDLE);
		} catch (DroneDispatcherException e) {
			assertEquals(e.getMessage(), "Only idle, loading, loaded and returning drones can be set to Idle");
		}
	}

	@Test
	void updateDroneStateToIdle_Success(){
		DroneEntity drone =  new DroneEntity("AA1", DroneModel.Cruiserweight, 500, 79,
				DroneState.IDLE);
		when(droneRepository.findById("AA1")).thenReturn(Optional.of(drone));
		String resp  = dispatchService.updateDroneState("AA1", 96, DroneState.IDLE);
		assertEquals(drone.getBatteryCapacity(), 96);
		assertEquals("Drone updated", resp);
	}

	@Test
	void updateDroneStateToLoaded_ErrorWhenInvalidState(){
		DroneEntity drone =  new DroneEntity("AA1", DroneModel.Cruiserweight, 500, 79,
				DroneState.IDLE);
		when(droneRepository.findById("AA1")).thenReturn(Optional.of(drone));
		try{
			dispatchService.updateDroneState("AA1", 96, DroneState.LOADED);
		} catch (DroneDispatcherException e) {
			assertEquals(e.getMessage(), "Only loading drones can be set to Loaded");
		}
	}

	@Test
	void updateDroneStateToDelivering_ErrorWhenInvalidState(){
		DroneEntity drone =  new DroneEntity("AA1", DroneModel.Cruiserweight, 500, 79,
				DroneState.IDLE);
		when(droneRepository.findById("AA1")).thenReturn(Optional.of(drone));
		try{
			dispatchService.updateDroneState("AA1", 96, DroneState.DELIVERING);
		} catch (DroneDispatcherException e) {
			assertEquals(e.getMessage(), "Only loaded drones can be set to delivering");
		}
	}

	@Test
	void updateDroneStateToDelivered_ErrorWhenInvalidState(){
		DroneEntity drone =  new DroneEntity("AA1", DroneModel.Cruiserweight, 500, 79,
				DroneState.LOADING);
		when(droneRepository.findById("AA1")).thenReturn(Optional.of(drone));
		try{
			dispatchService.updateDroneState("AA1", 96, DroneState.DELIVERED);
		} catch (DroneDispatcherException e) {
			assertEquals(e.getMessage(), "Only delivering drones can be set to delivered");
		}
	}

	@Test
	void updateDroneStateToReturning_ErrorWhenInvalidState(){
		DroneEntity drone =  new DroneEntity("AA1", DroneModel.Cruiserweight, 500, 79,
				DroneState.IDLE);
		when(droneRepository.findById("AA1")).thenReturn(Optional.of(drone));
		try{
			dispatchService.updateDroneState("AA1", 96, DroneState.RETURNING);
		} catch (DroneDispatcherException e) {
			assertEquals(e.getMessage(), "Only delivered drones can be set to returning");
		}
	}

	@Test
	void updateDroneStateToLoading_DontAllowFromStateChange(){
		DroneEntity drone =  new DroneEntity("AA1", DroneModel.Cruiserweight, 500, 79,
				DroneState.IDLE);
		when(droneRepository.findById("AA1")).thenReturn(Optional.of(drone));
		try{
			dispatchService.updateDroneState("AA1", 96, DroneState.LOADING);
		} catch (DroneDispatcherException e) {
			assertEquals(e.getMessage(), "Invalid Drone status");
		}
	}
}