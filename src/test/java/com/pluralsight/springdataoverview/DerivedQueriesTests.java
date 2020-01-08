package com.pluralsight.springdataoverview;



import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.pluralsight.springdataoverview.entity.Flight;
import com.pluralsight.springdataoverview.repository.SpringDataOverviewRepository;

@DataJpaTest
public class DerivedQueriesTests {
	
	@Autowired
	SpringDataOverviewRepository repository;
	
	@BeforeEach
	public void setUp(){
		repository.deleteAll();
	}
	
	@Test
	public void shouldFindFlightsFromHyderabad(){
		
		Flight f1 = createFlight("Hyderabad");
		Flight f2 = createFlight("Chennai");
		Flight f3 = createFlight("Hyderabad");
		Flight f4 = createFlight("Mumbai");
		
		repository.save(f1);
		repository.save(f2);
		repository.save(f3);
		repository.save(f4);
		
		List<Flight> list = repository.findByOrigin("Hyderabad");
		assertThat(list).hasSize(2);
		assertThat(list.get(0)).isEqualToComparingFieldByField(f1);
		assertThat(list.get(1)).isEqualToComparingFieldByField(f3);
	}
	
	private Flight createFlight(String origin){
		return createFlight(origin, "New Delhi");
	}
	
	@Test
	public void shouldFindFlightsFromTo(){
		
		Flight f1 = createFlight("Hyderabad","Kolkata");
		Flight f2 = createFlight("Chennai","New Delhi");
		Flight f3 = createFlight("Hyderabad","Kolkata");
		Flight f4 = createFlight("Mumbai","Kolkata");
		
		repository.save(f1);
		repository.save(f2);
		repository.save(f3);
		repository.save(f4);
		
		List<Flight> list = repository.findByOriginAndDestination("Hyderabad","Kolkata");
		assertThat(list).hasSize(2);
		assertThat(list.get(0)).isEqualToComparingFieldByField(f1);
		assertThat(list.get(1)).isEqualToComparingFieldByField(f3);
	}
	
	@Test
	public void shouldFindFlightsFromHydOrChn(){
		
		Flight f1 = createFlight("Hyderabad","Kolkata");
		Flight f2 = createFlight("Chennai","New Delhi");
		Flight f3 = createFlight("Hyderabad","Kolkata");
		Flight f4 = createFlight("Mumbai","Kolkata");
		
		repository.save(f1);
		repository.save(f2);
		repository.save(f3);
		repository.save(f4);
		
		List<Flight> list = repository.findByOriginIn("Hyderabad","Chennai");
		assertThat(list).hasSize(3);
		assertThat(list.get(0)).isEqualToComparingFieldByField(f1);
		assertThat(list.get(1)).isEqualToComparingFieldByField(f2);
		assertThat(list.get(2)).isEqualToComparingFieldByField(f3);
	}
	
	@Test
	public void shouldFindFlightsFromHyderabadIgnoreCase(){
		
		Flight f1 = createFlight("HYDERABAD");
		repository.save(f1);
		
		List<Flight> list = repository.findByOriginIgnoreCase("Hyderabad");
		assertThat(list).hasSize(1);
		assertThat(list.get(0)).isEqualToComparingFieldByField(f1);
	}
	
	private Flight createFlight(String origin, String destination){
		Flight f = new Flight();
		f.setOrigin(origin);
		f.setDestination(destination);
		f.setScheduledAt(LocalDateTime.parse("2020-01-23T23:30:00"));
		return f;
	}

}
