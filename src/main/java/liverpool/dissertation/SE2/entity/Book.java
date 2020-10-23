package liverpool.dissertation.SE2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Book {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(length=300)
	private String title;
	
	@JsonIgnore
	private String encryptionIV;

	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getEncryptionIV() {
		return encryptionIV;
	}
	public void setEncryptionIV(String encryptionIV) {
		this.encryptionIV = encryptionIV;
	}

	@Override
	public String toString() {
		return "ID = \t" + id +  " & Title = \t" + title;
	}
}
