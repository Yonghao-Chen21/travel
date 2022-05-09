package com.navitas.travel;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.navitas.travel.domain.Airfare;
import com.navitas.travel.repo.AirfareRepo;
import com.navitas.travel.util.CsvReaderUtil;

@SpringBootApplication
public class TravelApplication implements CommandLineRunner {

	private CsvReaderUtil csvreader;
	private AirfareRepo airfareRepo;

	@Autowired
	public TravelApplication(final CsvReaderUtil csvreader, final AirfareRepo airfareRepo) {
		this.csvreader = csvreader;
		this.airfareRepo = airfareRepo;

	}

	public static void main(String[] args) {
		SpringApplication.run(TravelApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<Airfare> airfares = airfareRepo.findAll();
		if (airfares.size() == 0) {
			csvreader.loadAirFares();
		}

	}
}
