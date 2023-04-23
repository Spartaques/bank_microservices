package com.andriibashuk.applicationservice.statemachine;

import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.statemachine.actions.BaseAction;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;

import java.util.EnumSet;

@Configuration
public class ApplicationStateMachineConfig {
    @Autowired
    ApplicationPersistingStateMachineInterceptor<Application.Status, Application.Event, String> applicationPersistingStateMachineInterceptor;
    @Log
    @Configuration
    @EnableStateMachineFactory(contextEvents = false)
    public static class LoanStateMachineConfiguration extends EnumStateMachineConfigurerAdapter<Application.Status, Application.Event> {
        private final StateMachineListener<Application.Status, Application.Event> stateMachineListener;
        private final BaseAction baseAction;

        public LoanStateMachineConfiguration(StateMachineListener<Application.Status, Application.Event> stateMachineListener, BaseAction baseAction) {
            this.stateMachineListener = stateMachineListener;
            this.baseAction = baseAction;
        }

        @Override
        public void configure(StateMachineConfigurationConfigurer<Application.Status, Application.Event> config) throws Exception {

            config.withConfiguration().listener(this.stateMachineListener);
        }

        @Override
        public void configure(StateMachineStateConfigurer<Application.Status, Application.Event> states) throws Exception {
            states
                    .withStates()
                    .initial(Application.Status.NEW)
                    .states(EnumSet.allOf(Application.Status.class));
        }

        @Override
        public void configure(StateMachineTransitionConfigurer<Application.Status, Application.Event> transitions) throws Exception {
            transitions
                    .withExternal()
                    .source(Application.Status.NEW).target(Application.Status.DENIED)
                    .event(Application.Event.DENY)
                    .action(baseAction)
                    .and()
                    .withExternal()
                    .source(Application.Status.NEW).target(Application.Status.APPROVED)
                    .event(Application.Event.APPROVE)
                    .action(baseAction)
                    .and()
                    .withExternal()
                    .source(Application.Status.APPROVED).target(Application.Status.DENIED)
                    .event(Application.Event.DENY)
                    .action(baseAction)
                    .and()
                    .withExternal()
                    .source(Application.Status.APPROVED).target(Application.Status.SIGNED)
                    .event(Application.Event.SIGN)
                    .action(baseAction);
        }
    }

    @Bean
    DefaultStateMachinePersister<Application.Status, Application.Event, String> defaultStateMachinePersister() {
        return new DefaultStateMachinePersister<>(applicationPersistingStateMachineInterceptor);
    }
}