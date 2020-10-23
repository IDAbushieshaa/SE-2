package liverpool.dissertation.SE2.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import liverpool.dissertation.SE2.command.AddBooksCommand;
import liverpool.dissertation.SE2.command.FindBooksCommand;
import liverpool.dissertation.SE2.entity.Book;
import liverpool.dissertation.SE2.response.AddBooksResponse;
import liverpool.dissertation.SE2.response.FindBooksResponse;
import liverpool.dissertation.SE2.service.BooksService;

@RestController
@RequestMapping(path="/books")
public class BooksController {
	
	
	
	public static void main(String[] args) {
		
	}
	
	
	@Autowired
	BooksService booksService;
	
	@PostMapping(path= "/addBooks", consumes = "application/json", produces = "application/json")
	public AddBooksResponse addBooks(@RequestBody AddBooksCommand command) {
		
		List<Book> books = command.getBooks();
		
		List<Book> insertedBooks = booksService.insertBooks(books);
		
		AddBooksResponse response = new AddBooksResponse();
		response.setSuccess(true);
		response.setStatus("200");
		return response;
	}
	
	
	@PostMapping(path = "/findBooksByTitle", consumes = "application/json", produces = "application/json")
	public  FindBooksResponse findBooks(@RequestBody FindBooksCommand command) {
		Set<Book> result = booksService.findBooksByTitle(command.getSearchTerm(), 500);
		FindBooksResponse response = new FindBooksResponse();
		response.setBooks(result);
		response.setStatus("200");
		response.setSuccess(true);
		response.setCount(result.size());
		return response;
	}
	
}
