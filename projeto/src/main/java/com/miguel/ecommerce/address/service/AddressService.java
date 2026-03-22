package com.miguel.ecommerce.address.service;

import com.miguel.ecommerce.address.dto.AddressRequest;
import com.miguel.ecommerce.address.dto.AddressResponse;

import java.util.List;

public interface AddressService {
    AddressResponse save(AddressRequest addressRequest);
    List<AddressResponse> findAllByUserId(Long userId);
    AddressResponse findByID(Long id);
    void delete(Long id);
    AddressResponse update(Long id, AddressRequest addressRequest);

}
