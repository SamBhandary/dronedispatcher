package com.musala.dronedispatcher.scheduler;

import ch.qos.logback.classic.Logger;
import com.musala.dronedispatcher.entity.DroneEntity;
import com.musala.dronedispatcher.repository.DroneRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class AuditBattery {
	public static final Logger LOGGER = (Logger) LoggerFactory.getLogger(
			AuditBattery.class.getName());
	@Autowired
	private DroneRepository droneRepository;

	@Scheduled(fixedRateString = "${scheduler.interval}")
	public void auditLogBattery() {
		for (DroneEntity drone : droneRepository.findAll()) {
			LOGGER.warn("Drone Serial:: {}, Battery Percentage:: {}", drone.getSerialNumber(),
					drone.getBatteryCapacity());

		}
	}
}
