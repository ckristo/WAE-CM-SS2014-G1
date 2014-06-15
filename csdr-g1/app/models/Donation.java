package models;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Required;
import play.data.format.*;
import models.Enumerated.Category;
import models.Enumerated.TransportType;

@Entity
public class Donation {

	@GeneratedValue(strategy = GenerationType.TABLE) @Id
	private Integer id;
	
	@OneToOne(targetEntity=Category.class)
	private Category category;
	
	@OneToOne(targetEntity=TransportType.class)
	private TransportType transportType;
	
	@ManyToOne(targetEntity=Donor.class)
	private Donor donator;
	
	@OneToMany(targetEntity=File.class)
	private List<File> files = new ArrayList<File>();
	
	@ManyToMany(targetEntity=User.class)
	private List<User> users = new ArrayList<User>();
	
	@Required
	private String label;
	
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
	
	public Donor getDonator() {
		return donator;
	}

	public void setDonator(Donor donator) {
		this.donator = donator;
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

	
	@Override
	public String toString() {
		return String.format("Donation#%d (%s)", id, label);
	}
}
