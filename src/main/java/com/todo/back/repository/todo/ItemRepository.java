package com.todo.back.repository.todo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.todo.back.model.TodoItem;

public interface ItemRepository extends MongoRepository<TodoItem, String> {

	@Query("{userId:'?0', due : {$lte : ?1}}")
	List<TodoItem> findByUserToday(String userId, LocalDateTime today);

	@Query(value="{userId:'?0', done: false}")
	List<TodoItem> findByUserInbox(String userId);

	@Query(value="{userId:'?0', done: true}")
	List<TodoItem> findByUserDone(String userId);
	
	public long count();

}
