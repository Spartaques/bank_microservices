package com.andriibashuk.loanservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.data.RepositoryStateMachine;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "loans", indexes = {
        @Index(name = "idx_loan_userid", columnList = "userId"),
        @Index(name = "idx_loan_clientid", columnList = "clientId"),
        @Index(name = "idx_loan_applicationid", columnList = "applicationId")
})
@WithStateMachine
public class Loan extends RepositoryStateMachine implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = "issued")
    @Min(value = 0)
    @Max(value = 1000)
    private int amount;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    @NotNull
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
       NEW, PAID, PROLONGED, OVERDUE
    }

    public enum Event {
        PAY, OVERDUE, PROLONG
    }

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "balance_id")
    private Balance balance;

    @Positive
    private Long userId;

    @Positive
    @NotNull
    private Long clientId;

    @Positive
    private Long applicationId;
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Transaction> transactions;
}