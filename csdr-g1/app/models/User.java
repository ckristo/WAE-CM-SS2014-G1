package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import models.Enumerated.Role;


@Entity
public class User extends Person {

	@OneToOne(targetEntity=Role.class)
	private Role role;

	@ManyToMany(targetEntity=Donation.class)
	@JoinTable(	name = "Needs", 
	 			joinColumns = 			{ @JoinColumn(name = "user_id", nullable = false, updatable = false)}, 
				inverseJoinColumns = 	{ @JoinColumn(name = "donation_id", nullable = false, updatable = false)})
	private List<Donation> donations = new ArrayList<Donation>();
	
	private String username;
	private String password;
	private boolean active;
	
	
	public List<Donation> getDonations() {
		return donations;
	}
	public void setDonations(List<Donation> donations) {
		this.donations = donations;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}		
}
