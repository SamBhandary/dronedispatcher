package com.musala.dronedispatcher.services.impl;

import com.musala.dronedispatcher.entity.DroneEntity;
import com.musala.dronedispatcher.enums.DroneModel;
import com.musala.dronedispatcher.exceptions.DroneDispatcherException;
import com.musala.dronedispatcher.model.DroneRegistration;
import com.musala.dronedispatcher.repository.DroneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class DroneServiceImplTest {

	@InjectMocks
	DroneServiceImpl droneService;

	@Mock
	private DroneRepository droneRepository;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void registerDrone_FleetSizeExceeds() {
		when(droneRepository.count()).thenReturn(10L);
		DroneRegistration droneRegistration = Mockito.mock(DroneRegistration.class);
		try {
			droneService.registerDrone(droneRegistration);
		} catch (DroneDispatcherException e) {
			assertEquals(e.getMessage(), "Drone fleet size exceeded");
		}
	}

	@Test
	void registerDrone_Success() {
		when(droneRepository.count()).thenReturn(0L);

		DroneRegistration droneRegistration = new DroneRegistration();
		droneRegistration.setModel(DroneModel.Cruiserweight);
		droneRegistration.setBatteryCapacity(100);
		droneRegistration.setSerialNumber("ab1");
		droneRegistration.setWeightLimit(500);

		DroneEntity drone = Mockito.mock(DroneEntity.class);
		when(droneRepository.save(any())).thenReturn(drone);

		DroneEntity entity = droneService.registerDrone(droneRegistration);
		assertEquals(entity, drone);

	}
}