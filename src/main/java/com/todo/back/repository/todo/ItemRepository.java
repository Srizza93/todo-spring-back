package com.todo.back.repository.todo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.todo.back.model.TodoItem;

public interface ItemRepository extends MongoRepository<TodoItem, String> {

	List<TodoItem> findByUserIdAndDoneAndDueLessThanEqual(String userId, boolean isDone, LocalDateTime today);

	List<TodoItem> findByUserIdAndDone(String userId, boolean isDone);

	public long count();

}
