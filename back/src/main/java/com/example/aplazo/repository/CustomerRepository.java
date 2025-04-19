package com.example.aplazo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.aplazo.entity.Customer;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}