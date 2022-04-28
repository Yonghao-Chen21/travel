package com.navitas.travel.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.navitas.travel.domain.Airfare;

public interface AirfareRepo extends JpaRepository<Airfare,Long>{
	
}