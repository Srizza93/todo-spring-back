package com.todo.back;

import com.todo.back.model.TodoItem;
import com.todo.back.repository.CustomItemRepository;
import com.todo.back.repository.ItemRepository;
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
	CustomItemRepository customRepo;

	List<TodoItem> itemList = new ArrayList<TodoItem>();

	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}

	public void run(String... args) {

		// Clean up any previous data
		todoItemRepo.deleteAll(); // Doesn't delete the collection

		System.out.println("-------------CREATE TODO ITEMS-------------------------------\n");

		createTodoItems();

		System.out.println("\n----------------SHOW ALL TODO ITEMS---------------------------\n");

		showAllTodoItems();

		System.out.println("\n--------------GET ITEM BY NAME-----------------------------------\n");

		getTodoItemByName("Whole Wheat Biscuit");

		System.out.println("\n-----------GET ITEMS BY CATEGORY---------------------------------\n");

		getItemsByCategory("millets");

		System.out.println("\n-----------UPDATE CATEGORY NAME OF ALL TODO ITEMS----------------\n");

		updateCategoryName("snacks");

		System.out.println("\n-----------UPDATE QUANTITY OF A TODO ITEM------------------------\n");

		updateItemQuantity("Bonny Cheese Crackers Plain", 10);

		System.out.println("\n----------DELETE A TODO ITEM----------------------------------\n");

		deleteTodoItem("Kodo Millet");

		System.out.println("\n------------FINAL COUNT OF TODO ITEMS-------------------------\n");

		findCountOfTodoItems();

		System.out.println("\n-------------------THANK YOU---------------------------");

	}

	// CRUD operations

	//CREATE
	void createTodoItems() {
		System.out.println("Data creation started...");

		todoItemRepo.save(new TodoItem("Whole Wheat Biscuit", "Whole Wheat Biscuit", 5, "snacks"));
		todoItemRepo.save(new TodoItem("Kodo Millet", "XYZ Kodo Millet healthy", 2, "millets"));
		todoItemRepo.save(new TodoItem("Dried Red Chilli", "Dried Whole Red Chilli", 2, "spices"));
		todoItemRepo.save(new TodoItem("Pearl Millet", "Healthy Pearl Millet", 1, "millets"));
		todoItemRepo.save(new TodoItem("Cheese Crackers", "Bonny Cheese Crackers Plain", 6, "snacks"));

		System.out.println("Data creation complete...");
	}

	// READ
	// 1. Show all the data
	public void showAllTodoItems() {

		itemList = todoItemRepo.findAll();

		itemList.forEach(item -> System.out.println(getItemDetails(item)));
	}

	// 2. Get item by name
	public void getTodoItemByName(String name) {
		System.out.println("Getting item by name: " + name);
		TodoItem item = todoItemRepo.findItemByName(name);
		System.out.println(getItemDetails(item));
	}

	// 3. Get name and items of a all items of a particular category
	public void getItemsByCategory(String category) {
		System.out.println("Getting items for the category " + category);
		List<TodoItem> list = todoItemRepo.findAll(category);

		list.forEach(item -> System.out.println("Name: " + item.getName() + ", Quantity: " + item.getItemQuantity()));
	}

	// 4. Get count of documents in the collection
	public void findCountOfTodoItems() {
		long count = todoItemRepo.count();
		System.out.println("Number of documents in the collection = " + count);
	}

	// UPDATE APPROACH 1: Using MongoRepository
	public void updateCategoryName(String category) {

		// Change to this new value
		String newCategory = "munchies";

		// Find all the items with the category
		List<TodoItem> list = todoItemRepo.findAll(category);

		list.forEach(item -> {
			// Update the category in each document
			item.setCategory(newCategory);
		});

		// Save all the items in database
		List<TodoItem> itemsUpdated = todoItemRepo.saveAll(list);

		if(itemsUpdated != null)
			System.out.println("Successfully updated " + itemsUpdated.size() + " items.");
	}


	// UPDATE APPROACH 2: Using MongoTemplate
	public void updateItemQuantity(String name, float newQuantity) {
		System.out.println("Updating quantity for " + name);
		customRepo.updateItemQuantity(name, newQuantity);
	}

	// DELETE
	public void deleteTodoItem(String id) {
		todoItemRepo.deleteById(id);
		System.out.println("Item with id " + id + " deleted...");
	}
	// Print details in readable form

	public String getItemDetails(TodoItem item) {

		System.out.println(
				"Item Name: " + item.getName() +
						", \nItem Quantity: " + item.getItemQuantity() +
						", \nItem Category: " + item.getCategory()
		);

		return "";
	}
}
