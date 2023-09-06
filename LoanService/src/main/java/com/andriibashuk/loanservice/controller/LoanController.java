package com.andriibashuk.loanservice.controller;

import com.andriibashuk.loanservice.dto.ChangeLoanStatusDTO;
import com.andriibashuk.loanservice.dto.StoreLoanDTO;
import com.andriibashuk.loanservice.entity.Loan;
import com.andriibashuk.loanservice.repository.LoanRepository;
import com.andriibashuk.loanservice.service.LoanService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "loans",name = "loans")
public class LoanController {
    @Autowired
    private LoanService loanService;
    @Autowired
    private LoanRepository loanRepository;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Loan store(@Valid @RequestBody StoreLoanDTO storeLoanDTO) {
        return this.loanService.store(storeLoanDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{id}/change_status")
    public void changeStatus(@PathVariable Long id, @RequestBody ChangeLoanStatusDTO changeLoanStatusDTO)  {
        this.loanService.changeStatus(changeLoanStatusDTO.getEvent(), id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("{id}")
    public Loan update(@PathVariable Long id, @Valid @RequestBody StoreLoanDTO storeLoanDTO) {
        return this.loanService.store(storeLoanDTO, id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.loanRepository.deleteById(id);
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public Loan show(@PathVariable Long id) {
        Optional<Loan> loan = this.loanRepository.findById(id);

        if(loan.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return loan.get();
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<Loan> index()
    {
        Pageable pageable = PageRequest.of(
                0, 10);
        return this.loanRepository.findAll(pageable);
    }
}
