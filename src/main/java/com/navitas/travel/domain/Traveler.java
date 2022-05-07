package com.navitas.travel.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Traveler {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	private String name;
	private String email;
	private String originCity;
	private String originState;
	private String originAirport;
	private String destinationAirport;
	@ManyToOne
	@JoinColumn(name = "ticket_id")
	private Ticket ticket;
	private double airfareCost;
	private double mealCost;
	private double lodgingCost;

}
