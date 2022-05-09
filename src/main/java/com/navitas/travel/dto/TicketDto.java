package com.navitas.travel.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.navitas.travel.domain.Site;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {
	private String name;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate startDate;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate endDate;
	private List<TravelerDto> travlers;
	private String status;
	private List<Site> sites;
}
