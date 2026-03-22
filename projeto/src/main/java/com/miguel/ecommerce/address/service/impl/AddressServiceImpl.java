package com.miguel.ecommerce.address.service.impl;

import com.miguel.ecommerce.address.dto.AddressRequest;
import com.miguel.ecommerce.address.dto.AddressResponse;
import com.miguel.ecommerce.address.entity.Address;
import com.miguel.ecommerce.address.mapper.AddressMapper;
import com.miguel.ecommerce.address.repository.AddressRepository;
import com.miguel.ecommerce.address.service.AddressService;
import com.miguel.ecommerce.excpetion.ResourceNotFoundException;
import com.miguel.ecommerce.user.entity.User;
import com.miguel.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepositrory;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    @Override
    public AddressResponse save(AddressRequest addressRequest) {
        User user = userRepository.findById(addressRequest.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User", addressRequest.userId()));
        Address address = addressMapper.toEntity(addressRequest);
        address.setUser(user);
        return addressMapper.toAddressResponse(addressRepositrory.save(address));

    }

    @Override
    public List<AddressResponse> findAllByUserId(Long userId) {
        return addressMapper.toAddressResponseList(addressRepositrory.findByUserId(userId));

    }

    @Override
    public AddressResponse findByID(Long id) {
       Address address = addressRepositrory.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address", id));
        return addressMapper.toAddressResponse(address);
    }

    @Override
    public void delete(Long id) {
        Address address = addressRepositrory.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Address", id));
        addressRepositrory.delete(address);
    }

    @Override
    public AddressResponse update(Long id, AddressRequest addressRequest) {
        Address address = addressRepositrory.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address", id));

        address.setStreet(addressRequest.street());
        address.setNumber(addressRequest.number());
        address.setComplement(addressRequest.complement());
        address.setDistrict(addressRequest.district());
        address.setCity(addressRequest.city());
        address.setState(addressRequest.state());
        address.setZipCode(addressRequest.zipCode());

        return addressMapper.toAddressResponse(addressRepositrory.save(address));


    }
}
