package models;

import controllers.routes;

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
	
	private String filename;
	private String tmpFilename;
	private String mimetype;
	private String thumbnail;
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
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getTmpFilename() {
		return tmpFilename;
	}
	public void setTmpFilename(String tmpFilename) {
		this.tmpFilename = tmpFilename;
	}
	public String getMimetype() {
		return mimetype;
	}
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public boolean isIs_first() {
		return is_first;
	}
	public void setIs_first(boolean is_first) {
		this.is_first = is_first;
	}
	
	/**
	 * Returns the path to the image file.
	 * @return the path to the image.
	 */
	public String getPath() {
		return routes.Assets.at("files/"+donation.getId()+"/"+tmpFilename).url();
	}
	
	/**
	 * Returns the path to the thumbnail image file.
	 * @return the path to the thumbnail image.
	 */
	public String getThumbnailPath() {
		//TODO: use generated thumbnail path -- currently points to full image
		return routes.Assets.at("files/"+donation.getId()+"/"+tmpFilename).url();
	}
}
