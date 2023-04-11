package com.andriibashuk.applicationservice.service;

import com.andriibashuk.applicationservice.entity.Application;

public interface ApplicationStatusChangelogService {
    public void create(Application application, Application.Status fromStatus, Application.Status toStatus, Long userId);
}
