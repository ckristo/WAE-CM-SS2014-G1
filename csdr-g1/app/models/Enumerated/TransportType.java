package models.Enumerated;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.TypedQuery;

import play.db.jpa.JPA;

@Entity
public class TransportType extends EnumType {
		
	public TransportType() {}
	
	public TransportType(String name) {
		super(name);
	}
	
	/**
	 * Looks-up a transport type by id.
	 * @param id the id of the transport type to lookup
	 * @return the transport type or null
	 */
	public static TransportType findById(int id) {
		return JPA.em().find(TransportType.class, id);
	}
	
	/**
	 * Returns a list of all transport types saved in the database.
	 * @return a list of all transport types saved in the database.
	 */
	public static List<TransportType> findAll() {
		TypedQuery<TransportType> query = JPA.em().createQuery("SELECT t FROM TransportType t", TransportType.class);
	    return query.getResultList();
	}
	
	/**
	 * Generates transport type options for a form drop-down element.
	 * @return a map (key: option value, value: option name) containing all transport type options.
	 */
	public static Map<String, String> options() {
		// get all categories from db
		List<TransportType> transportTypes = TransportType.findAll();
	    
		// generate hash map including form options
	    LinkedHashMap<String, String> options = new LinkedHashMap<String, String>();
	    options.put("", "");
	    for(TransportType transportType : transportTypes) {
	    	options.put(transportType.getId().toString(), transportType.getName());
	    }
	    
        return options;
	}
}
