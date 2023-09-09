package com.andriibashuk.userauthservice.service;

import com.andriibashuk.userauthservice.entity.*;
import com.andriibashuk.userauthservice.exception.UserNotFoundException;
import com.andriibashuk.userauthservice.exception.WrongRoleIdsException;
import com.andriibashuk.userauthservice.exception.WrongRolesCountException;
import com.andriibashuk.userauthservice.repository.UserCommentRepository;
import com.andriibashuk.userauthservice.repository.RoleRepository;
import com.andriibashuk.userauthservice.repository.UserRepository;
import com.andriibashuk.userauthservice.response.UserResponse;
import com.andriibashuk.userauthservice.security.JWTUtil;
import org.modelmapper.ModelMapper;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Log
public class UserServiceImpl implements UserService {
    PasswordEncoder passwordEncoder;

    UserRepository userRepository;

    RoleRepository roleRepository;
    private final UserCommentRepository commentRepository;
    private final ModelMapper modelMapper;

    JWTUtil jwtUtil;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, JWTUtil jwtUtil, RoleRepository roleRepository, UserCommentRepository commentRepository, ModelMapper modelMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.roleRepository = roleRepository;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }
    @Transactional
    @Override
    public UserResponse register(String firstName,
                                 String lastName,
                                 String email,
                                 String password,
                                 Short age,
                                 User.Gender gender,
                                 Set<Long> rolesIds,
                                 Set<Address> addresses,
                                 Set<PhoneNumber> phoneNumbers,
                                 Set<UserComment> comments,
                                 Set<Audit> audits) {
        List<Role> roles = roleRepository.findByIdIn(rolesIds);
        if(rolesIds.isEmpty()) {
            throw new WrongRolesCountException("Wrong roles", HttpStatus.BAD_REQUEST, "WRONG_ROLES_COUNT");
        }
        if(rolesIds.size() != roles.size()) {
            throw new WrongRoleIdsException("Wrong roles", HttpStatus.BAD_REQUEST, "WRONG_ROLES");
        }
        log.info("Starting registering user");
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAge(age);
        user.setGender(gender);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles);
        user.addAddresses(addresses);
        user.addPhoneNumbers(phoneNumbers);
        user.addAudits(audits);
        commentRepository.saveAll(comments);
        commentRepository.flush();
        userRepository.save(user);
        userRepository.flush();
        log.info("User created in database");
        return modelMapper.map(user, UserResponse.class);
    }

    @Transactional
    @Override
    public String login(String email, String password) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if(byEmail.isEmpty()) {
            throw new UserNotFoundException("user not found", HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
        }
        User user = byEmail.get();

        Set<String> privileges = getPrivileges(user.getRoles());

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserNotFoundException("user not found", HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
        }

        return jwtUtil.generateToken(user, privileges);
    }

    @Override
    public ArrayList<User> getAll() {
        return null;
    }


    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private Set<String> getPrivileges(Collection<Role> roles) {

        Set<String> privileges = new HashSet<>();
        Set<Privilege> collection = new HashSet<>();
        for (Role role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(Set<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
