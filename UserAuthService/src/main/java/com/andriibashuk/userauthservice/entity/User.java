package com.andriibashuk.userauthservice.entity;

import com.andriibashuk.userauthservice.validation.ValidEmail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.ZonedDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "password")
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uc_users_email", columnNames = {"email"})
})
public class User implements UserDetails {
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

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Audit> audits = new LinkedHashSet<>();

    public void addAudits(Set<Audit> audits)
    {
        for (Audit audit : audits) {
            this.audits.add(audit);
            audit.setUser(this);
        }
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PhoneNumber> phoneNumbers = new LinkedHashSet<>();

    public void addPhoneNumbers(Set<PhoneNumber> phoneNumbers)
    {
        for (PhoneNumber phoneNumber : phoneNumbers) {
            this.phoneNumbers.add(phoneNumber);
            phoneNumber.setUser(this);
        }
    }

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

    public enum Gender {
        MALE,
        FEMALE
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Address> addresses = new LinkedHashSet<>();

    public void addAddresses(Set<Address> addresses)
    {
        for (Address address : addresses) {
            this.addresses.add(address);
            address.setUser(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User client = (User) o;
        return getId() != null && Objects.equals(getId(), client.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}