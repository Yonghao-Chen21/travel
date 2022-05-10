package com.navitas.travel.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutputDto {
	private Long id;
	private String name;
	private LocalDate startDate;
	private LocalDate endDate;
	private List<TravelerOutputDto> travelers;
	private String destinationCity;
	private String destinationState;
	private String status;

}
