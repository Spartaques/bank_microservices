package com.andriibashuk.userauthservice.repository;

import com.andriibashuk.userauthservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    long countByIdIn(Collection<Long> ids);
    Role findByName(String name);

    List<Role> findByIdIn(Collection<Long> ids);
}
