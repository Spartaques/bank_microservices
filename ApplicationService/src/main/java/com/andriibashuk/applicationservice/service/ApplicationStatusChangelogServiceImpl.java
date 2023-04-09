package com.andriibashuk.applicationservice.service;

import com.andriibashuk.applicationservice.entity.Application;
import com.andriibashuk.applicationservice.entity.ApplicationStatusChangeLog;
import com.andriibashuk.applicationservice.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationStatusChangelogServiceImpl implements ApplicationStatusChangelogService{

    private final ApplicationRepository applicationRepository;

    public ApplicationStatusChangelogServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public void create(long applicationId, Application.Status fromStatus, Application.Status toStatus, Long userId) {
        Optional<Application> optionalApplication = applicationRepository.findById(applicationId);
        if(optionalApplication.isEmpty()) {
            throw new RuntimeException();
        }
        Application application = optionalApplication.get();
        ApplicationStatusChangeLog applicationStatusChangeLog = new ApplicationStatusChangeLog();
        applicationStatusChangeLog.setFromStatus(fromStatus);
        applicationStatusChangeLog.setToStatus(toStatus);
        applicationStatusChangeLog.setUserId(userId);
        applicationStatusChangeLog.setApplication(application);
        application.setApplicationStatusChangeLog(applicationStatusChangeLog);
        applicationRepository.save(application);
    }
}
