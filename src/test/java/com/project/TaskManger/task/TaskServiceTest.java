package com.project.TaskManger.task;

import com.github.javafaker.Faker;
import com.project.TaskManger.category.Category;
import com.project.TaskManger.category.CategoryRepository;
import com.project.TaskManger.notification.MailService;
import com.project.TaskManger.security.config.JwtService;
import com.project.TaskManger.security.user.Role;
import com.project.TaskManger.security.user.User;
import com.project.TaskManger.security.user.UserRepository;
import com.project.TaskManger.taskScheduling.TaskScheduling;
import com.project.TaskManger.taskScheduling.TaskSchedulingRepository;
import com.project.TaskManger.taskScheduling.TaskSchedulingService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    private TaskService underTest;
    @Mock
    private TaskRepository taskRepository;

    private  JwtService jwtService=new JwtService();
    @Mock
    private  UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private TaskSchedulingService schedulingService;
    @Mock
    private TaskSchedulingRepository schedulingRepository;
    @Mock
    private Clock clock;
    @Mock
    private MailService mailService;

    protected static final Faker FAKER=new Faker();

    @BeforeEach
    void setUp() {
        schedulingService=new TaskSchedulingService(schedulingRepository,taskRepository,userRepository,clock,mailService);
        underTest=new TaskService(taskRepository,jwtService,userRepository,categoryRepository,schedulingService);
    }

    @Test
    void addTask() {
        // Given
        User user1=new User(1,"lm","df","ema@gmail.com","01234",Role.USER);
        String token = getToken(user1);
        String title=FAKER.name().title();
        String description=FAKER.leagueOfLegends().champion();
        TaskDto task=new TaskDto(title,description,Priority.HIGH,Status.DONE,title);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user1));
        when(categoryRepository.findCategoryByTitle(title)).thenReturn(Optional.of(new Category(title,description)));
        //When
        underTest.addTask(task,token);
        //Then
        ArgumentCaptor<Task> argumentCaptor=ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getTitle()).isEqualTo(title);
        assertThat(argumentCaptor.getValue().getDescription()).isEqualTo(description);
        assertThat(argumentCaptor.getValue().getPriority()).isEqualTo(task.getPriority());
        assertThat(argumentCaptor.getValue().getStatus()).isEqualTo(task.getStatus());
        assertThat(argumentCaptor.getValue().getCategory().getTitle()).isEqualTo(task.getCategoryTitle());
    }
    @Test
    void addTaskWithSchedule() {
        // Given
        User user1=new User(1,"lm","df","ema@gmail.com","01234",Role.USER);
        String token = getToken(user1);
        String title=FAKER.name().title();
        String description=FAKER.leagueOfLegends().champion();
        String categoryTitle=FAKER.leagueOfLegends().champion();
        LocalDateTime startDate=LocalDateTime.now();
        LocalDateTime dateTime=LocalDateTime.now().plusHours(1);
        TaskWithScheduleDto taskWithSchedule=new TaskWithScheduleDto(title,description,Priority.MEDIUM,Status.TO_DO,categoryTitle,startDate,dateTime);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user1));
        when(categoryRepository.findCategoryByTitle(categoryTitle)).thenReturn(Optional.of(new Category(categoryTitle,description)));
        Task task = new Task(title, description, Priority.MEDIUM, Status.TO_DO);
        task.setId(10);
        when(taskRepository.findTaskByUserIdAndTitle(user1.getId(),title)).thenReturn(Optional.of(task));
        when(taskRepository.existsTasksById(10)).thenReturn(true);
        //When
        underTest.addTaskWithSchedule(taskWithSchedule,token);

        //Then
        ArgumentCaptor<Task> argumentCaptor=ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getTitle()).isEqualTo(taskWithSchedule.getTitle());
        assertThat(argumentCaptor.getValue().getDescription()).isEqualTo(taskWithSchedule.getDescription());
        assertThat(argumentCaptor.getValue().getStatus()).isEqualTo(taskWithSchedule.getStatus());
        assertThat(argumentCaptor.getValue().getPriority()).isEqualTo(taskWithSchedule.getPriority());
        assertThat(argumentCaptor.getValue().getCategory().getTitle()).isEqualTo(taskWithSchedule.getCategoryTitle());
        ArgumentCaptor<TaskScheduling> schedulingArgumentCaptor=ArgumentCaptor.forClass(TaskScheduling.class);
        verify(schedulingRepository).save(schedulingArgumentCaptor.capture());
        assertThat(schedulingArgumentCaptor.getValue().getStartDate()).isEqualTo(taskWithSchedule.getStartDate());
        assertThat(schedulingArgumentCaptor.getValue().getDueDate()).isEqualTo(taskWithSchedule.getDueDate());
    }



    @Test
    void getTasks() {
        // Given
        User user1=new User(1,"lm","df","ema@gmail.com","01234",Role.USER);
        String token = getToken(user1);

        //When
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user1));
        underTest.getTasks(token);
        //Then
        verify(taskRepository).findAllByUserId(user1.getId());

    }

    @Test
    void getTaskByTitleStartWith() {
        User user1=new User(1,"lm","df","ema@gmail.com","01234",Role.USER);
        String token = getToken(user1);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user1));

        //When

        underTest.getTaskByTitleStartWith("tt",token);

        //Then
        verify(taskRepository).findAllByUserIdAndTitleStartsWith(user1.getId(),"tt");

    }

    @Test
    void removeTask() {
        // Given
        int id=1;
        User user1=new User(1,"lm","df","ema@gmail.com","01234",Role.USER);
        String token = getToken(user1);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user1));
        when(taskRepository.existsTasksByIdAndUserId(1,user1.getId())).thenReturn(true);
        //When
        underTest.removeTask(id,token);
        //Then
        verify(taskRepository).deleteTaskByIdAndUserId(id,user1.getId());
    }

    @Test
    void updateTask() {
        // Given
        int id=1;
        User user1=new User(1,"lm","df","ema@gmail.com","01234",Role.USER);
        String token = getToken(user1);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user1));
        String title=FAKER.name().title();
        String description=FAKER.leagueOfLegends().champion();
        Category category = new Category(title, description);
        Task task=new Task(FAKER.name().title(),FAKER.gameOfThrones().quote(),null,null,category);
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        TaskDto update=new TaskDto(title,description,Priority.HIGH,Status.DONE,title);
        //When
        underTest.updateTask(id,update,token);
        //Then
        ArgumentCaptor<Task> argumentCaptor=ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getTitle()).isEqualTo(update.getTitle());
        assertThat(argumentCaptor.getValue().getDescription()).isEqualTo(update.getDescription());
        assertThat(argumentCaptor.getValue().getPriority()).isEqualTo(update.getPriority());
        assertThat(argumentCaptor.getValue().getStatus()).isEqualTo(update.getStatus());
        assertThat(argumentCaptor.getValue().getCategory().getTitle()).isEqualTo(update.getCategoryTitle());
    }

    @NotNull
    private String getToken(User user1) {
        var user = User.builder()
                .firstname(user1.getFirstname())
                .lastname(user1.getLastname())
                .email(user1.getEmail())
                .password(user1.getPassword())
                .role(user1.getRole())
                .build();
        userRepository.save(user);
        var jwtToken = "Bearer " + jwtService.generateToken(user);
        return jwtToken;
    }


}