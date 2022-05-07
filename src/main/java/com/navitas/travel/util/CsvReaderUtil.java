package com.navitas.travel.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.navitas.travel.domain.Airfare;
import com.navitas.travel.repo.AirfareRepo;

@Component
public class CsvReaderUtil {
	private AirfareRepo airfareRepo;
	
	@Autowired
	public CsvReaderUtil(final AirfareRepo airfareRepo) {
		this.airfareRepo = airfareRepo;
	}

	public void loadAirFares() {
		List<Airfare> list = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		try {
			List<String> dlist = Files
					.readAllLines(Paths.get(CsvReaderUtil.class.getResource("/awards_2022.csv").toURI()));
			for (int i = 1; i < dlist.size(); i++) {
				String[] data = dlist.get(i).split(",");
				String originAirport = data[2];
				String destinationAirport = data[3];
				String originCity = data[4];
				String originState = data[5];
				String destinationCity = data[7];
				String destinationState = data[8];
				String airline = data[10];
				double ycaFare = Double.parseDouble(data[13]);
				double caFare = Double.parseDouble(data[14]);
				LocalDate effectiveDate = LocalDate.parse(data[21],formatter);
				LocalDate expirationDate = LocalDate.parse(data[22],formatter);
				Airfare airfare = Airfare.builder().airline(airline).caFare(caFare)
						.destinationAirport(destinationAirport).destinationCity(destinationCity)
						.destinationState(destinationState).effectiveDate(effectiveDate).expirationDate(expirationDate)
						.originAirport(originAirport).originCity(originCity).originState(originState).ycaFare(ycaFare)
						.build();
				list.add(airfare);
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		airfareRepo.saveAll(list);
		System.out.println("Airfare table loaded: " + list.size());
	}
	

}
