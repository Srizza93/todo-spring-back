package com.todo.back.services;

import com.todo.back.controller.TodoItemController;
import com.todo.back.dto.TodoDto;
import com.todo.back.model.TodoItem;
import com.todo.back.repository.ItemRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class TodoService {

    private final ItemRepository todoItemRepository;

    public TodoService(ItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }

    public CollectionModel<EntityModel<TodoItem>> todos() throws UserServiceException {

        try {
            List<EntityModel<TodoItem>> todos = todoItemRepository.findAll().stream()
                    .map(todo -> EntityModel.of(todo,
                            linkTo(methodOn(TodoItemController.class).getAllTodos()).withRel("todo_items")))
                    .collect(Collectors.toList());

            return CollectionModel.of(todos, linkTo(methodOn(TodoItemController.class).getAllTodos()).withSelfRel());
        } catch (Exception e) {
            throw new UserServiceException("Failed to fetch todos " + e.getMessage());
        }
    }

    public CollectionModel<EntityModel<TodoItem>> today(Long userId) throws UserServiceException {
        try {
            LocalDate localDate = LocalDate.now();
            LocalDateTime today = localDate.atTime(LocalTime.MAX);
            Date todayDate = Date.from(today.atZone(ZoneId.systemDefault()).toInstant());

            List<EntityModel<TodoItem>> todos = todoItemRepository.findByUserIdAndDoneAndDueLessThanEqual(userId, false, todayDate).stream()
                    .map(todo -> EntityModel.of(todo,
                            linkTo(methodOn(TodoItemController.class).getAllTodos()).withRel("todo_items")))
                    .collect(Collectors.toList());

            return CollectionModel.of(todos, linkTo(methodOn(TodoItemController.class).getAllTodos()).withSelfRel());
        } catch (Exception e) {
            throw new UserServiceException("Failed to fetch today's todos " + e.getMessage(), e);
        }
    }

    public CollectionModel<EntityModel<TodoItem>> inbox(Long userId) throws UserServiceException {

        try {
            List<EntityModel<TodoItem>> todos = todoItemRepository.findByUserIdAndDone(userId, false).stream()
                    .map(todo -> EntityModel.of(todo,
                            linkTo(methodOn(TodoItemController.class).getAllTodos()).withRel("todo_items")))
                    .collect(Collectors.toList());

            return CollectionModel.of(todos, linkTo(methodOn(TodoItemController.class).getAllTodos()).withSelfRel());
        } catch (Exception e) {
            throw new UserServiceException("Failed to fetch inbox's todos " + e.getMessage(), e);
        }
    }

    public CollectionModel<EntityModel<TodoItem>> done(Long userId) throws UserServiceException {

        try {
            List<EntityModel<TodoItem>> todos = todoItemRepository.findByUserIdAndDone(userId, true).stream()
                    .map(todo -> EntityModel.of(todo,
                            linkTo(methodOn(TodoItemController.class).getAllTodos()).withRel("todo_items")))
                    .collect(Collectors.toList());

            return CollectionModel.of(todos, linkTo(methodOn(TodoItemController.class).getAllTodos()).withSelfRel());
        } catch (Exception e) {
            throw new UserServiceException("Failed to fetch done todos " + e.getMessage(), e);
        }
    }

    public TodoItem addTodo(TodoDto todoDto) throws UserServiceException {
        try {
            TodoItem todo = getTodoFromDto(todoDto);

            if (todo.getContent().length() > 100) {
                throw new IllegalArgumentException("The content is too long");
            }

            todo.setDone(false);

            return todoItemRepository.save(todo);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new UserServiceException("Failed to add a new todo: " + e.getMessage());
        }
    }

    public TodoItem editTodoStatus(TodoDto todoDto) throws UserServiceException {

        try {
            return todoItemRepository.findById(todoDto.getId()) //
                    .map(todo -> {
                        todo.setDone(todoDto.getDone());
                        return todoItemRepository.save(todo);
                    }) //
                    .orElseGet(() -> {
                        todoDto.setDone(todoDto.getDone());

                        TodoItem todo = getTodoFromDto(todoDto);

                        return todoItemRepository.save(todo);
                    });
        } catch (Exception e) {
            throw new UserServiceException("Failed to edit the todo status: " + e.getMessage());
        }
    }

    public ResponseEntity<?> deleteTodo(Long id) throws UserServiceException {

        try {
            todoItemRepository.deleteById(id);

            return ResponseEntity.ok("Successfully deleted the todo");
        } catch (Exception e) {
            throw new UserServiceException("Failed to delete the todo " + e.getMessage(), e);
        }
    }

    public TodoItem getTodoFromDto(TodoDto todoDto) {
        Long userId = todoDto.getUserId();
        String content = todoDto.getContent();
        Date due = todoDto.getDue();
        Date created = todoDto.getCreated();

        return new TodoItem(null, userId, content, due, created);
    }

}
