package com.andriibashuk.applicationservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.statemachine.data.RepositoryStateMachine;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Application extends RepositoryStateMachine implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(nullable = false)
    @Min(value = 0)
    @Max(value = 100000)
    private Integer requestedAmount;

    @Min(value = 0)
    @Max(value = 100000)
    private Integer approvedAmount;

    @Column(nullable = false)
    private Long clientId;

    private Long userId;

    @Column(nullable = false)
    private Status status;

    @Column(updatable = false, columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    private ApplicationStatusChangeLog applicationStatusChangeLog;

    @JsonIgnore
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

    public static enum Status {
        NEW,
        APPROVED,
        DENIED,
        SIGNED
    }

    public static enum Event {
        CREATE,
        APPROVE,
        DENY,
        SIGN
    }
}
