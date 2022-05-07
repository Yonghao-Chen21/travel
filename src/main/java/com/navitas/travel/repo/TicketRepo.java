package com.navitas.travel.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.navitas.travel.domain.Ticket;

public interface TicketRepo extends JpaRepository<Ticket, Long>{

}
