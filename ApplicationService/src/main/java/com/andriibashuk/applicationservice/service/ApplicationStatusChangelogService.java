package com.andriibashuk.applicationservice.service;

import com.andriibashuk.applicationservice.entity.Application;

public interface ApplicationStatusChangelogService {
    public void create(long applicationId, Application.Status fromStatus, Application.Status toStatus, Long userId);
}
