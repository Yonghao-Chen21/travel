package com.navitas.travel.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.navitas.travel.domain.Airfare;

public interface AirfareRepo extends JpaRepository<Airfare, Long> {
	//In query, use Java style attribute name
	@Query("select a from Airfare a where a.originCity LIKE %:originCity% and a.originState = :originState and destinationCity LIKE %:destinationCity% and destinationState = :destinationState")
	public List<Airfare> getAirfare(@Param("originCity") String originCity, @Param("originState") String originState,
			@Param("destinationCity") String destinationCity, @Param("destinationState") String destinationState);

}