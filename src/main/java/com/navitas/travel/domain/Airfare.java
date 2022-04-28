package com.navitas.travel.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Airfare {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	private String originAirport;
	private String destinationAirport;
	private String originCity;
	private String originState;
	private String destinationCity;
	private String destinationState;
	private String airline;
	private double ycaFare;
	private double caFare;
	private Date effectiveDate;
	private Date expirationDate;

}
