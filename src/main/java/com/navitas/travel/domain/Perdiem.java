package com.navitas.travel.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Perdiem {
	private String city;
	private String county;
	private String state;
	private int year;
	private double meal;
	private double lodging;
}

