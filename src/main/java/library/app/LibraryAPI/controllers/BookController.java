package library.app.LibraryAPI.controllers;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import library.app.LibraryAPI.models.Author;
import library.app.LibraryAPI.models.Book;
import library.app.LibraryAPI.models.BookDetail;
import library.app.LibraryAPI.repo.AuthorRepo;
import library.app.LibraryAPI.repo.BookDetailRepo;
import library.app.LibraryAPI.repo.BookRepo;
import library.app.LibraryAPI.repo.CatalogRepo;

@RestController
public class BookController {

    @Autowired
    private BookRepo bookRepo;
    @Autowired
    private CatalogRepo catalogRepo;
    @Autowired
    private BookDetailRepo bookDetailRepo;
    @Autowired
    private AuthorRepo authorRepo;

    @GetMapping("/book")
    public ResponseEntity<List<Book>> getBooks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long filter,
            @RequestParam(required = false) Long author) {
        List<Book> books;

        if (search != null && !search.isEmpty()) {
            books = bookRepo.searchByNameContainingIgnoreCase(search);
        } else if (filter != null) {
            books = bookRepo.findByCatalogId(filter);
        } else if (author != null) {
            books = bookRepo.findBooksByAuthor(author);
        } else {
            books = bookRepo.findAll();
        }

        return ResponseEntity.ok(books);
    }

    @PostMapping(value = "/book")
    public ResponseEntity<?> createBook(@Validated @RequestBody CreateBookDto book,
            BindingResult bindingResult) {
        // validation
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed.Please check your input.");
        }

        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("book name cannot be empty.");
        }

        if (book.getTitle().length() > 60) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("book name must be less that 60 letters.");
        }
        // end validation

        // Insert catalog
        Book createBook = new Book();
        createBook.setCatalog(catalogRepo.findById(book.getCatalog_id()).get());
        createBook.setTitle(book.getTitle());
        Book createdBook = bookRepo.save(createBook);
        // Insert to book detail
        for (Long Id : book.getAuthors()) {
            BookDetail bookDetail = new BookDetail();
            bookDetail.setBook(createdBook);
            Author author = authorRepo.findById(Id).get();
            bookDetail.setAuthor(author);
            bookDetailRepo.save(bookDetail);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @PutMapping(value = "/book/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book book,
            BindingResult bindingResult) {
        // validation
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed. Please check your input.");
        }

        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("book name cannot be empty.");
        }

        if (book.getTitle().length() > 60) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("book name must be less that 60 letters.");
        }
        // end validation
        Book udpatedbook = bookRepo.findById(id).get();
        udpatedbook.setTitle(book.getTitle());
        Book updatedbook = bookRepo.save(udpatedbook);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedbook);
    }

    @DeleteMapping(value = "/book/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        // Check if catalog exists
        if (bookRepo.existsById(id)) {
            Book deleteBook = bookRepo.findById(id).get();
            bookRepo.delete(deleteBook);
            return ResponseEntity.status(HttpStatus.OK).body("Book deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
    }
}

class CreateBookDto {
    private long catalog_id;
    private String title;
    private List<Long> authors;

    public long getCatalog_id() {
        return catalog_id;
    }

    public void setCatalog_id(long catalog_id) {
        this.catalog_id = catalog_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Long> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Long> authors) {
        this.authors = authors;
    }
}