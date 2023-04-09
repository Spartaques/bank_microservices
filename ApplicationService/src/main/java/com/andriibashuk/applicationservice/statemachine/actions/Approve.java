package com.andriibashuk.applicationservice.statemachine.actions;

import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.service.ApplicationService;
import com.andriibashuk.applicationservice.service.ApplicationStatusChangelogService;
import com.andriibashuk.applicationservice.service.UserService;
import lombok.extern.java.Log;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Service;

@Service
@Log
public class Approve implements Action<Application.Status, Application.Event> {
    ApplicationStatusChangelogService applicationStatusChangelogService;

    public Approve(ApplicationStatusChangelogService applicationStatusChangelogService) {
        this.applicationStatusChangelogService = applicationStatusChangelogService;
    }

    @Override
    public void execute(StateContext<Application.Status, Application.Event> stateContext) {
        Long userId = UserService.authenticated() ? Long.valueOf(UserService.getUser().get().getUsername()) : null;
        applicationStatusChangelogService.create(Long.parseLong(stateContext.getStateMachine().getId()), stateContext.getSource().getId(), stateContext.getTarget().getId(), userId);
    }
}
