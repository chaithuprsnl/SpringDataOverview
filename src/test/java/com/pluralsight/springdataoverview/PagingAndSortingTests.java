package com.pluralsight.springdataoverview;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.pluralsight.springdataoverview.entity.Flight;
import com.pluralsight.springdataoverview.repository.SpringDataOverviewPASRepository;

@DataJpaTest
public class PagingAndSortingTests {
	
	@Autowired
	SpringDataOverviewPASRepository repository;
	
	@BeforeEach
	public void setUp(){
		repository.deleteAll();
	}
	
	@Test
	public void shouldSortFlightsByOrigin(){
		LocalDateTime now = LocalDateTime.now();
		Flight f1 = createFlight("Hyderabad", now);
		Flight f2 = createFlight("Chennai", now);
		Flight f3 = createFlight("Hyderabad", now);
		Flight f4 = createFlight("Mumbai", now);
		
		repository.save(f1);
		repository.save(f2);
		repository.save(f3);
		repository.save(f4);
		
		Iterable<Flight> list = repository.findAll(Sort.by("origin"));
		
		assertThat(list).hasSize(4);
		final Iterator<Flight> iterator = list.iterator();
		assertThat(iterator.next().getOrigin()).isEqualTo("Chennai");
		assertThat(iterator.next().getOrigin()).isEqualTo("Hyderabad");
		assertThat(iterator.next().getOrigin()).isEqualTo("Hyderabad");
		assertThat(iterator.next().getOrigin()).isEqualTo("Mumbai");
		
	}
	
	@Test
	public void shouldSortFlightsByOriginAndScheduled(){
		LocalDateTime now = LocalDateTime.now();
		Flight f1 = createFlight("Hyderabad", now);
		Flight f2 = createFlight("Chennai", now.plusHours(2));
		Flight f3 = createFlight("Hyderabad", now.minusHours(1));
		Flight f4 = createFlight("Mumbai", now.plusHours(1));
		Flight f5 = createFlight("Mumbai", now);
		
		repository.save(f1);
		repository.save(f2);
		repository.save(f3);
		repository.save(f4);
		repository.save(f5);
		
		Iterable<Flight> list = repository.findAll(Sort.by("origin","scheduledAt"));
		
		assertThat(list).hasSize(5);
		final Iterator<Flight> iterator = list.iterator();
		assertThat(iterator.next()).isEqualToComparingFieldByField(f2);
		assertThat(iterator.next()).isEqualToComparingFieldByField(f3);
		assertThat(iterator.next()).isEqualToComparingFieldByField(f1);
		assertThat(iterator.next()).isEqualToComparingFieldByField(f5);
		assertThat(iterator.next()).isEqualToComparingFieldByField(f4);
		
	}
	
	@Test
	public void shouldPageResults(){
		for(int i=0;i<50;i++){
			repository.save(createFlight(String.valueOf(i)));
		}
		
		Page<Flight> page = repository.findAll(PageRequest.of(2, 5));
		
		assertThat(page.getTotalElements()).isEqualTo(50);
		assertThat(page.getNumberOfElements()).isEqualTo(5);
		assertThat(page.getTotalPages()).isEqualTo(10);
		assertThat(page.getContent()).extracting(Flight::getOrigin).containsExactly("10","11","12","13","14");
	}
	
	@Test
	public void shouldPageAndSortResults(){
		for(int i=0;i<50;i++){
			repository.save(createFlight(String.valueOf(i)));
		}
		
		Page<Flight> page = repository.findAll(PageRequest.of(2, 5, Sort.by(Direction.DESC, "origin")));
		
		assertThat(page.getTotalElements()).isEqualTo(50);
		assertThat(page.getNumberOfElements()).isEqualTo(5);
		assertThat(page.getTotalPages()).isEqualTo(10);
		assertThat(page.getContent()).extracting(Flight::getOrigin).containsExactly("44","43","42","41","40");
	}
	
	@Test
	public void shouldPageAndSortDerived(){
		for(int i=0;i<10;i++){
			Flight f = createFlight(String.valueOf(i));
			f.setDestination("London");
			repository.save(f);
		}
		
		for(int i=0;i<10;i++){
			Flight f = createFlight(String.valueOf(i));
			f.setDestination("New York");
			repository.save(f);
		}
		
		Page<Flight> page = repository.findByDestination("London", PageRequest.of(0, 5, Sort.by(Direction.DESC, "origin")));
		
		assertThat(page.getTotalElements()).isEqualTo(10);
		assertThat(page.getNumberOfElements()).isEqualTo(5);
		assertThat(page.getTotalPages()).isEqualTo(2);
		assertThat(page.getContent()).extracting(Flight::getOrigin).containsExactly("9","8","7","6","5");
	}
	
	private Flight createFlight(String origin, String destination, LocalDateTime scheduledAt){
		Flight f = new Flight();
		f.setOrigin(origin);
		f.setDestination(destination);
		f.setScheduledAt(scheduledAt);
		return f;
	}
	
	private Flight createFlight(String origin, LocalDateTime scheduledAt){
		return createFlight(origin, "New Delhi", scheduledAt);
	}
	
	private Flight createFlight(String origin){
		return createFlight(origin, "New Delhi", LocalDateTime.now());
	}

}
