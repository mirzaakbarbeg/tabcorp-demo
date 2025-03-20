package com.tabcorp.demo.repository;

import com.tabcorp.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> { }
