package com.andriibashuk.applicationservice.statemachine;

import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.repository.ApplicationStateMachineRepository;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.service.StateMachineSerialisationService;
import org.springframework.statemachine.data.RepositoryStateMachinePersist;

public class ApplicationRepositoryStateMachinePersist<S, E> extends RepositoryStateMachinePersist<Application, S, E> {

    private final ApplicationStateMachineRepository applicationRepository;

    /**
     * Instantiates a new jpa repository state machine persist.
     *
     * @param applicationRepository the jpa state machine repository
     */
    public ApplicationRepositoryStateMachinePersist(ApplicationStateMachineRepository applicationRepository) {
        super();
        this.applicationRepository = applicationRepository;
    }

    /**
     * Instantiates a new jpa repository state machine persist.
     *
     * @param applicationRepository the jpa state machine repository
     * @param serialisationService the serialisation service
     */
    public ApplicationRepositoryStateMachinePersist(ApplicationStateMachineRepository applicationRepository,
                                                    StateMachineSerialisationService<S, E> serialisationService) {
        super(serialisationService);
        this.applicationRepository = applicationRepository;
    }
    @Override
    protected ApplicationStateMachineRepository getRepository() {
        return this.applicationRepository;
    }

    @Override
    protected Application build(StateMachineContext<S, E> context, Object contextObj, byte[] serialisedContext) {
        Application application = this.applicationRepository.findById(context.getId()).orElse(new Application());
        application.setStatus(context.getState() != null ? Application.Status.valueOf(context.getState().toString()) : null);
        application.setStateMachineContext(serialisedContext);
        return application;
    }
}
