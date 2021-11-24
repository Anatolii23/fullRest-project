package com.example.payroll.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ordered_products")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderedProduct {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private double price;
    private int quantity;
}
