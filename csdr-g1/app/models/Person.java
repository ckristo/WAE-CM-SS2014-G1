package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import play.data.validation.Constraints.*;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Person {
	
	@GeneratedValue(strategy = GenerationType.TABLE) @Id
	private Integer id;
	
	@Required
	private String name;
	
	private String svnr;
	
	@Required
	@Email
	private String email;
	
	private String street;
	
	private String phone;
	
	private Integer zip;
	
	private String city;
	
	public Integer getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSvnr() {
		return svnr;
	}
	
	public void setSvnr(String svnr) {
		this.svnr = svnr;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public Integer getZip() {
		return zip;
	}
	
	public void setZip(Integer zip) {
		this.zip = zip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@Override
	public String toString() {
		return String.format("Person#%d (%s)", id, name);
	}
}
