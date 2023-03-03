package ru.maxima.springboottest.ProjectSpringBoot1.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void doAdminThings() {
        System.out.println("This staff can make only admins");
    }
}
