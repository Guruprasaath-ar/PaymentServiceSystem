package dev.guru.UserService.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long  id;
    @Column(unique = true)
    private String userName;
    @Column(unique = true)
    private String email;
    private String password;
    private boolean isVerified;
    @CreatedDate
    @Column(updatable = false)
    private Instant createdDate;
    @LastModifiedDate
    private Instant updatedDate;

    public UserEntity() {

    }

    public UserEntity(Builder builder){
        this.userName = builder.userName;
        this.email = builder.email;
        this.password = builder.password;
        this.isVerified = builder.isVerified;
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class Builder {
        private final String userName;
        private final String email;
        private final String password;
        private boolean isVerified;

        public Builder(String userName, String email, String password) {
            this.userName = userName;
            this.email = email;
            this.password = password;
        }

        public Builder withVerified(boolean isVerified) {
            this.isVerified = isVerified;
            return this;
        }

        public UserEntity build() {
            return new UserEntity(this);
        }
    }

    public String toString(){
        return this.userName + " " + this.email + " " + this.password + " " + this.isVerified;
    }
}
