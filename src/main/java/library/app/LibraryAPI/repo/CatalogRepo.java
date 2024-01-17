package library.app.LibraryAPI.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import library.app.LibraryAPI.models.Catalog;

public interface CatalogRepo extends JpaRepository<Catalog, Long> {

}
