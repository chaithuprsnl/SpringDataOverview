package com.pluralsight.springdataoverview;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.pluralsight.springdataoverview.entity.Flight;
import com.pluralsight.springdataoverview.repository.SpringDataOverviewRepository;


@DataJpaTest
class SpringDataOverviewApplicationTests {

	@Autowired
	EntityManager entitymanager;
	
	@Autowired
	SpringDataOverviewRepository repository;
	
	//Using JPA without Spring Data
	@Test
	public void verifyFlightCanBeSaved() {
		final Flight f = new Flight();
		f.setOrigin("Chennai");
		f.setDestination("Delhi");
		f.setScheduledAt(LocalDateTime.parse("2019-12-12T23:30:00"));
		
		entitymanager.persist(f);
		
		final TypedQuery<Flight> results = entitymanager.createQuery("select f from Flight f",Flight.class);
		final List<Flight> resultList = results.getResultList();
		
		assertThat(resultList).hasSize(1).first().isEqualTo(f);
	}
	
	//Using Spring Data JPA
	@Test
	public void shouldPerformCRUDOperations(){
		Flight f = new Flight();
		f.setOrigin("Chennai");
		f.setDestination("New Delhi");
		f.setScheduledAt(LocalDateTime.parse("2019-10-12T21:00:00"));
		
		repository.save(f);
		
		assertThat(repository.findAll()).hasSize(1).first().isEqualToComparingFieldByField(f);
		
		repository.deleteById(f.getId());
		
		assertThat(repository.count()).isZero();
		
	}

}
