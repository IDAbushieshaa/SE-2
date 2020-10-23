package liverpool.dissertation.SE2.response;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import liverpool.dissertation.SE2.entity.Book;

public class FindBooksResponse {
	
	private boolean success;
	private String status;
	int count;
	
	private Set<Book> books = new HashSet<Book>();

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<Book> getBooks() {
		return books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}