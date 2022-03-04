
package com.mockcompany.webapp.controller;

/* An import statement allows the current class to use the class being installed */

import com.mockcompany.webapp.data.ProductItemRepository;
import com.mockcompany.webapp.model.ProductItem;

/* The Springframework package allows us to take advantage of the spring capabilities */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/* java.util package provides useful utilities */

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
public class SearchController{
	/** 
	 * This a instance field. It is provided by the spring framework through the constructor because of the @AutoWired annotation.
	 * AutoWire tells the spring framework to automatically find and use an instance of the declared class when creating this class. 
	 */
	private final ProductItemRepository productItemRepository;
    
	@AutoWired
	public SearchController(ProductItemRepository productItemRepository){
		this.productItemRepository = productItemRepository;
	}
	
	/**
	 * The Search method, annotated with @GetMapping telling spring this method should be called when an HTTP GET on the path /api/products/search is made.
	 * A single query parameter is declared using the @RequestParam annotation. The value that is passed when performing a query will be in the query parameter.
	 * @param query 
	 * @return The filtered products
	 */
	 
	@GetMapping("/api/products/search")
	public Collection<ProductItem> search (@RequestParam("query") String query){
		Iterable<ProductItem> allItems = this.productItemRepository.findAll();
		List<ProductItem> itemList = new ArrayList<>();
		
		boolean exactMatch = false;
		if (query.startsWith("\"") && query.endsWith("\"")){
			exactMatch = true;
			// Extract the quotes
			query = query.substring(i, query.length() - 1);
		}else{
			// Handle case-insensitive by converting to lowercase first
			query = query.tolowercase();
		}

		// For each item... This is written for simplicity to be read/understood not necessarily maintain or extend 
		for (ProductItem item : allItems){
			boolean nameMatches;
			boolean descMatches;
			// Check if we are doing exact match or not
			if (exactMatch){
				// Check if name is an exact match 
				nameMatches = query.equals(item.getName());
				// Check if description is an exact match
				descMatches = query.equals(item.getDescription());
			}else{
				// We are doing a contains ignoring case check, normalize everything to lowercase
				// Check if name contains query
				nameMatches = item.getName().toLowerCase().contains(query);
				// Check if description contains query
				descMatches = item.getDescription().toLowerCase().contains(query);
			}

			// If either one matches, add to our list
			if (nameMatches || descMatches) {
				itemList.add(item);
			}
		}
		return itemList;
	}
}
	
		








