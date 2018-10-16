package com.example.demo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.model.DemoUser;

@Repository
public interface DemoUserRepository extends JpaRepository<DemoUser, String> {
	public DemoUser findByLoginId(String loginId);
}
