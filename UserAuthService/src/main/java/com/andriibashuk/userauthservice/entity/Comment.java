package com.andriibashuk.userauthservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.SqlTypes;

import java.time.ZonedDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Immutable
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "entity_type", discriminatorType = DiscriminatorType.INTEGER)
@Table(name = "comment", uniqueConstraints =  @UniqueConstraint(
        name = "entity_type_entity_id",
        columnNames = {
                "entity_type",
                "entity_id"
        }
))
public abstract class Comment {
    @Column(name = "text", nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private String text;

    @Column(updatable = false, columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private ZonedDateTime createdDate;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "entity_type", nullable = false)
    private ENTITY_TYPE entityType;

    @Column(name = "entity_id", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private int entityId;

    public enum ENTITY_TYPE {
        USER
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return entityId == comment.entityId && entityType == comment.entityType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityType, entityId);
    }
}