package com.todo.back.repository;

import com.todo.back.model.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ItemRepository extends JpaRepository<TodoItem, Long> {

	List<TodoItem> findByUserIdAndDoneAndDueLessThanEqual(Long userId, boolean isDone, Date today);

	List<TodoItem> findByUserIdAndDone(Long userId, boolean isDone);

	public long count();

}
