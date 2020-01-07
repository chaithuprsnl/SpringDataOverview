package com.pluralsight.springdataoverview;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import com.pluralsight.springdataoverview.entity.Flight;


@DataJpaTest
class SpringDataOverviewApplicationTests {

	@Autowired
	EntityManager entitymanager;
	
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

}
