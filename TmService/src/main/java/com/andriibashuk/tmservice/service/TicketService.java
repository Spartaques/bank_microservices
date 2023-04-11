package com.andriibashuk.tmservice.service;

import com.andriibashuk.tmservice.entity.Ticket;
import com.andriibashuk.tmservice.repository.TicketRepository;
import com.andriibashuk.tmservice.statemachine.TicketPersistingStateMachineInterceptor;
import lombok.extern.java.Log;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Log
public class TicketService {
    private TicketRepository ticketRepository;

    private final StateMachineFactory<Ticket.Status, Ticket.Event> factory;

    private final StateMachinePersister<Ticket.Status, Ticket.Event, String> persister;

    public TicketService(TicketRepository ticketRepository, StateMachineFactory<Ticket.Status, Ticket.Event> factory, TicketPersistingStateMachineInterceptor<Ticket.Status, Ticket.Event, String> ticketPersistingStateMachineInterceptor) {
        this.ticketRepository = ticketRepository;
        this.factory = factory;
        this.persister = new DefaultStateMachinePersister<>(ticketPersistingStateMachineInterceptor);
    }

    public void create(Long clientId, String phone, Ticket.Status status, Ticket.Type type, Map<String, String> meta) {
        Ticket ticket = new Ticket();
        ticket.setClientId(clientId);
        ticket.setStatus(status);
        ticket.setType(type);
        ticket.setPhone(phone);
        ticket.setMeta(meta);
        ticketRepository.save(ticket);
        StateMachine<Ticket.Status, Ticket.Event> stateMachine = factory.getStateMachine(ticket.getMachineId());
        stateMachine.start();
        try {
            persister.persist(stateMachine, ticket.getMachineId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info("ticket created: "+ticket);
    }
}
