package liverpool.dissertation.SE2.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilterFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
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
		
		int from = 0;
		int to = 10;
		int numberIndexed = 0;
		List<Book> insertedBooks = new ArrayList<Book>();
		while(to%10 <= books.size()%10) {
			List<Book> subList = books.subList(from, to);
			insertedBooks.addAll(insertBooksInDBWithTitleEncrypted(subList));
			numberIndexed += indexBooks(insertedBooks);
			from += 10;
			to += 10;
			try {
				Thread.sleep(2500);
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return insertedBooks;
	}
	
	private ArrayList<String> analyzeText(String title) {
		StandardAnalyzer analyzer = new StandardAnalyzer();
		TokenStream stream = analyzer.tokenStream(null, title);
		stream = new EnglishMinimalStemFilter(stream);
		stream = new PorterStemFilterFactory(new HashMap<String, String>()).create(stream);
//		stream = new PorterStemFilter(stream);
		ArrayList<String> tokens = new ArrayList<String>();
		try {
			stream.reset();
			while(stream.incrementToken()) {
				String token = stream.getAttribute(CharTermAttribute.class).toString();
				tokens.add(token);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return tokens;
	}
	
	private List<Book> insertBooksInDBWithTitleEncrypted(List<Book> books) {
		for(Book book : books) {
			Book encryptedBook = new Book();
			String iv = AES.generateEncryptionIV();
			encryptedBook.setTitle(AES.encrypt(book.getTitle(), Base64.getDecoder().decode(iv)));
			Book insertedBook = booksDBRepository.save(encryptedBook);
			book.setId(insertedBook.getId());
			book.setEncryptionIV(iv);
		}
		return books;
	}
	
	private int indexBooks(List<Book> books) {
		int numberIndexed = 0;
		for(Book book : books) {
			BookDocument bookDocument = new BookDocument();	
			bookDocument.setDatabaseId(new Long(book.getId()).toString());
			bookDocument.setTitle(book.getTitle());
			bookDocument.setEncryptionSalt(book.getEncryptionIV());
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
	public Set<Book> findBooksByTitle(String title, int pageSize) {
		
		ArrayList<String> titleTokens = analyzeText(title);
		
		System.out.println("===>>>> " + titleTokens);
		
		Set<BookDocument> solrSearchResult = new HashSet<BookDocument>();
		for(String token : titleTokens) {
			Page<BookDocument> page = booksSolrRepository.findByTitle(token, PageRequest.of(0, pageSize));
			solrSearchResult.addAll(page.getContent());		
		}
		
		Set<Book> foundBooks = new HashSet<Book>();
		for(BookDocument document : solrSearchResult) {
			Book book = booksDBRepository.findById(new Long(document.getDatabaseId())).get();
			if(book != null) {
				Book foundBook = new Book();
				foundBook.setId(book.getId());
				byte[] iv = Base64.getDecoder().decode(document.getEncryptionSalt());
				foundBook.setTitle(AES.decrypt(book.getTitle(), iv));
				foundBooks.add(foundBook);
				System.out.println("===>>> " + document.getEncryptionSalt());
			}
				
		}
		return foundBooks;
	}
}

