package com.miguel.ecommerce.address.repository;

import com.miguel.ecommerce.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    boolean existsByIdAndUserId(Long id, Long userId);

    List<Address> findByUserId(Long userId);
}
