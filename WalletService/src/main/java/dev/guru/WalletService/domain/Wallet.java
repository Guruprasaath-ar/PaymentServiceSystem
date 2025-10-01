package dev.guru.WalletService.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private Long userId;
    private double balance;
    @LastModifiedDate
    private Instant updatedAt;

    public Wallet() {

    }

    public Wallet(Builder builder) {
        this.userId = builder.userId;
        this.balance = builder.balance;
    }

    public static class Builder{
        private Long userId;
        private double balance;

        public Builder withUserId(Long userId){
            this.userId = userId;
            return this;
        }

        public Builder withBalance(double balance){
            this.balance = balance;
            return this;
        }

        public Wallet build(){
            return new Wallet(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void debit(double balance){
        this.balance -= balance;
    }

    public void credit(double balance){
        this.balance += balance;
    }

}
