package com.tabcorp.demo.repository;

import com.tabcorp.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> { }
