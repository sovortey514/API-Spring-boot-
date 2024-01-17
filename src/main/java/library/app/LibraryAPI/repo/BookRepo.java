package library.app.LibraryAPI.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import library.app.LibraryAPI.models.Book;

public interface BookRepo extends JpaRepository<Book, Long> {
    // Your other repository methods...
    @Query("SELECT b FROM Book b WHERE b.catalog.id = :catalogId")
    List<Book> findByCatalogId(@Param("catalogId") Long catalogId);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Book> searchByNameContainingIgnoreCase(@Param("search") String search);

    @Query("SELECT b FROM BookDetail bd JOIN bd.book b WHERE bd.author.id = :authorId")
    List<Book> findBooksByAuthor(@Param("authorId") Long authorId);

}
