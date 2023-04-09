package com.andriibashuk.applicationservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
public class ApplicationStatusChangeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="applicationId")
    private Application application;

    @Column(updatable = false, columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    private Long userId;

    @Column(nullable = false)
    private Application.Status fromStatus;

    @Column(nullable = false)
    private Application.Status toStatus;
}
