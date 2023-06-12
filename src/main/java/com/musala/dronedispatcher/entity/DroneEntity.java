package com.musala.dronedispatcher.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.musala.dronedispatcher.enums.DroneModel;
import com.musala.dronedispatcher.enums.DroneState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "drones")
public class DroneEntity {

	@Id
	@Column(name = "serial_number", unique = true)
	private String serialNumber;
	@Column(name = "model")
	private DroneModel model;
	@Column(name = "weight_limit")
	private Integer weightLimit;
	@Column(name = "battery_capacity")
	private Integer batteryCapacity;
	@Column(name = "state")
	private DroneState state;

	@OneToMany(fetch = FetchType.LAZY)
	@JsonIgnore
	private List<MedicationEntity> medications;

	public DroneEntity() {
	}

	public DroneEntity(String serialNumber, DroneModel model, Integer weightLimit,
			Integer batteryCapacity, DroneState state) {
		this.serialNumber = serialNumber;
		this.model = model;
		this.weightLimit = weightLimit;
		this.batteryCapacity = batteryCapacity;
		this.state = state;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public DroneModel getModel() {
		return model;
	}

	public void setModel(DroneModel model) {
		this.model = model;
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

	public DroneState getState() {
		return state;
	}

	public void setState(DroneState state) {
		this.state = state;
	}

	public List<MedicationEntity> getMedications() {
		return medications;
	}

	public void setMedications(List<MedicationEntity> medications) {
		this.medications = medications;
	}

}
