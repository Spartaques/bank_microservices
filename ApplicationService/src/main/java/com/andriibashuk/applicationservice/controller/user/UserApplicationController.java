package com.andriibashuk.applicationservice.controller.user;

import com.andriibashuk.applicationservice.request.ApproveApplicationRequest;
import com.andriibashuk.applicationservice.response.ApplicationResponse;
import com.andriibashuk.applicationservice.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path = "user")
public class UserApplicationController {
    ApplicationService applicationService;

    public UserApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PreAuthorize("hasAuthority('APPLICATION_APPROVE')")
    @PatchMapping("/approve/{id}")
    public ResponseEntity<ApplicationResponse> newApplication(@PathVariable("id") Long id, @Valid @RequestBody ApproveApplicationRequest approveApplicationRequest, Principal principal) {
        return new ResponseEntity<>(applicationService.approve(id, Long.parseLong(principal.getName()), approveApplicationRequest.getApprovedAmount()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('APPLICATION_APPROVE')")
    @PatchMapping("/deny/{id}")
    public ResponseEntity<ApplicationResponse> deny(@PathVariable("id") Long id) {
        return new ResponseEntity<>(applicationService.deny(id), HttpStatus.OK);
    }
}
