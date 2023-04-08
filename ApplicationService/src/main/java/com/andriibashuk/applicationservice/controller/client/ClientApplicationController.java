package com.andriibashuk.applicationservice.controller.client;

import com.andriibashuk.applicationservice.request.NewApplicationRequest;
import com.andriibashuk.applicationservice.response.ApplicationResponse;
import com.andriibashuk.applicationservice.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "client")
public class ClientApplicationController {
    ApplicationService applicationService;

    public ClientApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/hi")
    public String hi() {
        return "hi";
    }

    @PostMapping("/new")
    public ResponseEntity<ApplicationResponse> newApplication(@Valid @RequestBody NewApplicationRequest newApplicationRequest) {
        return new ResponseEntity<>(applicationService.newApplication(newApplicationRequest.getClientId(), newApplicationRequest.getRequestedAmount()), HttpStatus.CREATED);
    }
}


