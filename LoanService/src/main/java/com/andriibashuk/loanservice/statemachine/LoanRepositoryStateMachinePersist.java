package com.andriibashuk.loanservice.statemachine;

import com.andriibashuk.loanservice.entity.Loan;
import com.andriibashuk.loanservice.repository.LoanStateMachineRepository;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.data.RepositoryStateMachinePersist;
import org.springframework.statemachine.service.StateMachineSerialisationService;

public class LoanRepositoryStateMachinePersist <S, E> extends RepositoryStateMachinePersist<Loan, S, E> {

    private final LoanStateMachineRepository loanRepository;

    /**
     * Instantiates a new jpa repository state machine persist.
     *
     * @param loanRepository the jpa state machine repository
     */
    public LoanRepositoryStateMachinePersist(LoanStateMachineRepository loanRepository) {
        super();
        this.loanRepository = loanRepository;
    }

    /**
     * Instantiates a new jpa repository state machine persist.
     *
     * @param loanRepository the jpa state machine repository
     * @param serialisationService the serialisation service
     */
    public LoanRepositoryStateMachinePersist(LoanStateMachineRepository loanRepository,
                                            StateMachineSerialisationService<S, E> serialisationService) {
        super(serialisationService);
        this.loanRepository = loanRepository;
    }
    @Override
    protected LoanStateMachineRepository getRepository() {
        return this.loanRepository;
    }

    @Override
    protected Loan build(StateMachineContext<S, E> context, Object contextObj, byte[] serialisedContext) {
        Loan loan = this.loanRepository.findById(context.getId()).orElse(new Loan());
        loan.setStatus(context.getState() != null ? Loan.Status.valueOf(context.getState().toString()) : null);
        loan.setStateMachineContext(serialisedContext);
        return loan;
    }
}
