package com.project.TaskManger.task;

import com.project.TaskManger.category.CategoryRepository;
import com.project.TaskManger.exception.NotFoundException;
import com.project.TaskManger.security.config.JwtService;
import com.project.TaskManger.security.user.User;
import com.project.TaskManger.security.user.UserRepository;

import com.project.TaskManger.taskScheduling.TaskSchedulingDto;

import com.project.TaskManger.taskScheduling.TaskSchedulingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TaskSchedulingService taskSchedulingService;
    public void addTask(TaskDto request,String token){
        User user = getUser(token);
        Task task=new Task();
            task.setUser(user);
            task.setTitle(getUniqueTitle(request.getTitle(),user.getId()));
            task.setDescription(request.getDescription());
            task.setPriority(request.getPriority()==null?
                    Priority.MEDIUM:request.getPriority());
        task.setStatus(request.getStatus()==null?
                Status.TO_DO:request.getStatus());
        task.setCategory(categoryRepository.
                findCategoryByTitle(request.getCategoryTitle()).
                orElseThrow(() -> new NotFoundException("no category with name [%S]".formatted(request.getCategoryTitle()))));
        taskRepository.save(task);
    }
    @Transactional
    public void addTaskWithSchedule(TaskWithScheduleDto request,String token){
        User user=getUser(token);
        TaskDto task=new TaskDto(request.getTitle()
                ,request.getDescription(),
                request.getPriority()
                ,request.getStatus(),
                request.getCategoryTitle());
        addTask(task,token);
        int id = taskRepository.
                findTaskByUserIdAndTitle(user.getId(), request.getTitle()).
                orElseThrow(() ->new NotFoundException("task with title [%s] not found".formatted(request.getTitle()))).
                getId();
        TaskSchedulingDto taskWithSchedule=new TaskSchedulingDto(request.getStartDate(),request.getDueDate());
        taskSchedulingService.addTaskSchedule(id,taskWithSchedule);
    }

    public List<Task> getTasks(String token){
        User user = getUser(token);

        return taskRepository.findAllByUserId(user.getId());
    }
    public List<Task> getTaskByTitleStartWith(String title,String token){
        User user=getUser(token);
        return taskRepository.findAllByUserIdAndTitleStartsWith(user.getId(),title);
    }
    public void removeTask(int id,String token){
        User user=getUser(token);
        if(!taskRepository.existsTasksByIdAndUserId(id, user.getId()))
            throw new NotFoundException("task with id [%s] not found".formatted(id));
        taskRepository.deleteTaskByIdAndUserId(id, user.getId());
    }
    public void updateTask(int id,TaskDto update,String token){
        User user=getUser(token);
        Task task=taskRepository.findById(id).
                orElseThrow(() -> new NotFoundException("task with id [%s] not found".formatted(id)));
        boolean changes=false;
        String title = update.getTitle();
        if(title !=null&&!title.equals(task.getTitle())){
            task.setTitle(title);
            changes=true;
        }  if(update.getDescription()!=null&&!update.getDescription().equals(task.getDescription())){
            task.setDescription(update.getDescription());
            changes=true;
        }  if(update.getStatus()!=null&&!update.getStatus().equals(task.getStatus())){
            task.setStatus(update.getStatus());
            changes=true;
        }  if(update.getPriority()!=null&&!update.getPriority().equals(task.getPriority())){
            task.setPriority(update.getPriority());
            changes=true;
        }
        if(update.getCategoryTitle()!=null&&!update.getCategoryTitle().equals(task.getCategory().getTitle())){
            task.setCategory(categoryRepository.
                    findCategoryByTitle(title).
                    orElseThrow(() -> new
                            NotFoundException("no category with name [%S]".formatted(title))));
            changes=true;
        }
        if(changes){
            taskRepository.save(task);
        }
    }




    private String getUniqueTitle(String title,int id){
            String uniqueTitle=title;
            int index=1;
            while (taskRepository.existsByTitleAndUserId(uniqueTitle,id)){
                uniqueTitle=title;
                uniqueTitle+=index++;
            }
            return uniqueTitle;
    }


    private User getUser(String token) {
        token = token.substring(7);
        String email = jwtService.extractUsername(token);
        User user=userRepository.findByEmail(email).orElseThrow(() ->new UsernameNotFoundException("user with userName not found"));
        return user;
    }

}
