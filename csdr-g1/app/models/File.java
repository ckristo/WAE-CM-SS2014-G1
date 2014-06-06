package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class File {
	
	@GeneratedValue @Id
	private Integer id;
	
	@ManyToOne(targetEntity=Donation.class)
	private Donation donation;
	
	private String filepath;
	private String mimetype;
	private String thumbnailpath;
	private boolean is_first;
	
	
	public Donation getDonation() {
		return donation;
	}
	public void setDonation(Donation donation) {
		this.donation = donation;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getMimetype() {
		return mimetype;
	}
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}
	public String getThumbnailpath() {
		return thumbnailpath;
	}
	public void setThumbnailpath(String thumbnailpath) {
		this.thumbnailpath = thumbnailpath;
	}
	public boolean isIs_first() {
		return is_first;
	}
	public void setIs_first(boolean is_first) {
		this.is_first = is_first;
	}
	
	
}
