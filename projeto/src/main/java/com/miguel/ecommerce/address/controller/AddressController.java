package com.miguel.ecommerce.address.controller;

import com.miguel.ecommerce.address.dto.AddressRequest;
import com.miguel.ecommerce.address.dto.AddressResponse;
import com.miguel.ecommerce.address.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/address")
public class AddressController {

    private final AddressService addressService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressResponse> save(@Valid @RequestBody AddressRequest addressRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.save(addressRequest));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressResponse> update(@PathVariable("id") Long id, @Valid @RequestBody AddressRequest addressRequest) {
        return ResponseEntity.ok(addressService.update(id, addressRequest));
    }

    @GetMapping(value ="/users/{userId}", consumes=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AddressResponse>> findAllByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(addressService.findAllByUserId(userId));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressResponse> findByID(@PathVariable("id") Long id) {
        return ResponseEntity.ok(addressService.findByID(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
