package com.andriibashuk.tmservice.statemachine;

import com.andriibashuk.tmservice.entity.Ticket;
import com.andriibashuk.tmservice.repository.TicketStateMachineRepository;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.data.RepositoryStateMachinePersist;
import org.springframework.statemachine.service.StateMachineSerialisationService;

public class TiicketRepositoryStateMachinePersist<S, E> extends RepositoryStateMachinePersist<Ticket, S, E> {

    private final TicketStateMachineRepository ticketStateMachineRepository;

    /**
     * Instantiates a new jpa repository state machine persist.
     *
     * @param ticketStateMachineRepository the jpa state machine repository
     */
    public TiicketRepositoryStateMachinePersist(TicketStateMachineRepository ticketStateMachineRepository) {
        super();
        this.ticketStateMachineRepository = ticketStateMachineRepository;
    }

    /**
     * Instantiates a new jpa repository state machine persist.
     *
     * @param ticketStateMachineRepository the jpa state machine repository
     * @param serialisationService the serialisation service
     */
    public TiicketRepositoryStateMachinePersist(TicketStateMachineRepository ticketStateMachineRepository,
                                                StateMachineSerialisationService<S, E> serialisationService) {
        super(serialisationService);
        this.ticketStateMachineRepository = ticketStateMachineRepository;
    }
    @Override
    protected TicketStateMachineRepository getRepository() {
        return this.ticketStateMachineRepository;
    }

    @Override
    protected Ticket build(StateMachineContext<S, E> context, Object contextObj, byte[] serialisedContext) {
        Ticket ticket = this.ticketStateMachineRepository.findById(context.getId()).orElse(new Ticket());
        ticket.setStatus(context.getState() != null ? Ticket.Status.valueOf(context.getState().toString()) : null);
        ticket.setStateMachineContext(serialisedContext);
        return ticket;
    }
}
