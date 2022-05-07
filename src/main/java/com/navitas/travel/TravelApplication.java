package com.navitas.travel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.navitas.travel.domain.Airfare;
import com.navitas.travel.domain.Site;
import com.navitas.travel.domain.Ticket;
import com.navitas.travel.domain.Traveler;
import com.navitas.travel.repo.AirfareRepo;
import com.navitas.travel.repo.TicketRepo;
import com.navitas.travel.service.AppService;
import com.navitas.travel.util.CsvReaderUtil;

@SpringBootApplication
public class TravelApplication implements CommandLineRunner {

	private CsvReaderUtil csvreader;
	private AirfareRepo airfareRepo;
	private AppService appService;
	private TicketRepo ticketRepo;

	@Autowired
	public TravelApplication(final CsvReaderUtil csvreader, final AirfareRepo airfareRepo,
			final AppService appService, final  TicketRepo ticketRepo) {
		this.csvreader = csvreader;
		this.airfareRepo = airfareRepo;
		this.appService = appService;
		this.ticketRepo = ticketRepo;

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

		LocalDate startDate = LocalDate.parse("2022-04-28");
		LocalDate endDate = LocalDate.parse("2022-05-04");

		List<Traveler> travelers = new ArrayList<>();
		Traveler traveler1 = Traveler.builder().name("John").email("John@gmail.com").originCity("SAN DIEGO")
				.originState("CA").build();
		travelers.add(traveler1);
		Traveler traveler2 = Traveler.builder().name("May").email("May@gmail.com").originCity("Washington")
				.originState("DC").build();
		travelers.add(traveler2);
		
		List<Site> sites = new ArrayList<>();
		sites.add(new Site("SEATTLE", "WA"));
		// sites.add(new Site("Denver","CO"));
		sites.add(new Site("SAN FRANCISCO", "CA"));
		
		Ticket ticket = new Ticket();
		ticket.setStartDate(startDate);
		ticket.setEndDate(endDate);
		ticket.setName("test conference");
		traveler1.setTicket(ticket);
		traveler2.setTicket(ticket);
		ticket.setTravlers(travelers);		
		appService.getSolution(ticket, sites);
		System.out.println(ticket.getDestinationCity());



	}
}
