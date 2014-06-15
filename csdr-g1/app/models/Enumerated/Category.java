package models.Enumerated;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.TypedQuery;

import play.db.jpa.JPA;

@Entity
public class Category extends EnumType {
	
	public Category() {}
	
	public Category(String name) {
		super(name);
	}
	
	/**
	 * Looks-up a category by id.
	 * @param id the id of the category to lookup
	 * @return the category or null
	 */
	public static Category findById(int id) {
		return JPA.em().find(Category.class, id);
	}
	
	/**
	 * Returns a list of all categories saved in the database.
	 * @return a list of all categories saved in the database.
	 */
	public static List<Category> findAll() {
		TypedQuery<Category> query = JPA.em().createQuery("SELECT c FROM Category c", Category.class);
	    return query.getResultList();
	}
	
	/**
	 * Generates category options for a form drop-down element.
	 * @return a sorted map (key: option value, value: option name) containing all category options.
	 */
	public static Map<String, String> options() {
		// get all categories from db
		List<Category> categories = Category.findAll();
		
		// sort alphabetically
		Collections.sort(categories, new Comparator<Category>() {
			@Override
			public int compare(Category o1, Category o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	    
		// generate hash map including form options
	    LinkedHashMap<String, String> options = new LinkedHashMap<String, String>();
	    options.put("", "");
	    for(Category category : categories) {
	    	options.put(category.getId().toString(), category.getName());
	    }
	    
        return options;
	}
	
}
