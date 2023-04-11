package com.andriibashuk.tmservice.repository;

import com.andriibashuk.tmservice.entity.Ticket;
import org.springframework.statemachine.data.StateMachineRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketStateMachineRepository extends StateMachineRepository<Ticket> {
}
