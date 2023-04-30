package com.todo.back.repository.todo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.todo.back.model.TodoItem;

public interface ItemRepository extends MongoRepository<TodoItem, String> {

	String today = LocalDate.now().parse("2023-04-30").toString();

	@Query(value="{userId:'?0', done: false, due: ?1}")
	List<TodoItem> findByUserToday(String userId, String today);

	@Query(value="{userId:'?0', done: false}")
	List<TodoItem> findByUserInbox(String userId);

	@Query(value="{userId:'?0', done: true}")
	List<TodoItem> findByUserDone(String userId);
	
	public long count();

}
