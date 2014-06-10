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
	
	@ManyToOne(targetEntity=Donator.class)
	private Donator donator;
	
	@OneToMany(targetEntity=File.class)
	private List<File> files = new ArrayList<File>();
	
	@ManyToMany(targetEntity=User.class)
	private List<User> users = new ArrayList<User>();
	
	private String label;
	
	private String description;
	
	private int number;
	
	private Date pickUpDate;
	
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
	
	public Donator getDonator() {
		return donator;
	}

	public void setDonator(Donator donator) {
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

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public Date getPickUpDate() {
		return pickUpDate;
	}

	public void setPickUpDate(Date pickUpDate) {
		this.pickUpDate = pickUpDate;
	}
	
	@Override
	public String toString() {
		return String.format("Donation#%d (%s)", id, label);
	}
}
