package library.app.LibraryAPI.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import library.app.LibraryAPI.models.BookDetail;

public interface BookDetailRepo extends JpaRepository<BookDetail, Long> {

}
