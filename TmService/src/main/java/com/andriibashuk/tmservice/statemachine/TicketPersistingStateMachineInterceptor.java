package com.andriibashuk.tmservice.statemachine;

import com.andriibashuk.tmservice.repository.TicketStateMachineRepository;
import lombok.extern.java.Log;
import org.springframework.messaging.Message;
import org.springframework.statemachine.*;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.region.Region;
import org.springframework.statemachine.state.AbstractState;
import org.springframework.statemachine.state.HistoryPseudoState;
import org.springframework.statemachine.state.PseudoState;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.*;
import org.springframework.statemachine.transition.Transition;
import org.springframework.statemachine.transition.TransitionKind;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Function;

@Log
@Service
public class TicketPersistingStateMachineInterceptor<S, E, T> extends StateMachineInterceptorAdapter<S, E>
        implements StateMachinePersist<S, E, T>, StateMachineRuntimePersister<S, E, T> {

    private Function<StateMachine<S, E>, Map<Object, Object>> extendedStateVariablesFunction = new AllVariablesFunction<>();
    private final TiicketRepositoryStateMachinePersist<S, E> persist;

    public TicketPersistingStateMachineInterceptor(TicketStateMachineRepository ticketStateMachineRepository) {
        persist = new TiicketRepositoryStateMachinePersist<S, E>(ticketStateMachineRepository);
    }

    @Override
    public void write(StateMachineContext<S, E> context, T contextObj) throws Exception {
        log.info("Should be write here");
        persist.write(context, contextObj);
    }

    @Override
    public StateMachineContext<S, E> read(T contextObj) throws Exception {
        log.info("Should be read here");
        return persist.read(contextObj);
    }

    @Override
    public StateMachineInterceptor<S, E> getInterceptor() {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void preStateChange(State<S, E> state, Message<E> message, Transition<S, E> transition,
                               StateMachine<S, E> stateMachine, StateMachine<S, E> rootStateMachine) {

        log.info("preStateChange with stateMachine " + stateMachine);
        log.info("preStateChange with root stateMachine " + rootStateMachine);
        log.info("preStateChange with state " + state);
        // try to persist context and in case of failure, interceptor
        // call chain aborts transition
        // TODO: should probably come up with a policy vs. not force feeding this functionality
        try {
            write(buildStateMachineContext(stateMachine, rootStateMachine, state, message), (T)stateMachine.getId());
        } catch (Exception e) {
            throw new StateMachineException("Unable to persist stateMachineContext", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void postStateChange(State<S, E> state, Message<E> message, Transition<S, E> transition,
                                StateMachine<S, E> stateMachine, StateMachine<S, E> rootStateMachine) {

        log.info("postStateChange with stateMachine " + stateMachine);
        log.info("postStateChange with root stateMachine " + rootStateMachine);
        log.info("postStateChange with state " + state);
        // initial transitions are never intercepted as those cannot fail or get aborted.
        // for now, handle persistence in post state change
        // TODO: consider intercept initial transition, but not aborting if error is thrown?
        if (state != null && transition != null && transition.getKind() == TransitionKind.INITIAL) {
            try {
                write(buildStateMachineContext(stateMachine, rootStateMachine, state, message), (T)stateMachine.getId());
            } catch (Exception e) {
                throw new StateMachineException("Unable to persist stateMachineContext", e);
            }
        }
    }

    /**
     * Sets the function creating extended state variables.
     *
     * @param extendedStateVariablesFunction the extended state variables function
     */
    public void setExtendedStateVariablesFunction(
            Function<StateMachine<S, E>, Map<Object, Object>> extendedStateVariablesFunction) {
        Assert.notNull(extendedStateVariablesFunction, "'extendedStateVariablesFunction' cannot be null");
        this.extendedStateVariablesFunction = extendedStateVariablesFunction;
    }

    /**
     * Builds the state machine context. Note, for backward compatibility this
     * method doesn't pass event or headers into a {@link StateMachineContext}.
     * Implementor of this class, if using this method should move over to
     * {@link #buildStateMachineContext(StateMachine, StateMachine, State, Message)}.
     *
     * @param stateMachine the state machine
     * @param rootStateMachine the root state machine
     * @param state the state
     * @return the state machine context
     * @deprecated in favour of {@link #buildStateMachineContext(StateMachine, StateMachine, State, Message)}
     */
    protected StateMachineContext<S, E> buildStateMachineContext(StateMachine<S, E> stateMachine,
                                                                 StateMachine<S, E> rootStateMachine, State<S, E> state) {
        return buildStateMachineContext(stateMachine, rootStateMachine, state, null);
    }

    /**
     * Builds the state machine context.
     *
     * @param stateMachine the state machine
     * @param rootStateMachine the root state machine
     * @param state the state
     * @param message the message
     * @return the state machine context
     */
    protected StateMachineContext<S, E> buildStateMachineContext(StateMachine<S, E> stateMachine,
                                                                 StateMachine<S, E> rootStateMachine, State<S, E> state, Message<E> message) {
        ExtendedState extendedState = new DefaultExtendedState();
        extendedState.getVariables().putAll(extendedStateVariablesFunction.apply(stateMachine));

        List<StateMachineContext<S, E>> childs = new ArrayList<StateMachineContext<S, E>>();
        List<String> childRefs = new ArrayList<>();
        S id = null;
        if (state.isSubmachineState()) {
            id = getDeepState(state);
        } else if (state.isOrthogonal()) {
            if (stateMachine.getState().isOrthogonal()) {
                Collection<Region<S, E>> regions = ((AbstractState<S, E>)state).getRegions();
                for (Region<S, E> r : regions) {
                    // realistically we can only add refs because reqions are independent
                    // and when restoring, those child contexts need to get dehydrated
                    childRefs.add(r.getId());
                }
            }
            id = state.getId();
        } else {
            id = state.getId();
        }

        // building history state mappings
        Map<S, S> historyStates = new HashMap<S, S>();
        PseudoState<S, E> historyState = ((AbstractStateMachine<S, E>) stateMachine).getHistoryState();
        if (historyState != null) {
            historyStates.put(null, ((HistoryPseudoState<S, E>)historyState).getState().getId());
        }
        Collection<State<S, E>> states = stateMachine.getStates();
        for (State<S, E> ss : states) {
            if (ss.isSubmachineState()) {
                StateMachine<S, E> submachine = ((AbstractState<S, E>) ss).getSubmachine();
                PseudoState<S, E> ps = ((AbstractStateMachine<S, E>) submachine).getHistoryState();
                if (ps != null) {
                    State<S, E> pss = ((HistoryPseudoState<S, E>)ps).getState();
                    if (pss != null) {
                        historyStates.put(ss.getId(), pss.getId());
                    }
                }
            }
        }
        E event = message != null ? message.getPayload() : null;
        Map<String, Object> eventHeaders = message != null ? message.getHeaders() : null;
        return new DefaultStateMachineContext<S, E>(childRefs, childs, id, event, eventHeaders, extendedState,
                historyStates, stateMachine.getId());
    }

    private S getDeepState(State<S, E> state) {
        Collection<S> ids1 = state.getIds();
        @SuppressWarnings("unchecked")
        S[] ids2 = (S[]) ids1.toArray();
        // TODO: can this be empty as then we'd get error?
        return ids2[ids2.length-1];
    }

    private static class AllVariablesFunction<S, E> implements Function<StateMachine<S, E>, Map<Object, Object>> {

        @Override
        public Map<Object, Object> apply(StateMachine<S, E> stateMachine) {
            return stateMachine.getExtendedState().getVariables();
        }
    }
}
