package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;
import models.Enumerated.Category;
import models.Enumerated.TransportType;
import play.db.jpa.JPA;

@Entity
public class Donation {

	@GeneratedValue(strategy = GenerationType.TABLE) @Id
	private Integer id;
	
	@OneToOne(targetEntity=Category.class)
	private Category category;
	
	@OneToOne(targetEntity=TransportType.class)
	private TransportType transportType;
	
	@ManyToOne(targetEntity=Donor.class)
	private Donor donor;
	
	@OneToMany(targetEntity=File.class)
	private List<File> files = new ArrayList<File>();
	
	@ManyToMany(targetEntity=User.class)
	private List<User> users = new ArrayList<User>();
	
	@Required
	private String label;
	
	@Lob
	@Required
	private String description;
	
	@Required
	private Integer number;
	
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

	public int getId() {
		return id;
	}
	
	public Donor getDonor() {
		return donor;
	}

	public void setDonor(Donor donor) {
		this.donor = donor;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public TransportType getTransportType() {
		return transportType;
	}

	public void setTransportType(TransportType transportType) {
		this.transportType = transportType;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public String getDonorAddress() {
		if (donor != null 
				&& donor.getStreet() != null
				&& donor.getZip() != null
				&& donor.getCity() != null) {
			return donor.getStreet() + ", " + donor.getZip() + " " + donor.getCity();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return String.format("Donation#%d (%s)", id, label);
	}
	
    /**
     * Return a page of Donations
     * @param page the page to display
     * @param pageSize the number of entries per page
     * @param sortBy Donation property used for sorting
     * @param order sort order (either asc or desc)
     * @param filter filter applied on the name column
     */
    public static DonationPage page(int page, int pageSize, String filter, List<Category> categories) {
        if(page < 1) page = 1;
        
        List<Integer> ids = new ArrayList<>();
        if(categories.size() > 0) {
        	for(Category temp : categories) {
        		ids.add(new Integer(temp.getId()));
        	}
        }
        else {
        	ids.add(-1);
        }
        
        Long total = (Long)JPA.em().createQuery("SELECT count(d) FROM Donation d WHERE lower(d.label) LIKE :filter AND d.category.id IN (:ids) ORDER BY d.label ASC", Long.class)
	            .setParameter("filter", "%" + filter.toLowerCase() + "%").setParameter("ids", ids).getSingleResult();
        
        List<Donation> data = JPA.em().createQuery("FROM Donation d WHERE lower(d.label) LIKE :filter AND d.category.id IN (:ids) ORDER BY d.label ASC", Donation.class)
				.setParameter("filter", "%" + filter.toLowerCase() + "%").setParameter("ids", ids).setFirstResult((page - 1) * pageSize).setMaxResults(pageSize).getResultList();
        
        return new DonationPage(data, total, page, pageSize);
    }
}
