package com.e_commerce.users.repositories;

import com.e_commerce.users.models.AddressModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<AddressModel, UUID> {
}
