package liverpool.dissertation.SE2.service;

import java.util.List;
import java.util.Set;

import liverpool.dissertation.SE2.entity.Book;

public interface BooksService {
	
	List<Book> insertBooks(List<Book> books);
	
	Set<Book> findBooksByTitle(String title, int pageSize);

}
