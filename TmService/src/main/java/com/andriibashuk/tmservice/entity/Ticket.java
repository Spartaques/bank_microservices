package com.andriibashuk.tmservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.statemachine.data.RepositoryStateMachine;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Ticket extends RepositoryStateMachine implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long clientId;

    @Column(length = 20)
    private String phone;

    private Long userId;

    @Column(nullable = false)
    private Status status;

    @Column(updatable = false, columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

    @Lob
    @Column(name = "state_machine_context", length = 10240)
    private byte[] stateMachineContext;
    @Column(name="meta_value")
    @MapKeyJoinColumn(name="meta_key")
    @ElementCollection(fetch = FetchType.LAZY)
    private Map<String, String> meta;

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
        READY_TO_HANDLE,
        HANDLED,
        WAITING,
        COMPLETED,
        FAILED,
        OUTDATED
    }

    public static enum Event {
        MOVE_TO_READY_FOR_HANDLE,
        HANDLE,
        MOVE_TO_WAITING,
        MOVE_TO_COMPLETE,
        MOVE_TO_FAILED,
        MOVE_TO_OUTDATED
    }
}
