package models.Enumerated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class EnumType {
	
	@GeneratedValue(strategy = GenerationType.TABLE) @Id
	private Integer id;
	
	private String name;
	
	public EnumType() {}
	
	public EnumType(String name) {
		this.name = name;
	}
	
	public EnumType(EnumType et) {
		this.name = et.getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return String.format("EnumType#%d (%s)", id, name);
	}
}
