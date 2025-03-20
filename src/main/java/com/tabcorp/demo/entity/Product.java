package com.tabcorp.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "product_code")
    private String productCode;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "status")
    private String status;
}

