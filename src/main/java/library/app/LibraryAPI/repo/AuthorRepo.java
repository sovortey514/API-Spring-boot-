package library.app.LibraryAPI.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import library.app.LibraryAPI.models.Author;

public interface AuthorRepo extends JpaRepository<Author, Long> {

}
