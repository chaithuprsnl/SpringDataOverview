package com.pluralsight.springdataoverview.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pluralsight.springdataoverview.entity.Flight;

@Repository
public interface SpringDataOverviewPASRepository extends PagingAndSortingRepository<Flight, Long>{
	
	List<Flight> findByOrigin(String origin);
	
	List<Flight> findByOriginAndDestination(String origin, String destination);
	
	List<Flight> findByOriginIn(String... origin);
	
	List<Flight> findByOriginIgnoreCase(String origin);
	
	Page<Flight> findByDestination(String destination, Pageable page);

}
