package com.andriibashuk.loanservice.statemachine;

import com.andriibashuk.loanservice.entity.Loan;
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
public class LoanStateMachineConfig {
    @Log
    @Configuration
    @EnableStateMachineFactory(contextEvents = false)
    public static class LoanStateMachineConfiguration extends EnumStateMachineConfigurerAdapter<Loan.Status, Loan.Event> {

        private final BaseAction baseAction;

        private final StateMachineListener<Loan.Status, Loan.Event> stateMachineListener;

        public LoanStateMachineConfiguration(BaseAction baseAction, StateMachineListener<Loan.Status, Loan.Event> stateMachineListener) {
            this.baseAction = baseAction;
            this.stateMachineListener = stateMachineListener;
        }

        @Override
        public void configure(StateMachineConfigurationConfigurer<Loan.Status, Loan.Event> config) throws Exception {

            config.withConfiguration().listener(this.stateMachineListener);
        }

        @Override
        public void configure(StateMachineStateConfigurer<Loan.Status, Loan.Event> states) throws Exception {
            states
                    .withStates()
                    .initial(Loan.Status.NEW)
                    .states(EnumSet.allOf(Loan.Status.class));
        }

        @Override
        public void configure(StateMachineTransitionConfigurer<Loan.Status, Loan.Event> transitions) throws Exception {
            transitions
                    .withExternal()
                    .source(Loan.Status.NEW).target(Loan.Status.OVERDUE)
                    .event(Loan.Event.OVERDUE)
                    .action(baseAction)
                    .and()
                    .withExternal()
                    .source(Loan.Status.NEW).target(Loan.Status.PAID)
                    .event(Loan.Event.PAY)
                    .action(baseAction)
                    .and()
                    .withExternal()
                    .action(baseAction)
                    .and()
                    .withExternal()
                    .source(Loan.Status.PAID).target(Loan.Status.PROLONGED)
                    .event(Loan.Event.PROLONG)
                    .action(baseAction)
            ;
        }
    }
}