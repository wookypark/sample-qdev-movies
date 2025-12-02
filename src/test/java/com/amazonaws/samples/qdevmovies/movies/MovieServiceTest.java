package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ahoy matey! These be the tests for our MovieService treasure hunting methods!
 * We be testing all the ways to search for movie treasures, arrr!
 */
public class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    void setUp() {
        // Create a test MovieService with predefined test data, me hearty!
        movieService = new MovieService() {
            private final List<Movie> testMovies = List.of(
                new Movie(1L, "The Prison Escape", "John Director", 1994, "Drama", "A tale of redemption", 142, 5.0),
                new Movie(2L, "The Family Boss", "Michael Filmmaker", 1972, "Crime/Drama", "A crime family saga", 175, 5.0),
                new Movie(3L, "Space Adventure", "Sci-Fi Director", 2020, "Sci-Fi", "An epic space journey", 120, 4.5)
            );

            @Override
            public List<Movie> getAllMovies() {
                return new ArrayList<>(testMovies);
            }

            @Override
            public Optional<Movie> getMovieById(Long id) {
                if (id == null || id <= 0) {
                    return Optional.empty();
                }
                return testMovies.stream()
                        .filter(movie -> movie.getId() == id)
                        .findFirst();
            }
        };
    }

    @Test
    @DisplayName("Arrr! Test searching for movie treasures by name")
    void testSearchMoviesByName() {
        // Test exact match, me hearty!
        List<Movie> results = movieService.searchMoviesByName("Prison");
        assertEquals(1, results.size());
        assertEquals("The Prison Escape", results.get(0).getMovieName());

        // Test partial match, ye scallywag!
        results = movieService.searchMoviesByName("The");
        assertEquals(2, results.size());

        // Test case insensitive search, arrr!
        results = movieService.searchMoviesByName("family");
        assertEquals(1, results.size());
        assertEquals("The Family Boss", results.get(0).getMovieName());

        // Test no matches found, shiver me timbers!
        results = movieService.searchMoviesByName("Nonexistent Movie");
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Batten down the hatches! Test edge cases for name search")
    void testSearchMoviesByNameEdgeCases() {
        // Test null input, ye landlubber!
        List<Movie> results = movieService.searchMoviesByName(null);
        assertTrue(results.isEmpty());

        // Test empty string, arrr!
        results = movieService.searchMoviesByName("");
        assertTrue(results.isEmpty());

        // Test whitespace only, me hearty!
        results = movieService.searchMoviesByName("   ");
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Shiver me timbers! Test searching for treasures by genre")
    void testSearchMoviesByGenre() {
        // Test exact genre match, arrr!
        List<Movie> results = movieService.searchMoviesByGenre("Drama");
        assertEquals(2, results.size()); // Both "Drama" and "Crime/Drama"

        // Test partial genre match, me hearty!
        results = movieService.searchMoviesByGenre("Crime");
        assertEquals(1, results.size());
        assertEquals("The Family Boss", results.get(0).getMovieName());

        // Test case insensitive search, ye scallywag!
        results = movieService.searchMoviesByGenre("sci-fi");
        assertEquals(1, results.size());
        assertEquals("Space Adventure", results.get(0).getMovieName());

        // Test no matches, batten down the hatches!
        results = movieService.searchMoviesByGenre("Horror");
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Arrr! Test edge cases for genre search")
    void testSearchMoviesByGenreEdgeCases() {
        // Test null input, ye landlubber!
        List<Movie> results = movieService.searchMoviesByGenre(null);
        assertTrue(results.isEmpty());

        // Test empty string, me hearty!
        results = movieService.searchMoviesByGenre("");
        assertTrue(results.isEmpty());

        // Test whitespace only, arrr!
        results = movieService.searchMoviesByGenre("   ");
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Ahoy! Test the ultimate treasure hunting method - combined search")
    void testSearchMoviesCombined() {
        // Test search by name only, me hearty!
        List<Movie> results = movieService.searchMovies("Prison", null, null);
        assertEquals(1, results.size());
        assertEquals("The Prison Escape", results.get(0).getMovieName());

        // Test search by id only, ye scallywag!
        results = movieService.searchMovies(null, 2L, null);
        assertEquals(1, results.size());
        assertEquals("The Family Boss", results.get(0).getMovieName());

        // Test search by genre only, arrr!
        results = movieService.searchMovies(null, null, "Sci-Fi");
        assertEquals(1, results.size());
        assertEquals("Space Adventure", results.get(0).getMovieName());

        // Test combined search - name and genre, shiver me timbers!
        results = movieService.searchMovies("The", null, "Drama");
        assertEquals(2, results.size());

        // Test combined search with no matches, batten down the hatches!
        results = movieService.searchMovies("Prison", null, "Sci-Fi");
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Test search with invalid id, ye landlubber!")
    void testSearchMoviesWithInvalidId() {
        // Test with negative id, arrr!
        List<Movie> results = movieService.searchMovies(null, -1L, null);
        assertTrue(results.isEmpty());

        // Test with zero id, me hearty!
        results = movieService.searchMovies(null, 0L, null);
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Test search with all empty parameters, ye scallywag!")
    void testSearchMoviesAllEmpty() {
        // When all parameters are empty, should return all movies, arrr!
        List<Movie> results = movieService.searchMovies(null, null, null);
        assertEquals(3, results.size());

        // Same with empty strings, me hearty!
        results = movieService.searchMovies("", null, "");
        assertEquals(3, results.size());
    }

    @Test
    @DisplayName("Test getting movie by id, like finding a specific treasure!")
    void testGetMovieById() {
        // Test valid id, arrr!
        Optional<Movie> result = movieService.getMovieById(1L);
        assertTrue(result.isPresent());
        assertEquals("The Prison Escape", result.get().getMovieName());

        // Test invalid id, shiver me timbers!
        result = movieService.getMovieById(999L);
        assertFalse(result.isPresent());

        // Test null id, ye landlubber!
        result = movieService.getMovieById(null);
        assertFalse(result.isPresent());

        // Test negative id, me hearty!
        result = movieService.getMovieById(-1L);
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Test getting all movie treasures, arrr!")
    void testGetAllMovies() {
        List<Movie> allMovies = movieService.getAllMovies();
        assertEquals(3, allMovies.size());
        
        // Verify the treasures are in the chest, me hearty!
        assertTrue(allMovies.stream().anyMatch(m -> m.getMovieName().equals("The Prison Escape")));
        assertTrue(allMovies.stream().anyMatch(m -> m.getMovieName().equals("The Family Boss")));
        assertTrue(allMovies.stream().anyMatch(m -> m.getMovieName().equals("Space Adventure")));
    }
}