package com.nexign.bootcamp.emulator.model;


import jakarta.persistence.*;
import lombok.Data;

/**
 * Represents a subscriber entity with a unique identifier and phone number.
 */
@Data
@Entity
@Table(name = "subscriber")
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long phoneNumber;
}
