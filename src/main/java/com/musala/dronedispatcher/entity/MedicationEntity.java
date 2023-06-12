package com.musala.dronedispatcher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "medications")
public class MedicationEntity {

	@Column(name = "code", unique = true)
	@Id
	private String code;
	@Column(name = "name", unique = true)
	private String name;
	@Column(name = "weight")
	private Integer weight;
	@Column(name = "image", unique = true)
	private String image;

	public MedicationEntity() {
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}

