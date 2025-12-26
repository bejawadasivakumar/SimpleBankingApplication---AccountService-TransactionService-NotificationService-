package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.EmailNotification;

public interface EmailNotificationRepository extends JpaRepository<EmailNotification,Long> {

}
