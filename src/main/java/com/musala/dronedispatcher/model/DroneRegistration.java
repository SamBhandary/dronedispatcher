package com.musala.dronedispatcher.model;

import com.musala.dronedispatcher.enums.DroneModel;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class DroneRegistration {

	@Size(min = 1, max = 100)
	private String serialNumber;

	@Min(0)
	@Max(500)
	private Integer weightLimit;

	@Min(0)
	@Max(100)
	private Integer batteryCapacity;

	@Enumerated(EnumType.STRING)
	private DroneModel model;

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getWeightLimit() {
		return weightLimit;
	}

	public void setWeightLimit(Integer weightLimit) {
		this.weightLimit = weightLimit;
	}

	public Integer getBatteryCapacity() {
		return batteryCapacity;
	}

	public void setBatteryCapacity(Integer batteryCapacity) {
		this.batteryCapacity = batteryCapacity;
	}

	public DroneModel getModel() {
		return model;
	}

	public void setModel(DroneModel model) {
		this.model = model;
	}
}
