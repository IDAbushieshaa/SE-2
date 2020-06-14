package liverpool.dissertation.SE2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import liverpool.dissertation.SE2.entity.Book;

@Repository
public interface BooksDBRepository extends JpaRepository<Book, Long>{
}
