package com.andriibashuk.clientauthservice.entity;

import com.andriibashuk.clientauthservice.validation.ValidEmail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "password")
@Entity
@Table(name = "clients", uniqueConstraints = {
        @UniqueConstraint(name = "uc_clients_email", columnNames = {"email"})
})
public class Client implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Length(min = 3, max = 75)
    @NotBlank
    @NotEmpty
    @NotNull
    @Column(length = 75, nullable = false)
    private String firstName;
    @Column(length = 75, nullable = false)
    @Length(min = 3, max = 75)
    @NotBlank
    @NotEmpty
    @NotNull
    private String lastName;
    @Column(length = 150, nullable = false)
    @Length(min = 3, max = 150)
    @NotBlank
    @NotEmpty
    @NotNull
    @ValidEmail
    private String email;
    @Column(length = 30, nullable = false)
    @Length(min = 3, max = 30)
    @NotBlank
    @NotEmpty
    @NotNull
    private String phone;
    @Column(nullable = false)
    @JsonIgnore
    @NotBlank
    @NotEmpty
    @NotNull
    private String password;
    @Column(updatable = false, columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private ZonedDateTime createdDate;
    @Column(columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    private ZonedDateTime lastModifiedDate;
    private Long createdBy;
    private Long lastModifiedBy;
    @Range(min = 18, max = 150)
    @Column(nullable = false)
    @NotNull
    @PositiveOrZero
    private Short age;
    @Column(nullable = false)
    @NotNull
    private Gender gender;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static enum Gender {
        MALE,
        FEMALE
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Client client = (Client) o;
        return getId() != null && Objects.equals(getId(), client.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}