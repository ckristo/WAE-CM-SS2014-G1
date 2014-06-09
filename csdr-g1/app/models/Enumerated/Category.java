package models.Enumerated;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Query;

import play.db.jpa.JPA;

@Entity
public class Category extends EnumType {
	
	public static Map<String, String> options() {
		LinkedHashMap<String, String> options = new LinkedHashMap<String, String>();
		
		Query query = JPA.em().createQuery("SELECT c FROM Category c ORDER BY c.name");
	    Collection<Category> categories = query.getResultList();
	    
	    for(Category category : categories) {
	    	options.put(category.getId().toString(), category.getName());
	    }
        
        return options;
	}
	
}
