package liverpool.dissertation.SE2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import liverpool.dissertation.SE2.document.BookDocument;
import liverpool.dissertation.SE2.encryption.AES;
import liverpool.dissertation.SE2.entity.Book;
import liverpool.dissertation.SE2.repository.BooksDBRepository;
import liverpool.dissertation.SE2.repository.BooksSolrRepository;

@Service
public class BooksServiceImpl implements BooksService{
	
	
	@Autowired
	private BooksDBRepository booksDBRepository;
	
	@Autowired
	private BooksSolrRepository booksSolrRepository;

	@Override
	public List<Book> insertBooks(List<Book> books) {
		List<Book> insertedBooks = insertBooksInDBWithTitleEncrypted(books);
		int numberIndexed = indexBooks(insertedBooks);
		return insertedBooks;
	}
	
	private List<Book> insertBooksInDBWithTitleEncrypted(List<Book> books) {
		for(Book book : books) {
			Book encryptedBook = new Book();
			String encryptionKey = AES.getRandomEncryptionSecret();
			String encryptionSalt = AES.getRandomEncryptionSecret();
			encryptedBook.setTitle(AES.encrypt(book.getTitle(), encryptionKey, encryptionSalt));
			Book insertedBook = booksDBRepository.save(encryptedBook);
			book.setEncryptionKey(encryptionKey);
			book.setEncryptionSalt(encryptionSalt);
			book.setId(insertedBook.getId());
		}
		return books;
	}
	
	private int indexBooks(List<Book> books) {
		int numberIndexed = 0;
		for(Book book : books) {
			BookDocument bookDocument = new BookDocument();	
			bookDocument.setDatabaseId(new Long(book.getId()).toString());
			bookDocument.setTitle(book.getTitle());
			bookDocument.setEncryptionKey(book.getEncryptionKey());
			bookDocument.setEncryptionSalt(book.getEncryptionSalt());
			try {
				BookDocument inserted = booksSolrRepository.save(bookDocument);
				numberIndexed++;
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return numberIndexed;
	}
	
	@Override
	public List<Book> findBooksByTitle(String title, int pageSize) {
		Page<BookDocument> page = booksSolrRepository.findByTitle(title, PageRequest.of(0, pageSize));
		
		List<BookDocument> searchResult = page.getContent();
		
		List<Book> foundBooks = new ArrayList<Book>();
		for(BookDocument document : searchResult) {
			Book book = booksDBRepository.findById(new Long(document.getDatabaseId())).get();
			if(book != null) {
				Book foundBook = new Book();
				foundBook.setId(book.getId());
				foundBook.setTitle(AES.decrypt(book.getTitle(), document.getEncryptionKey(), document.getEncryptionSalt()));
				foundBooks.add(foundBook);
			}
				
		}

		return foundBooks;
	}
}

