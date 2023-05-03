package com.todo.back.repository.todo;

public interface CustomItemRepository {
	
	void updateItemQuantity(String itemName, float newQuantity);

}
