package com.navitas.travel.service;

import java.util.List;

import com.navitas.travel.domain.Airfare;
import com.navitas.travel.domain.Perdiem;
import com.navitas.travel.domain.Site;
import com.navitas.travel.domain.Ticket;
import com.navitas.travel.domain.Traveler;
import com.navitas.travel.dto.TicketDto;
import com.navitas.travel.dto.OutputDto;
import com.navitas.travel.dto.PerdiemDto;

public interface AppService {	
	
	public OutputDto respondToApi(TicketDto input);
	
	public Ticket getSolution(Ticket ticket, List<Site> sites);
	
	public OutputDto mapperToOutput(Ticket ticket);
	
	public Perdiem getPerdiem(String destinationCity, String destinationState, int year, int month);
	
	public Airfare getAirfare(String originCity, String originState,String destinationCity, String destinationState);

	public PerdiemDto getOneTravelerPerdiemCostForOneSite(Traveler traveler, Site site, int year, int month, int days);
		
	public double getOneTravelerAirfareForOneSite(Traveler traveler, Site site);
	
	public double getOneTravelerCostForOneSite(Traveler traveler, Site site, int year, int month, int days);
	
	public double getAllTravelerCostForOneSite(List<Traveler> travelers, Site site, int year, int month, int days);
	
	
}
