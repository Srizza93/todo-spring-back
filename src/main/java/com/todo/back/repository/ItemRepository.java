package com.todo.back.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.todo.back.model.TodoItem;

public interface ItemRepository extends MongoRepository<TodoItem, String> {
	
	@Query("{name:'?0'}")
	TodoItem findItemByName(String name);
	
	@Query(value="{category:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
	List<TodoItem> findAll(String category);
	
	public long count();

}
