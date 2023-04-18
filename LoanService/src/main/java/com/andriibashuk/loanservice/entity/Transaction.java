package com.andriibashuk.loanservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@Entity
public class Transaction implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    public void setAmount(int amount, Type type) {
        if(type == Type.PAYOUT) {
            amount =  -amount;
        }
        this.amount = amount;
    }

    @Column(name = "amount", nullable = false)
    @Max(value = 1000)
    private int amount;

    public enum Type {
        PAYOUT, PAYIN, INTEREST, PENALTY, OVERPAYMENT
    }

    @Setter
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="loanId")
    private Loan loan;
}
