package uz.pdp.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.Position;
import uz.pdp.apphrmanagement.entity.Role;
import uz.pdp.apphrmanagement.entity.Task;
import uz.pdp.apphrmanagement.entity.User;
import uz.pdp.apphrmanagement.entity.enums.TaskStatus;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.TaskDto;
import uz.pdp.apphrmanagement.repository.TaskRepository;
import uz.pdp.apphrmanagement.repository.UserRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TaskService {

    final TaskRepository taskRepository;
    final UserRepository userRepository;
    @Autowired
    JavaMailSender javaMailSender;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public ApiResponse createTask(TaskDto taskDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        //userRepository.findById(principal.getId());
        UUID resId = taskDto.getResId();
        Optional<User> optionalUser = userRepository.findById(resId);
        if (!optionalUser.isPresent())
            return new ApiResponse("Such user not found", false);
        User user = optionalUser.get();
        Set<Position> resPos = user.getPositions();


        Set<Position> positions = principal.getPositions();
        for (Position position : positions) {
            for (Position resPo : resPos) {
                if (position.getPositionNumber() == 1 && (resPo.getPositionNumber() == 2 || resPo.getPositionNumber() == 3)) {
                    Task newTask = new Task();
                    newTask.setTaskName(taskDto.getTaskName());
                    newTask.setDescription(taskDto.getDescription());
                    newTask.setCreatedAt(taskDto.getCreatedAt());
                    newTask.setDeadline(taskDto.getDeadline());
                    newTask.setStatus(TaskStatus.NEW.name());
                    newTask.setCreatedBy(principal);
                    newTask.setResId(optionalUser.get());
                    taskRepository.save(newTask);
                    sendEmailToUser(user.getEmail(), newTask);
                    return new ApiResponse(resPo.getPositionNumber() == 2 ? "Task created by director to manager" : "Task created by director to worker", true);
                }
                if (position.getPositionNumber() == 2 && resPo.getPositionNumber() == 3) {
                    Task newTask = new Task();
                    newTask.setTaskName(taskDto.getTaskName());
                    newTask.setDescription(taskDto.getDescription());
                    newTask.setCreatedAt(taskDto.getCreatedAt());
                    newTask.setDeadline(taskDto.getDeadline());
                    newTask.setStatus(TaskStatus.NEW.name());
                    newTask.setCreatedBy(principal);
                    newTask.setResId(optionalUser.get());
                    taskRepository.save(newTask);
                    return new ApiResponse("Task created by manager to worker", true);
                }
                if (position.getPositionNumber() == 2 && resPo.getPositionNumber() == 2)
                    return new ApiResponse("Manager can not create task to managers", false);
                if (position.getPositionNumber() == 3 && (resPo.getPositionNumber() == 1 || resPo.getPositionNumber() == 2 || resPo.getPositionNumber() == 3))
                    return new ApiResponse("Worker can not created task to any users", false);

            }
        }
        return new ApiResponse("Error in server", false);


    }


    public ApiResponse editTask(TaskDto taskDto) {

        Optional<Task> optionalTask = taskRepository.findAllByTaskName(taskDto.getTaskName());
        if (!optionalTask.isPresent())
            return new ApiResponse("Such task not found", false);

        Task editingTask = optionalTask.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        UUID resId = taskDto.getResId();
        Optional<User> optionalUser = userRepository.findById(resId);
        if (!optionalUser.isPresent())
            return new ApiResponse("Such user not found", false);
        User user = optionalUser.get();
        Set<Position> resPos = user.getPositions();

        Set<Position> positions = principal.getPositions();
        for (Position position : positions) {
            for (Position resPo : resPos) {
                if (position.getPositionNumber() == 1 && (resPo.getPositionNumber() == 2 || resPo.getPositionNumber() == 3)) {

                    editingTask.setTaskName(taskDto.getTaskName());
                    editingTask.setDescription(taskDto.getDescription());
                    editingTask.setCreatedAt(taskDto.getCreatedAt());
                    editingTask.setDeadline(taskDto.getDeadline());
                    editingTask.setCreatedBy(principal);
                    editingTask.setResId(optionalUser.get());
                    taskRepository.save(editingTask);
                    sendEmailToUser(user.getEmail(), editingTask);
                    return new ApiResponse(resPo.getPositionNumber() == 2 ? "Task created by director to manager" : "Task created by director to worker", true);
                }
                if (position.getPositionNumber() == 2 && resPo.getPositionNumber() == 3) {
                    Task editingTask2 = new Task();
                    editingTask2.setTaskName(taskDto.getTaskName());
                    editingTask2.setDescription(taskDto.getDescription());
                    editingTask2.setCreatedAt(taskDto.getCreatedAt());
                    editingTask2.setDeadline(taskDto.getDeadline());
                    editingTask2.setCreatedBy(principal);
                    editingTask2.setResId(optionalUser.get());
                    sendEmailToUser(user.getEmail(), editingTask);
                    taskRepository.save(editingTask2);
                    sendEmailToUser(user.getEmail(), editingTask2);
                    return new ApiResponse("Task created by manager to worker", true);
                }
                if (position.getPositionNumber() == 2 && resPo.getPositionNumber() == 2)
                    return new ApiResponse("Manager can not create task to managers", false);
                if (position.getPositionNumber() == 3 && (resPo.getPositionNumber() == 1 || resPo.getPositionNumber() == 2 || resPo.getPositionNumber() == 3))
                    return new ApiResponse("Worker can not created task to any users", false);

            }

        }
        return new ApiResponse("Error in server", false);
    }

    public void sendEmailToUser(String sendingEmail, Task task) {
        String taskName = task.getTaskName();
        String description = task.getDescription();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("emailSender");
        mailMessage.setTo(sendingEmail);
        mailMessage.setSubject("Message !");

        mailMessage.setText("Task name  " + taskName + "  \n" + "Task description" + description + "\n " + "Created by " + task.getCreatedBy() + "\n" + "Task status " + task.getStatus());
        javaMailSender.send(mailMessage);

    }

    public Object get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Set<Role> roles = principal.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().equalsIgnoreCase("director")) {
                return taskRepository.findAll();
            }

        }
        return new ApiResponse("Only director can see all tasks", false);
    }

    public Object getById(UUID id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();

        Set<Role> roles = principal.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().equalsIgnoreCase("director")) {
                Optional<Task> optionalTask = taskRepository.findById(id);
                return optionalTask.orElse(null);
            }

        }
        return new ApiResponse("Only director can see all tasks by id", false);

    }

    public ApiResponse changeTaskStatus(String name, UUID id, Integer number) {

        Optional<Task> optionalTask = taskRepository.findAllByTaskNameAndResId(name, id);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();

            if (number == 2) {
                task.setStatus(TaskStatus.IN_PROGRESS.name());
                taskRepository.save(task);
                return new ApiResponse("Task is in progress", true);
            }
            if (number == 3) {
                task.setStatus(TaskStatus.DONE.name());
                Task savedTask = taskRepository.save(task);
                User createdByUser = task.getCreatedBy();
                String email = createdByUser.getEmail();
                sendEmailToUser(email, savedTask);
                return new ApiResponse("Task was done", true);
            }
        }

        return new ApiResponse("Such task not found", false);

    }

}

