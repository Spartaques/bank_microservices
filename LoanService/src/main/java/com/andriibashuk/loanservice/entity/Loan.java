package com.andriibashuk.loanservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.data.RepositoryStateMachine;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
@WithStateMachine
public class Loan extends RepositoryStateMachine implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = "issued")
    @Setter
    @Min(value = 0)
    @Max(value = 1000)
    private int amount;

    @NotNull
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    @NotNull
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Lob
    @Column(name = "state_machine_context", length = 10240)
    private byte[] stateMachineContext;

    @Override
    public String getMachineId() {
        return this.id.toString();
    }

    @Override
    public String getState() {
        return this.status.name();
    }

    @Override
    public byte[] getStateMachineContext() {
        return stateMachineContext;
    }

    public void setStateMachineContext(byte[] stateMachineContext) {
        this.stateMachineContext = stateMachineContext;
    }

    public enum Status {
        NEW, PROCESSING, APPROVED, ISSUED, CANCELLED, DECLINED, PAID, PROLONGED
    }

    public enum Event {
        START_PROCESSING, APPROVE, ISSUE, PAY, DECLINE, CANCEL, PROLONG
    }

    @Setter
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "balance_id")
    private Balance balance;

    private Long user_id;
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private Set<Transaction> transactions;
}