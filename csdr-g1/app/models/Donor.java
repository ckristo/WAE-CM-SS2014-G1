package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;

import play.db.jpa.JPA;

@Entity
public class Donor extends Person {

	@OneToMany(targetEntity=Donation.class)
	private List<Donation> donations = new ArrayList<Donation>();

	public List<Donation> getDonations() {
		return donations;
	}

	public void setDonations(List<Donation> donations) {
		this.donations = donations;
	}

    /**
	 * Returns a list of all donors saved in the database.
	 * @return a list of all donors saved in the database.
	 */
	public static List<Donor> findAll() {
		TypedQuery<Donor> query = JPA.em().createQuery("SELECT d FROM Donor d", Donor.class);
	    return query.getResultList();
	}
}
