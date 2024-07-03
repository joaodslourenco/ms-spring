package com.e_commerce.users.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tb_addresses")
@Data
public class AddressModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "neighbourhood", nullable = false)
    private String neighbourhood;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "zipCode", nullable = false)
    private String zipCode;

    @Column(name = "country", nullable = false)
    private String country;

    @OneToOne(mappedBy = "address")
    private UserModel user;
}
