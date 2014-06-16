package controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import models.Donation;
import models.Enumerated.Category;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import views.html.list.*;

public class DonationList extends Controller {
	
	private static List<Category> categories = new ArrayList<Category>();
	
	

    @Transactional(readOnly=true)
    public static Result list(int page, String filter) {
    	if(categories.size() == 0) {
    		categories.addAll(Category.findAll());
    	}
    	
        return ok(
            list.render(Donation.page(page, 4, filter, categories), filter, categories)
        );
    }
    
    @Transactional(readOnly=true)
    public static Result addCategory(int page, String filter) {
    	DynamicForm bindedForm = Form.form().bindFromRequest();
    	
    	int category_id = Integer.parseInt(bindedForm.get("categorySelect"));
    	Category category = Category.findById(category_id);
    	
    	boolean contains = false;
    
    	for(Category temp : categories) {
    		if(temp.getId().equals(category_id)) {
    			contains = true;
    			break;
    		}
    	}
    	if(!contains) {
    		categories.add(category);
    	}
    	
    	return ok(
                list.render(Donation.page(page, 4, filter, categories), filter, categories)
            );
    }
    
    @Transactional(readOnly=true)
    public static Result removeCategory(int page, String filter) {
    	DynamicForm bindedForm = Form.form().bindFromRequest();
    	
    	int category_id = Integer.parseInt(bindedForm.get("categorySelect"));
    	
    	Iterator<Category> iterator = categories.iterator();
    	while (iterator.hasNext()) {
			Category type = (Category) iterator.next();
			if(type.getId().equals(category_id)) {
				iterator.remove();
			}
		}
   	
    	return ok(
                list.render(Donation.page(page, 4, filter, categories), filter, categories)
            );
    }
    
}
