package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Donator extends Person {

	@OneToMany(targetEntity=Donation.class)
	private List<Donation> donations = new ArrayList<Donation>();

	public List<Donation> getDonations() {
		return donations;
	}

	public void setDonations(List<Donation> donations) {
		this.donations = donations;
	}
	
}
