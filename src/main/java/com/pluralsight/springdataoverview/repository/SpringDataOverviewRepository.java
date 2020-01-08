package com.pluralsight.springdataoverview.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pluralsight.springdataoverview.entity.Flight;

@Repository
public interface SpringDataOverviewRepository extends CrudRepository<Flight, Long>{
	
	List<Flight> findByOrigin(String origin);
	
	List<Flight> findByOriginAndDestination(String origin, String destination);
	
	List<Flight> findByOriginIn(String... origin);
	
	List<Flight> findByOriginIgnoreCase(String origin);
}
