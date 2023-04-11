package com.andriibashuk.tmservice.statemachine;

import com.andriibashuk.tmservice.entity.Ticket;
import com.andriibashuk.tmservice.statemachine.actions.*;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;

import java.util.EnumSet;

@Configuration
public class TicketStateMachineConfig {
    @Log
    @Configuration
    @EnableStateMachineFactory(contextEvents = false)
    public static class LoanStateMachineConfiguration extends EnumStateMachineConfigurerAdapter<Ticket.Status, Ticket.Event> {
        private final StateMachineListener<Ticket.Status, Ticket.Event> stateMachineListener;
        private final ReadyToHandle readyToHandle;
        private final Handle handle;
        private final Waiting waiting;

        private final Failed failed;

        private final Completed completed;

        private final Outdated outdated;

        public LoanStateMachineConfiguration(StateMachineListener<Ticket.Status, Ticket.Event> stateMachineListener, ReadyToHandle readyToHandle, Handle handle, Waiting waiting, Failed failed, Completed completed, Outdated outdated) {
            this.stateMachineListener = stateMachineListener;
            this.readyToHandle = readyToHandle;
            this.handle = handle;
            this.waiting = waiting;
            this.failed = failed;
            this.completed = completed;
            this.outdated = outdated;
        }

        @Override
        public void configure(StateMachineConfigurationConfigurer<Ticket.Status, Ticket.Event> config) throws Exception {

            config.withConfiguration().listener(this.stateMachineListener);
        }

        @Override
        public void configure(StateMachineStateConfigurer<Ticket.Status, Ticket.Event> states) throws Exception {
            states
                    .withStates()
                    .initial(Ticket.Status.NEW)
                    .states(EnumSet.allOf(Ticket.Status.class));
        }

        @Override
        public void configure(StateMachineTransitionConfigurer<Ticket.Status, Ticket.Event> transitions) throws Exception {
            transitions
                    .withExternal()
                    .source(Ticket.Status.NEW).target(Ticket.Status.READY_TO_HANDLE)
                    .event(Ticket.Event.MOVE_TO_READY_FOR_HANDLE)
                    .action(readyToHandle)
                    .and()
                    .withExternal()
                    .source(Ticket.Status.READY_TO_HANDLE).target(Ticket.Status.HANDLED)
                    .event(Ticket.Event.HANDLE)
                    .action(handle)
                    .and()
                    .withExternal()
                    .source(Ticket.Status.HANDLED).target(Ticket.Status.WAITING)
                    .event(Ticket.Event.MOVE_TO_WAITING)
                    .action(waiting);
        }
    }
}