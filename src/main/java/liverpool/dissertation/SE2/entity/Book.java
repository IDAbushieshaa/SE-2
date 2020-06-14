package liverpool.dissertation.SE2.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Book {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String title;
	private String author;
	
	private String encryptionKey;
	private String encryptionSalt;

	
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
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}

	public String getEncryptionKey() {
		return encryptionKey;
	}
	public void setEncryptionKey(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}
	
	public String getEncryptionSalt() {
		return encryptionSalt;
	}
	public void setEncryptionSalt(String encryptionSalt) {
		this.encryptionSalt = encryptionSalt;
	}
	
	@Override
	public String toString() {
		return "ID = \t" + id +  " & Title = \t" + title + "\t\t & Author = \t" + author;
	}
}
