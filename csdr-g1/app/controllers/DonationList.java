package controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import models.Donation;
import models.Enumerated.Category;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import views.html.list.*;

public class DonationList extends Controller {

	private static Map<String, String> categoryMap = new LinkedHashMap<>();
	private static String filter = "";

	private static final int PAGE_SIZE = 4;

	@Security.Authenticated(Secured.class)
	@Transactional(readOnly = true)
	public static Result list(int page) {
		if (categoryMap.size() == 0) {
			categoryMap = Category.options();
		}

		return ok(list.render(
				Donation.page(page, PAGE_SIZE, filter, categoryMap), filter,
				categoryMap));
	}

	@Security.Authenticated(Secured.class)
	@Transactional(readOnly = true)
	public static Result filter() {
		DynamicForm bindedForm = Form.form().bindFromRequest();

		filter = bindedForm.get("filter");

		return ok(list.render(Donation.page(0, PAGE_SIZE, filter, categoryMap),
				filter, categoryMap));
	}

	@Security.Authenticated(Secured.class)
	@Transactional(readOnly = true)
	public static Result addCategory() {
		// get category id
		DynamicForm bindedForm = Form.form().bindFromRequest();
		int category_id;
		try {
			category_id = Integer.parseInt(bindedForm.get("category"));
		}
		catch (NumberFormatException e) {
			return badRequest(list.render(
					Donation.page(0, PAGE_SIZE, filter, categoryMap), filter,
					categoryMap));
		}
		
		// load category
		Category category = Category.findById(category_id);
		if (category == null) {
			return badRequest(list.render(
					Donation.page(0, PAGE_SIZE, filter, categoryMap), filter,
					categoryMap));
		}
		
		// add to selected categories
		categoryMap.put(category.getId().toString(), category.getName());
		categoryMap = sortByValues(categoryMap);
		
		return ok(list.render(Donation.page(0, PAGE_SIZE, filter, categoryMap),
				filter, categoryMap));
	}

	@Security.Authenticated(Secured.class)
	@Transactional(readOnly = true)
	public static Result removeCategory(String id) {
		if (categoryMap.size() > 1) {
			categoryMap.remove(id);
			categoryMap = sortByValues(categoryMap);
		}
		else {
			flash("warning", "warning-1");
		}
		return ok(list.render(Donation.page(0, PAGE_SIZE, filter, categoryMap),
				filter, categoryMap));
	}
	
	@Security.Authenticated(Secured.class)
	@Transactional(readOnly = true)
	public static Result resetFilters() {
	
		categoryMap = Category.options();
		filter = "";
		
		return ok(list.render(
				Donation.page(0, PAGE_SIZE, filter, categoryMap), filter,
				categoryMap));
	}

	@Security.Authenticated(Secured.class)
	@Transactional(readOnly = true)
	public static Result needDonation(Integer id) {
		flash("success", "success");
		return ok(list.render(Donation.page(0, PAGE_SIZE, filter, categoryMap), filter, categoryMap));
	}
	
	private static LinkedHashMap<String, String> sortByValues(Map<String, String> map) {
		// convert map to linked list
		List<Entry<String, String>> list = new LinkedList<>(map.entrySet());
		
		// sort linked list
		Collections.sort(list, new Comparator<Entry<String, String>>() {
			public int compare(Entry<String, String> o1, Entry<String, String> o2) {
				Map.Entry<String, String> entry1 = (Map.Entry<String, String>) o1;
				Map.Entry<String, String> entry2 = (Map.Entry<String, String>) o2;
				return entry1.getValue().compareTo(entry2.getValue());
			}
		});

		// convert list back to map
		LinkedHashMap<String, String> sortedHashMap = new LinkedHashMap<>();
		for (Iterator<Entry<String, String>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		}
		
		return sortedHashMap;
	}

}
