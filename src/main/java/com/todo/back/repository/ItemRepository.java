package com.todo.back.repository;

import com.todo.back.model.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<TodoItem, String> {

	List<TodoItem> findByUserIdAndDoneAndDueLessThanEqual(String userId, boolean isDone, LocalDateTime today);

	List<TodoItem> findByUserIdAndDone(String userId, boolean isDone);

	public long count();

}
