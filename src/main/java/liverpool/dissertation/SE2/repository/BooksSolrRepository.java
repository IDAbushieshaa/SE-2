package liverpool.dissertation.SE2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.data.solr.repository.Spellcheck;
import org.springframework.stereotype.Repository;

import liverpool.dissertation.SE2.document.BookDocument;

@Repository
public interface BooksSolrRepository extends SolrCrudRepository<BookDocument, Integer>{
	
	BookDocument findBySolrId(Integer id);
	
	@Query(requestHandler="/spell", value="TITLE:*?0*")
	@Spellcheck(count=5, extendedResults=true)
	Page<BookDocument> findByTitle(String searchTerm, Pageable pageable);
	
	@Query("DB_ID:*?0*")
	Page<BookDocument> findByDatabaseId(String searchTerm, Pageable pageable);

}
