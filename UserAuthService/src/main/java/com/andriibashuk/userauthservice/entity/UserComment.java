package com.andriibashuk.userauthservice.entity;

import jakarta.persistence.*;
import lombok.ToString;

import java.util.Objects;


@DiscriminatorValue("0")
@Entity
public class UserComment extends Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;
}
