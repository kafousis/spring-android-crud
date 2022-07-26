package com.github.kafousis;

import com.github.kafousis.entities.Author;
import com.github.kafousis.entities.Book;
import com.github.kafousis.entities.Genre;
import com.github.kafousis.entities.Publisher;
import com.github.kafousis.repositories.AuthorRepository;
import com.github.kafousis.repositories.BookRepository;
import com.github.kafousis.repositories.GenreRepository;
import com.github.kafousis.repositories.PublisherRepository;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication @Slf4j
public class BooksBackendApplication implements CommandLineRunner {

	// ### DO NOT INCREASE - POSSIBLE OVERFLOW ### //
	private static final int TOTAL_GENRES = 30;
	private static final int TOTAL_PUBLISHERS = 50;
	private static final int TOTAL_AUTHORS = 100;
	private static final int TOTAL_BOOKS = 180;
	// ############################################ //

	// ---

	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private PublisherRepository publisherRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private BookRepository bookRepository;

	// ---

	public static void main(String[] args) {
		SpringApplication.run(BooksBackendApplication.class, args);
	}

	// ---

	@Override
	public void run(String... args) throws Exception {

		Faker faker = new Faker();

		// break after unused loops
		int loopLimit = 1000;

		// --- Genres
		for (int i = 0; i<TOTAL_GENRES; i++) {
			String genreName = faker.book().genre();
			if (genreRepository.existsGenreByName(genreName)) {
				loopLimit--;
				if (loopLimit == 0) break;
				i--;
			} else {
				//log.info("i="+i +" - inserting genre " + genreName);
				Genre genre = new Genre().setName(genreName);
				genreRepository.save(genre);
			}
		}
		log.info(genreRepository.count() + " genres saved.");

		// --- Publishers
		for (int i = 0; i<TOTAL_PUBLISHERS; i++) {
			String publisherName = faker.book().publisher();
			if (publisherRepository.existsPublisherByName(publisherName)) {
				loopLimit--;
				if (loopLimit == 0) break;
				i--;
			} else {
				//log.info("i="+i +" - inserting publisher " + publisherName);
				Publisher publisher = new Publisher().setName(publisherName).setCountry(faker.country().name());
				publisherRepository.save(publisher);
			}
		}
		log.info(publisherRepository.count() + " publishers saved.");

		// --- Authors
		String authorName = null;
		for (int i = 0; i<TOTAL_AUTHORS; i++) {
			authorName = faker.book().author();
			if (authorRepository.existsAuthorByFullName(authorName)) {
				loopLimit--;
				if (loopLimit == 0) break;
				i--;
			} else {
				//log.info("i="+i +" - inserting author " + authorName);
				Author author = new Author().setFullName(authorName).setCountry(faker.country().name());
				authorRepository.save(author);
			}
		}
		log.info(authorRepository.count() + " authors saved.");

		// --- Books
		for (int i = 0; i<TOTAL_BOOKS; i++) {
			String bookTitle = faker.book().title();
			String bookIsbn = faker.code().isbn13();
			if (bookRepository.existsBookByTitle(bookTitle) || bookRepository.existsBookByIsbn(bookIsbn)) {
				loopLimit--;
				if (loopLimit == 0) break;
				i--;
			} else {
				//log.info("i="+i +" - inserting book " + bookTitle);

				Book book = new Book();
				book.setTitle(bookTitle);
				book.setIsbn(bookIsbn);
				book.setTotalPages(faker.random().nextInt(50, 500));
				book.setPublishedYear(faker.random().nextInt(1960, 2022));

				book.setPublisher(publisherRepository.getReferenceById(faker.random().nextLong(1, TOTAL_PUBLISHERS)));
				book.setGenre(genreRepository.getReferenceById(faker.random().nextLong(1, TOTAL_GENRES)));

				if (i % 2 == 0) {
					// even (i) generates a book with 2 authors
					Long author1_id = faker.random().nextLong(1, TOTAL_AUTHORS);
					Long author2_id = faker.random().nextLong(1, TOTAL_AUTHORS);

					while (author1_id.equals(author2_id)) {
						author2_id = faker.random().nextLong(1, TOTAL_AUTHORS);
					}

					Author author1 = authorRepository.findById(author1_id).get();
					Author author2 = authorRepository.findById(author2_id).get();
					book.setAuthors(new HashSet<>(Arrays.asList(author1, author2)));
				} else {
					// odd (i) generates a book with 1 author
					Long authorId = faker.random().nextLong(1, TOTAL_AUTHORS);
					Author author = authorRepository.findById(authorId).get();
					book.setAuthors(Collections.singleton(author));
				}
				bookRepository.save(book);
			}
		}
		log.info(bookRepository.count() + " books saved.");
		log.info("Finished inserting!");
	}
}
