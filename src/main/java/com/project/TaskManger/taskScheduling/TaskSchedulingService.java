package com.project.TaskManger.taskScheduling;

import com.project.TaskManger.exception.DuplicateResourceException;
import com.project.TaskManger.exception.NotFoundException;
import com.project.TaskManger.notification.Mail;
import com.project.TaskManger.notification.MailService;
import com.project.TaskManger.security.user.UserRepository;
import com.project.TaskManger.task.Priority;
import com.project.TaskManger.task.Status;
import com.project.TaskManger.task.Task;
import com.project.TaskManger.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskSchedulingService  {
    private final TaskSchedulingRepository taskSchedulingRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final Clock clock;
    private final MailService mailService;

    public void addTaskSchedule(int id,TaskSchedulingDto request){
        if(taskSchedulingRepository.existsTaskSchedulingByTaskId(id)){
            DuplicateResourceException duplicateResourceException = new DuplicateResourceException("task with id [%s] have schedule".formatted(id));
            log.error("error in duplicate task with id {}",id,duplicateResourceException);
            throw duplicateResourceException;
        }
        Task task = taskRepository.findById(id).orElseThrow(() -> {
            NotFoundException notFoundException = new NotFoundException("task with id [%s] NOT FOUND ".formatted(id));
            log.error("error in getting task with id {}",id,notFoundException);
            return notFoundException;
        });
        TaskScheduling taskScheduling=new TaskScheduling();
        taskScheduling.setStartDate(request.getStartDate());
        taskScheduling.setDueDate(request.getDueDate());
        taskScheduling.setTask(task);
        taskSchedulingRepository.save(taskScheduling);
    }
    public List<TaskScheduling> getTaskSchedules(int id){
        return taskSchedulingRepository.findAllByTask_Id(id);
    }
    public TaskScheduling getTaskSchedule(int id){
        return taskSchedulingRepository.findById(id).orElseThrow(() ->{
                NotFoundException notFoundException = new NotFoundException("TaskScheduling with id [%s] NOT FOUND ".formatted(id));
        log.error("error in getting TaskScheduling with id {}",id,notFoundException);
          return notFoundException;
        });
    }
    public void removeTaskSchedule(int id){
        if(!taskSchedulingRepository.existsTaskSchedulingById(id))
            throw new NotFoundException("resource with [%s] NOT FOUND".formatted(id));
        taskSchedulingRepository.deleteById(id);
    }
    public void updateTaskSchedule(int id,TaskSchedulingDto update){
        TaskScheduling taskScheduling = taskSchedulingRepository.findById(id).orElseThrow(() ->
                new NotFoundException("resource with [%s] NOT FOUND".formatted(id)));
        boolean changes=false;
        if(update.getStartDate()!=null&&!update.getDueDate().equals(taskScheduling.getStartDate())){
            taskScheduling.setStartDate(update.getStartDate());
            changes=true;
        }
        if(update.getDueDate()!=null&&!update.getDueDate().equals(taskScheduling.getDueDate())){
            taskScheduling.setDueDate(update.getDueDate());
            changes=true;
        }
        if(changes)
            taskSchedulingRepository.save(taskScheduling);
    }
    @Scheduled(cron = "*/60 * * * * *")
    public void taskStatusCheck(){
        List<TaskScheduling> schedules = taskSchedulingRepository.findAll();
        schedules.stream().
                filter(s -> s.getTask().getStatus().equals(Status.IN_PROGRESS)||s.getTask().getStatus().equals(Status.TO_DO)).
                forEach(ts -> {
                    if(isStale(ts)){
                        updateExtractedTask(ts,Status.STALLED,Priority.LOW);
                        mailService.sendEmail(new Mail(getEmail(ts),
                                "task has been staled",
                                "task [%s] has been in in progress but schedule due date for to long".
                                        formatted(ts.getTask().getTitle())));
                    }
                    else if(isArgent(ts)){
                        updateExtractedTask(ts,Status.NEED_INTENTION,Priority.HIGH);
                        mailService.sendEmail(new Mail(getEmail(ts),
                                "task  need intention",
                                "task [%s]  in in progress and schedule due date is close to set".
                                        formatted(ts.getTask().getTitle())));
                    }
                });
    }
    private void updateExtractedTask(TaskScheduling ts,Status status,Priority priority) {
        ts.getTask().setStatus(status);
        ts.getTask().setPriority(priority);
        taskSchedulingRepository.save(ts);
    }

    private String getEmail(TaskScheduling ts) {
        Integer userId = ts.getTask().getUser().getId();
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("user with id [%s] not found".formatted(userId))).
                getEmail();
    }

    public boolean isStale(TaskScheduling ts){
      Duration  duration = Duration.between(ts.getStartDate(), ts.getDueDate());
      return LocalDateTime.now(clock).isAfter(ts.getDueDate().plus(duration));
    }
    public boolean isArgent(TaskScheduling ts){
        Duration  duration = Duration.between(ts.getStartDate(), ts.getDueDate());
        Duration remaining=Duration.between(LocalDateTime.now(clock),ts.getDueDate());
        long percent = (remaining.toHours() * 100) / duration.toHours();
        return percent>=70&&percent<100;
    }
}
