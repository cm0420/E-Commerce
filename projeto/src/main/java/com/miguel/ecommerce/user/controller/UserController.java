    package com.miguel.ecommerce.user.controller;

    import com.miguel.ecommerce.user.dto.UserRequest;
    import com.miguel.ecommerce.user.dto.UserResponse;
    import com.miguel.ecommerce.user.repository.UserRepository;
    import com.miguel.ecommerce.user.service.UserService;
    import com.miguel.ecommerce.user.service.impl.UserServiceImpl;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("api/v1/users")
    public class UserController {

        private final UserService userService;

        @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<UserResponse> save(@Valid @RequestBody UserRequest userRequest) {
           return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userRequest));}

        @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<UserResponse> findByID(@PathVariable("id") Long id) {
            return ResponseEntity.ok(userService.findByID(id));
        }

        @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<List<UserResponse>> findAll() {
            return ResponseEntity.ok(userService.findAll());
        }

        @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<UserResponse> update(@PathVariable("id") Long id, @Valid @RequestBody UserRequest userRequest) {
            return ResponseEntity.ok(userService.update(id, userRequest));
        }

        @PatchMapping(value = "/{id}/deactivate")
        public ResponseEntity<Void> deactivate(@PathVariable("id") Long id) {
            userService.deactivate(id);
            return ResponseEntity.noContent().build();
        }
    }
