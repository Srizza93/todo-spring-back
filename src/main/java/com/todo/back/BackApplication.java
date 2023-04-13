package com.todo.back;

import com.todo.back.model.TodoItem;
import com.todo.back.model.UserProfile;
import com.todo.back.repository.todo.CustomItemRepository;
import com.todo.back.repository.todo.ItemRepository;
import com.todo.back.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableMongoRepositories
public class BackApplication implements CommandLineRunner {

	@Autowired
	ItemRepository todoItemRepo;
	@Autowired
	UserRepository userProfileRepo;

	@Autowired
	CustomItemRepository customRepo;

	List<TodoItem> itemList = new ArrayList<TodoItem>();
	List<UserProfile> user = new ArrayList<UserProfile>();

	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}

	public void run(String... args) {
		System.out.println(user);
		System.out.println("----------------");
		System.out.println(itemList);
	}


}
