package com.navitas.travel.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.navitas.travel.domain.Ticket;
import com.navitas.travel.dto.TicketDto;
import com.navitas.travel.service.AppService;

@RestController
@RequestMapping("/solution")
public class AppController {
	
	private AppService service;
	
	@Autowired
	public AppController(final AppService service) {
		this.service = service;
	}
	
	@PostMapping
	public Ticket getResponse(@RequestBody TicketDto t) {
		return service.respondToApi(t);
	}

}
