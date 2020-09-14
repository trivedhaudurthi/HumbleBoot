package com.northeastern.edu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.northeastern.edu.models.Person;
@Service
public interface PersonRepository extends JpaRepository<Person, Integer> {
	public Person findByEmail(String email);
}
