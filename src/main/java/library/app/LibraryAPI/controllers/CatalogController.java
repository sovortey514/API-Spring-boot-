package library.app.LibraryAPI.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import library.app.LibraryAPI.models.Catalog;
import library.app.LibraryAPI.repo.CatalogRepo;

@RestController
public class CatalogController {

    @Autowired
    protected CatalogRepo catalogRepo;

    @GetMapping(value = "/catalog")
    public List<Catalog> getAllCatalogs() {
        return catalogRepo.findAll();
    }

    @GetMapping(value = "/catalog/{id}")
    public ResponseEntity<Catalog> getCatalogById(@PathVariable Long id) {
        Optional<Catalog> catalogOptional = catalogRepo.findById(id);

        if (catalogOptional.isPresent()) {
            Catalog catalog = catalogOptional.get();
            return ResponseEntity.ok(catalog);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/catalog")
    public ResponseEntity<?> createCatalog(@Validated @RequestBody Catalog catalog, BindingResult bindingResult) {
        // validation
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed. Please check your input.");
        }

        if (catalog.getName() == null || catalog.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Catalog name cannot be empty.");
        }

        if (catalog.getName().length() > 60) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Catalog name must be less that 60 letters.");
        }
        // end validation

        Catalog createdCatalog = catalogRepo.save(catalog);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCatalog);
    }

    @PutMapping(value = "/catalog/{id}")
    public ResponseEntity<?> updateCatalog(@PathVariable Long id, @RequestBody Catalog catalog,
            BindingResult bindingResult) {
        // validation
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed. Please check your input.");
        }

        if (catalog.getName() == null || catalog.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Catalog name cannot be empty.");
        }

        if (catalog.getName().length() > 60) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Catalog name must be less that 60 letters.");
        }
        // end validation
        Catalog udpatedCatalog = catalogRepo.findById(id).get();
        udpatedCatalog.setName(catalog.getName());
        Catalog updatedCatalog = catalogRepo.save(udpatedCatalog);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedCatalog);
    }

    @DeleteMapping(value = "/catalog/{id}")
    public ResponseEntity<String> deleteCatalog(@PathVariable Long id) {
        // Check if catalog exists
        if (catalogRepo.existsById(id)) {
            Catalog deletedCatalog = catalogRepo.findById(id).get();
            catalogRepo.delete(deletedCatalog);
            return ResponseEntity.status(HttpStatus.OK).body("Catalog deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Catalog not found");
        }
    }
}
