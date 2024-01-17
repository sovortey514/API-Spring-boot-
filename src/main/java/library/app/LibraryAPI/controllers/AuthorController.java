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

import library.app.LibraryAPI.models.Author;
import library.app.LibraryAPI.repo.AuthorRepo;

@RestController
public class AuthorController {
    @Autowired
    private AuthorRepo authorRepo;

    @GetMapping(value = "/author")
    public List<Author> getAllAuthors() {
        return authorRepo.findAll();
    }

    @GetMapping(value = "/author/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        Optional<Author> authorOptional = authorRepo.findById(id);

        if (authorOptional.isPresent()) {
            Author author = authorOptional.get();
            return ResponseEntity.ok(author);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/author")
    public ResponseEntity<?> createAuthor(@Validated @RequestBody Author author, BindingResult bindingResult) {
        // validation
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed. Please check your input.");
        }

        if (author.getName() == null || author.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Author name cannot be empty.");
        }

        if (author.getName().length() > 60) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Author name must be less that 60 letters.");
        }
        // end validation

        Author createdAuthor = authorRepo.save(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }

    @PutMapping(value = "/author/{id}")
    public ResponseEntity<?> updateAuthor(@PathVariable Long id, @RequestBody Author author,
            BindingResult bindingResult) {
        // validation
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed. Please check your input.");
        }

        if (author.getName() == null || author.getName().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("author name cannot be empty.");
        }

        if (author.getName().length() > 60) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("author name must be less that 60 letters.");
        }
        // end validation
        Author udpatedauthor = authorRepo.findById(id).get();
        udpatedauthor.setName(author.getName());
        Author updatedauthor = authorRepo.save(udpatedauthor);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedauthor);
    }

    @DeleteMapping(value = "/author/{id}")
    public ResponseEntity<String> deletedAuthor(@PathVariable Long id) {
        // Check if catalog exists
        if (authorRepo.existsById(id)) {
            Author deletedAuthor = authorRepo.findById(id).get();
            authorRepo.delete(deletedAuthor);
            return ResponseEntity.status(HttpStatus.OK).body("Author deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Author not found");
        }
    }

}
