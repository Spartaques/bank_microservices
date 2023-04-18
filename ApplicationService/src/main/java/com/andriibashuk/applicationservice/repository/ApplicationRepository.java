package com.andriibashuk.applicationservice.repository;

import com.andriibashuk.applicationservice.entity.Application;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotNull;
import org.hibernate.LockMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Application a where a.id = ?1")
    Optional<Application> findByIdLocked(Long id);
}
