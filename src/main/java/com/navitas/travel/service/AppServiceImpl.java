package com.navitas.travel.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.navitas.travel.domain.Airfare;
import com.navitas.travel.domain.Perdiem;
import com.navitas.travel.domain.Site;
import com.navitas.travel.domain.Ticket;
import com.navitas.travel.domain.Traveler;
import com.navitas.travel.dto.TicketDto;
import com.navitas.travel.dto.TravelerDto;
import com.navitas.travel.dto.TravelerOutputDto;
import com.navitas.travel.dto.OutputDto;
import com.navitas.travel.dto.PerdiemDto;
import com.navitas.travel.repo.AirfareRepo;
import com.navitas.travel.repo.TicketRepo;

@Service
public class AppServiceImpl implements AppService {

	private RestTemplate restTemplate;
	private AirfareRepo airfareRepo;
	private TicketRepo ticketRepo;
	private final String URL = "https://api.gsa.gov/travel/perdiem/v2/rates/city/{city}/state/{state}/year/{year}?api_key={apiKey}";
	private final String APIKEY = "o83Ktv48sVe0haA0TIuBbeutHeAi6ctZbuDlEAYq";

	@Autowired
	public AppServiceImpl(final RestTemplate restTemplate, final AirfareRepo airfareRepo, final TicketRepo ticketRepo) {
		this.restTemplate = restTemplate;
		this.airfareRepo = airfareRepo;
		this.ticketRepo = ticketRepo;
	}

	@Override
	public OutputDto respondToApi(TicketDto input) {
		Ticket ticket = new Ticket();
		String name = input.getName();
		LocalDate startDate = input.getStartDate();
		LocalDate endDate = input.getEndDate();
		List<TravelerDto> travelerDtos = input.getTravlers();
		String status = input.getStatus();
		List<Traveler> travelers = new ArrayList<>();
		List<Site> sites = input.getSites();
		ticket.setName(name);
		ticket.setStartDate(startDate);
		ticket.setEndDate(endDate);
		ticket.setStatus(status);
		for (TravelerDto obj : travelerDtos) {
			Traveler traveler = new Traveler();
			String tname = obj.getName();
			String email = obj.getEmail();
			String originCity = obj.getOriginCity();
			String originState = obj.getOriginState();
			traveler.setName(tname);
			traveler.setEmail(email);
			traveler.setOriginCity(originCity);
			traveler.setOriginState(originState);
			traveler.setTicket(ticket);
			travelers.add(traveler);
		}
		ticket.setTravelers(travelers);
		ticket = getSolution(ticket, sites);
		return mapperToOutput(ticket);
	}

	@Override
	public Ticket getSolution(Ticket ticket, List<Site> sites) {
		LocalDate startDate = ticket.getStartDate();
		LocalDate endDate = ticket.getEndDate();
		int year = endDate.getYear();
		List<Traveler> travelers = ticket.getTravelers();
		long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
		List<LocalDate> dates = IntStream.iterate(0, i -> i + 1).limit(numOfDaysBetween)
				.mapToObj(i -> startDate.plusDays(i)).collect(Collectors.toList());
		Map<Integer, Integer> map = new HashMap<>();
		for (LocalDate date : dates) {
			Integer month = date.getMonthValue();
			map.putIfAbsent(month, 0);
			map.put(month, map.get(month) + 1);
		}
		List<Double> costs = new ArrayList<>();
		for (int i = 0; i < sites.size(); i++) {
			double sum = 0;
			for (Integer month : map.keySet()) {
				Site site = sites.get(i);
				int days = map.get(month);
				double cost = getAllTravelerCostForOneSite(travelers, site, year, month, days);
				sum += cost;
			}
			costs.add(sum);
		}
		int minIdx = IntStream.range(0, costs.size()).reduce((i, j) -> costs.get(i) > costs.get(j) ? j : i).getAsInt();
		Site result = sites.get(minIdx);
		ticket.setDestinationCity(result.getDestinationCity());
		ticket.setDestinationState(result.getDestinationState());
		for (Traveler traveler : travelers) {
			double airfareCost = getOneTravelerAirfareForOneSite(traveler, result);
			double mealCost = 0;
			double lodgingCost = 0;
			Airfare airfare = getAirfare(traveler.getOriginCity(), traveler.getOriginState(),
					result.getDestinationCity(), result.getDestinationState());
			traveler.setOriginAirport(airfare.getOriginAirport());
			traveler.setDestinationAirport(airfare.getDestinationAirport());
			for (Integer month : map.keySet()) {
				int days = map.get(month);
				PerdiemDto perdiemDto = getOneTravelerPerdiemCostForOneSite(traveler, result, year, month, days);
				mealCost += perdiemDto.getMealCost();
				lodgingCost += perdiemDto.getLodgingCost();
			}
			traveler.setAirfareCost(airfareCost);
			traveler.setMealCost(mealCost);
			traveler.setLodgingCost(lodgingCost);
		}
		ticket.setStatus("submitted");
		ticket = ticketRepo.save(ticket);
		return ticket;
	}

	@Override
	public OutputDto mapperToOutput(Ticket ticket) {
		Long tid = ticket.getId();
		String tname = ticket.getName();
		LocalDate startDate = ticket.getStartDate();
		LocalDate endDate = ticket.getEndDate();
		String destinationCity = ticket.getDestinationCity();
		String destinationState = ticket.getDestinationState();
		String status = ticket.getStatus();
		List<Traveler> travelers = ticket.getTravelers();
		List<TravelerOutputDto> tDtoList = new ArrayList<>();
		for (Traveler t : travelers) {
			Long id = t.getId();
			String name = t.getName();
			String email = t.getEmail();
			String originCity = t.getOriginCity();
			String originState = t.getOriginState();
			String originAirport = t.getOriginAirport();
			String destinationAirport = t.getDestinationAirport();
			double airfareCost = t.getAirfareCost();
			double mealCost = t.getMealCost();
			double lodgingCost = t.getLodgingCost();
			TravelerOutputDto tDto = TravelerOutputDto.builder().Id(id).name(name).email(email).originCity(originCity)
					.originState(originState).originAirport(originAirport).destinationAirport(destinationAirport)
					.ticketId(tid).airfareCost(airfareCost).mealCost(mealCost).lodgingCost(lodgingCost).build();
			tDtoList.add(tDto);
		}
		OutputDto outputDto = OutputDto.builder().id(tid).name(tname).startDate(startDate).endDate(endDate)
				.travelers(tDtoList).destinationCity(destinationCity).destinationState(destinationState).status(status)
				.build();
		return outputDto;
	}

	@Override
	public Perdiem getPerdiem(String destinationCity, String destinationState, int year, int month) {

		String response = restTemplate.getForObject(URL, String.class, destinationCity, destinationState, year, APIKEY);
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(response);
			JSONObject obj = jsonObject.getJSONArray("rates").getJSONObject(0).getJSONArray("rate").getJSONObject(0);
			JSONArray months = obj.getJSONObject("months").getJSONArray("month");
			double meal = obj.getDouble("meals");
			String county = obj.getString("county");
			double lodging = 0;
			for (int i = 0; i < months.length(); i++) {
				if (months.getJSONObject(i).getInt("number") == month) {
					lodging = months.getJSONObject(i).getDouble("value");
				}
			}
			Perdiem perdiem = Perdiem.builder().city(destinationCity).county(county).state(destinationState).year(year)
					.meal(meal).lodging(lodging).build();
			return perdiem;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public Airfare getAirfare(String originCity, String originState, String destinationCity, String destinationState) {
		List<Airfare> airfares = airfareRepo.getAirfare(originCity.toUpperCase(), originState,
				destinationCity.toUpperCase(), destinationState);
		if (airfares != null) {
			Airfare result = airfares.get(0);
			for (int i = 1; i < airfares.size(); i++) {
				if (airfares.get(i).getYcaFare() < result.getYcaFare()) {
					result = airfares.get(i);
				}
			}
			return result;
		}
		return null;
	}

	@Override
	public PerdiemDto getOneTravelerPerdiemCostForOneSite(Traveler traveler, Site site, int year, int month, int days) {
		Perdiem perdiem = getPerdiem(site.getDestinationCity(), site.getDestinationState(), year, month);
		return new PerdiemDto(perdiem.getMeal() * days, perdiem.getLodging() * days);
	}

	@Override
	public double getOneTravelerAirfareForOneSite(Traveler traveler, Site site) {
		Airfare airfare = getAirfare(traveler.getOriginCity(), traveler.getOriginState(), site.getDestinationCity(),
				site.getDestinationState());
		return airfare.getYcaFare();
	}

	@Override
	public double getOneTravelerCostForOneSite(Traveler traveler, Site site, int year, int month, int days) {
		Perdiem perdiem = getPerdiem(site.getDestinationCity(), site.getDestinationState(), year, month);
		return getOneTravelerAirfareForOneSite(traveler, site) + (perdiem.getMeal() + perdiem.getLodging()) * days;
	}

	@Override
	public double getAllTravelerCostForOneSite(List<Traveler> travelers, Site site, int year, int month, int days) {
		double sum = 0;
		for (Traveler traveler : travelers) {
			sum += getOneTravelerCostForOneSite(traveler, site, year, month, days);
		}
		return sum;
	}

}
