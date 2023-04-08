package com.andriibashuk.userauthservice.listener;

import com.andriibashuk.userauthservice.entity.Privilege;
import com.andriibashuk.userauthservice.entity.Role;
import com.andriibashuk.userauthservice.repository.PrivilegeRepository;
import com.andriibashuk.userauthservice.repository.RoleRepository;
import com.andriibashuk.userauthservice.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;

    public SetupDataLoader(UserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Transactional
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup)
            return;
        Privilege applicationApprove
                = createPrivilegeIfNotFound("APPLICATION_APPROVE");
        Privilege applicationUpdate
                = createPrivilegeIfNotFound("APPLICATION_UPDATE");

        List<Privilege> applicationVerifierPrivileges = new ArrayList<>();
        applicationVerifierPrivileges.add(applicationApprove);
        List<Privilege> superAdminPrivileges = new ArrayList<>();
        superAdminPrivileges.add(applicationApprove);
        superAdminPrivileges.add(applicationUpdate);
        createRoleIfNotFound("ROLE_APPLICATION_VERIFIER", applicationVerifierPrivileges);
        createRoleIfNotFound("ROLE_SUPER_ADMIN", superAdminPrivileges);

        alreadySetup = true;
    }

    private Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    private Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
        }
        role.setPrivileges(privileges);
        roleRepository.save(role);
        return role;
    }
}
