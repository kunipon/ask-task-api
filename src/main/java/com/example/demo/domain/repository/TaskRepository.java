package com.example.demo.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.model.Task;

@Transactional
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    public List<Task> findByUsername(String username);
    public List<Task> findByUsernameOrderByDeadlineAscTitleAsc(String username);
    
    @PostAuthorize("returnObject.username == authentication.name")
    public Task findOne(long id);
}
