package com.todo.back.services;

import com.todo.back.dto.TodoDto;
import com.todo.back.model.TodoItem;
import com.todo.back.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username="admin",roles={"USER","ADMIN"})
public class TodoServiceTest {

    @Autowired
    private MockMvc mockMvc;

    TodoItem todoItem1 = new TodoItem("1", "user1", "Todo 1",  null, null);

    TodoItem todoItem2 = new TodoItem("2", "user2", "Unlock your potential, embrace challenges, learn from failures, and grow into the best version of yourself!", null, null);

    List<TodoItem> todoItemList = Arrays.asList(todoItem1, todoItem2);

    private final ItemRepository todoItemRepository = mock(ItemRepository.class);

    @InjectMocks
    private TodoService todoService;

    private final TodoService todoServiceMock = mock(TodoService.class);

    private final String mockId = "123";

    TodoDto todoDto = new TodoDto();

    LocalDate localDate = LocalDate.now();

    LocalDateTime today = localDate.atTime(LocalTime.MAX);

    @Test
    public void shouldGetAllTodos() {
        when(todoItemRepository.findAll()).thenReturn(todoItemList);

        CollectionModel<EntityModel<TodoItem>> todos = todoService.todos();

        verify(todoItemRepository, times(1)).findAll();

        assertEquals(todoItemList.size(), todos.getContent().size());
    }

    @Test
    public void shouldntGetAllTodosUserServiceException() {
        doThrow(UserServiceException.class).when(todoItemRepository).findAll();

        assertThrows(UserServiceException.class, () -> todoService.todos());
    }

    @Test
    public void shouldGetAllTodayTodos() {
        when(todoItemRepository.findByUserIdAndDoneAndDueLessThanEqual(mockId, false, today)).thenReturn(todoItemList);

        CollectionModel<EntityModel<TodoItem>> todos = todoService.today(mockId);

        verify(todoItemRepository, times(1)).findByUserIdAndDoneAndDueLessThanEqual(mockId, false, today);

        assertEquals(todoItemList.size(), todos.getContent().size());
    }

    @Test
    public void shouldntGetAllTodayTodosUserServiceException() {
        doThrow(UserServiceException.class).when(todoItemRepository).findByUserIdAndDoneAndDueLessThanEqual(mockId, false, today);

        assertThrows(UserServiceException.class, () -> todoService.today(mockId));
    }

    @Test
    public void shouldGetAllInboxTodos() {
        when(todoItemRepository.findByUserIdAndDone(mockId, false)).thenReturn(todoItemList);

        CollectionModel<EntityModel<TodoItem>> todos = todoService.inbox(mockId);

        verify(todoItemRepository, times(1)).findByUserIdAndDone(mockId, false);

        assertEquals(todoItemList.size(), todos.getContent().size());
    }

    @Test
    public void shouldntGetAllInboxTodosUserServiceException() {
        doThrow(UserServiceException.class).when(todoItemRepository).findByUserIdAndDone(mockId, false);

        assertThrows(UserServiceException.class, () -> todoService.inbox(mockId));
    }

    @Test
    public void shouldGetAllDoneTodos() {
        when(todoItemRepository.findByUserIdAndDone(mockId, true)).thenReturn(todoItemList);

        CollectionModel<EntityModel<TodoItem>> todos = todoService.done(mockId);

        verify(todoItemRepository, times(1)).findByUserIdAndDone(mockId, true);

        assertEquals(todoItemList.size(), todos.getContent().size());
    }

    @Test
    public void shouldntGetAllDoneTodosUserServiceException() {
        doThrow(UserServiceException.class).when(todoItemRepository).findByUserIdAndDone(mockId, true);

        assertThrows(UserServiceException.class, () -> todoService.done(mockId));
    }

    @Test
    public void shouldAddATodo() {
        TodoItem todoItem = new TodoItem(null, null, todoItem1.getContent(), null, null);
        TodoDto todoDto = new TodoDto();
        todoDto.setContent(todoItem.getContent());

        ArgumentCaptor<TodoItem> todoItemCaptor = ArgumentCaptor.forClass(TodoItem.class);
        when(todoItemRepository.save(todoItemCaptor.capture())).thenReturn(todoItem);

        todoService.addTodo(todoDto);

        TodoItem capturedTodoItem = todoItemCaptor.getValue();
        assertEquals(todoItem.getContent(), capturedTodoItem.getContent());

        verify(todoItemRepository, times(1)).save(capturedTodoItem);
    }

    @Test
    public void shouldntAddATodoWithLongContent() {
        todoDto.setContent(todoItem2.getContent());

        assertThrows(IllegalArgumentException.class, () -> todoService.addTodo(todoDto));
    }

    @Test
    public void shouldntAddATodoUserServiceException() {
        doThrow(UserServiceException.class).when(todoItemRepository).save(todoItem1);

        assertThrows(UserServiceException.class, () -> todoService.addTodo(todoDto));
    }

    @Test
    public void shouldChangeATodo() {
        todoDto.setId(mockId);

        when(todoItemRepository.findById(mockId)).thenReturn(Optional.of(todoItem1));


        todoService.editTodoStatus(todoDto);


        verify(todoItemRepository, times(1)).findById(mockId);
    }


    @Test
    public void shouldntChangeATodoUserServiceException() {
        todoDto.setId(mockId);
        doThrow(UserServiceException.class).when(todoItemRepository).findById(mockId);

        assertThrows(UserServiceException.class, () -> todoService.editTodoStatus(todoDto));
    }

    @Test
    public void shouldDeleteATodo() throws Exception {
        doNothing().when(todoItemRepository).deleteById(mockId);

        todoService.deleteTodo(mockId);

        verify(todoItemRepository, times(1)).deleteById(mockId);
    }

    @Test
    public void shouldntDeleteATodoUserServiceException() {
        doThrow(UserServiceException.class).when(todoItemRepository).deleteById(mockId);

        assertThrows(UserServiceException.class, () -> todoService.deleteTodo(mockId));
    }
}
