package com.example.payroll.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CUSTOMER_ORDER")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Date date;
    private Double price;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;



    public enum Status {

        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }
}
