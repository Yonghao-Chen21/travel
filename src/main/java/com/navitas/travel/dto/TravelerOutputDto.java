package com.navitas.travel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelerOutputDto {
	private Long Id;
	private String name;
	private String email;
	private String originCity;
	private String originState;
	private String originAirport;
	private String destinationAirport;
	private Long ticketId;
	private double airfareCost;
	private double mealCost;
	private double lodgingCost;

}
