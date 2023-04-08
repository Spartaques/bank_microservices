package com.andriibashuk.applicationservice.repository;

import com.andriibashuk.applicationservice.entity.Application;
import org.springframework.statemachine.data.StateMachineRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationStateMachineRepository extends StateMachineRepository<Application> {
}
