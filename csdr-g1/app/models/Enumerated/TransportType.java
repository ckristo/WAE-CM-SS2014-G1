package models.Enumerated;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Query;

import play.db.jpa.JPA;


@Entity
public class TransportType extends EnumType{
	
	public static Map<String, String> options() {
		LinkedHashMap<String, String> options = new LinkedHashMap<String, String>();
		
		Query query = JPA.em().createQuery("SELECT t FROM TransportType t ORDER BY t.name");
	    Collection<TransportType> transportTypes = query.getResultList();
	    
	    for(TransportType transportType : transportTypes) {
	    	options.put(transportType.getId().toString(), transportType.getName());
	    }
	    
	    return options;
	}
}
