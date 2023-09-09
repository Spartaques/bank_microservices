package com.andriibashuk.userauthservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "audit")
@ToString
@Immutable
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated
    @Column(nullable = false)
    @NotNull
    private Event event;

    @JsonIgnore
    @Column(updatable = false, columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private ZonedDateTime createdDate;
    public enum Event {
        LOAN_APPROVED,
        LOAN_ISSUED,
        LOAN_DENIED,
        CLIENT_CREATED,
        CLIENT_CANCELED
    }
}