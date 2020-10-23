package liverpool.dissertation.SE2.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

@SolrDocument(collection = "BOOKS-SE-2")
public class BookDocument {
	
	@Id
	@Indexed(name="id", type="string")
	private String solrId;
	
	
	@Indexed(name="TITLE", type="bookTitle")
	private String title;
	
	
	@Field(value="DB_ID")
	private String databaseId;
	
	@Field(value="ENCRYPTION_IV")
	private String encryptionSalt;


	public String getSolrId() {
		return solrId;
	}
	public void setSolrId(String solrId) {
		this.solrId = solrId;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}


	public String getDatabaseId() {
		return databaseId;
	}
	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}
	
	public String getEncryptionSalt() {
		return encryptionSalt;
	}
	public void setEncryptionSalt(String encryptionSalt) {
		this.encryptionSalt = encryptionSalt;
	}
	
	@Override
	public String toString() {
		return "Database ID = " + databaseId + " & solrId = " + solrId;
	}
	
	@Override
	public boolean equals(Object object) {
		
		if(object == null)
			return false;
		
		if(object instanceof BookDocument == false)
			return false;
		
		if(this.databaseId == null)
			return false;
		
		BookDocument otherObject = (BookDocument) object;
		return this.databaseId.equalsIgnoreCase(otherObject.getDatabaseId());
	}
	
	@Override
	public int hashCode() {
		return databaseId.hashCode();
	}
}
