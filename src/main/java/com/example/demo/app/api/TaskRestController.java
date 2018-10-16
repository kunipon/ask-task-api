package com.example.demo.app.api;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.relativeTo;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.domain.model.Task;
import com.example.demo.domain.repository.TaskRepository;
import com.example.demo.domain.service.helper.SpringBeanUtilsHelper;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {
    
    @Autowired
    TaskRepository taskRepository;
    
    @GetMapping
    List<Task> getMyTasks(Principal principal) {
        List<Task> tasks = taskRepository.findByUsername(principal.getName());
        return tasks;
    }
    
    /**
     * 
     * TODO: filterの条件はクエリにつけたいよね
     * @param principal
     * @return
     */
    @GetMapping("/count")
    long getTaskCount(Principal principal) {
        // TODO: deadline validate
        
        return
            taskRepository.findByUsername(principal.getName())
                .stream()
                    .filter(tasks -> tasks.getDeadline().compareTo(LocalDate.now())>=0)
                    .filter(tasks -> !tasks.isFinished())
                    .count();
    }
    
    /**
     * 
     * TODO: filterの条件とかソートもクエリにつけたいよね
     * @param id
     * @param idtype DB内での通し番号か取得したリストのインデックスか
     * @return
     */
    @GetMapping("{id}")
    Task getTask(Principal principal, 
                @PathVariable long id,
                @RequestParam(name = "idtype", defaultValue = "db") String idtype) {
        if(idtype.equals("list")) {
            // 期日の一番せまっているものから数えて、ユーザーの指定したインデックスのものを返す LocalDate.now()
            // TODO: deadline validate
            return taskRepository
                    .findByUsernameOrderByDeadlineAscTitleAsc(principal.getName())
                    .stream()
                        .filter(tasks -> tasks.getDeadline().compareTo(LocalDate.now())>=0)
                        .filter(tasks -> !tasks.isFinished())
                        .collect(Collectors.toList())
                    .get((int) id);
        } else {
            return taskRepository.findOne(id);
        }
        
    }
    
    @PostMapping
    ResponseEntity<Void> postTask(@RequestBody Task task, Principal principal, UriComponentsBuilder uriBuilder) {
        task.setUsername(principal.getName());
        taskRepository.save(task);
        URI createdTaskUri = relativeTo(uriBuilder).withMethodCall(on(TaskRestController.class).getTask(principal, task.getId(), "db"))
                .build().encode().toUri();
        return ResponseEntity.created(createdTaskUri).build();
    }
    
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void putTask(@PathVariable long id, @RequestBody Task in_task) {
        Task task = taskRepository.findOne(id); // 所有者チェック
        in_task.setId(id);
        BeanUtils.copyProperties(in_task, task, SpringBeanUtilsHelper.getNullPropertyNames(in_task));
        taskRepository.save(task);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTask(@PathVariable long id) {
//        taskRepository.findOne(id); // 所有者チェック
        taskRepository.delete(id);
    }
}
