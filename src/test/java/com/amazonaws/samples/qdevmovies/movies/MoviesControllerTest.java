package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.ui.Model;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ahoy matey! These be the tests for our MoviesController treasure hunting endpoints!
 * We be testing all the ways pirates can search for movie treasures, arrr!
 */
public class MoviesControllerTest {

    private MoviesController moviesController;
    private Model model;
    private MovieService mockMovieService;
    private ReviewService mockReviewService;

    @BeforeEach
    public void setUp() {
        moviesController = new MoviesController();
        model = new ExtendedModelMap();
        
        // Create mock services with search capabilities, me hearty!
        mockMovieService = new MovieService() {
            @Override
            public List<Movie> getAllMovies() {
                return Arrays.asList(
                    new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 4.5),
                    new Movie(2L, "Pirate Adventure", "Captain Director", 2022, "Adventure", "Arrr! A pirate tale", 130, 4.8)
                );
            }
            
            @Override
            public Optional<Movie> getMovieById(Long id) {
                if (id == 1L) {
                    return Optional.of(new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 4.5));
                } else if (id == 2L) {
                    return Optional.of(new Movie(2L, "Pirate Adventure", "Captain Director", 2022, "Adventure", "Arrr! A pirate tale", 130, 4.8));
                }
                return Optional.empty();
            }

            @Override
            public List<Movie> searchMovies(String movieName, Long movieId, String genre) {
                List<Movie> allMovies = getAllMovies();
                List<Movie> results = new ArrayList<>();
                
                for (Movie movie : allMovies) {
                    boolean matches = true;
                    
                    if (movieName != null && !movieName.trim().isEmpty()) {
                        matches = matches && movie.getMovieName().toLowerCase().contains(movieName.toLowerCase());
                    }
                    
                    if (movieId != null && movieId > 0) {
                        matches = matches && movie.getId() == movieId;
                    }
                    
                    if (genre != null && !genre.trim().isEmpty()) {
                        matches = matches && movie.getGenre().toLowerCase().contains(genre.toLowerCase());
                    }
                    
                    if (matches) {
                        results.add(movie);
                    }
                }
                
                return results;
            }
        };
        
        mockReviewService = new ReviewService() {
            @Override
            public List<Review> getReviewsForMovie(long movieId) {
                return new ArrayList<>();
            }
        };
        
        // Inject mocks using reflection, ye scallywag!
        try {
            java.lang.reflect.Field movieServiceField = MoviesController.class.getDeclaredField("movieService");
            movieServiceField.setAccessible(true);
            movieServiceField.set(moviesController, mockMovieService);
            
            java.lang.reflect.Field reviewServiceField = MoviesController.class.getDeclaredField("reviewService");
            reviewServiceField.setAccessible(true);
            reviewServiceField.set(moviesController, mockReviewService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock services", e);
        }
    }

    @Test
    @DisplayName("Arrr! Test the main movies page loads all treasures")
    public void testGetMovies() {
        String result = moviesController.getMovies(model);
        assertNotNull(result);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("movies"));
    }

    @Test
    @DisplayName("Shiver me timbers! Test movie details page")
    public void testGetMovieDetails() {
        String result = moviesController.getMovieDetails(1L, model);
        assertNotNull(result);
        assertEquals("movie-details", result);
        assertTrue(model.containsAttribute("movie"));
    }

    @Test
    @DisplayName("Batten down the hatches! Test movie not found")
    public void testGetMovieDetailsNotFound() {
        String result = moviesController.getMovieDetails(999L, model);
        assertNotNull(result);
        assertEquals("error", result);
        assertTrue(model.containsAttribute("title"));
        assertTrue(model.containsAttribute("message"));
    }

    @Test
    @DisplayName("Ahoy! Test treasure hunting by movie name")
    public void testSearchMoviesByName() {
        String result = moviesController.searchMovies("Test", null, null, model);
        assertNotNull(result);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("movies"));
        assertTrue(model.containsAttribute("searchPerformed"));
        assertTrue(model.containsAttribute("searchMessage"));
        assertEquals(true, model.getAttribute("searchPerformed"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertEquals(1, movies.size());
        assertEquals("Test Movie", movies.get(0).getMovieName());
    }

    @Test
    @DisplayName("Me hearty! Test treasure hunting by movie id")
    public void testSearchMoviesById() {
        String result = moviesController.searchMovies(null, 2L, null, model);
        assertNotNull(result);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("searchPerformed"));
        assertEquals(true, model.getAttribute("searchPerformed"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertEquals(1, movies.size());
        assertEquals("Pirate Adventure", movies.get(0).getMovieName());
    }

    @Test
    @DisplayName("Ye scallywag! Test treasure hunting by genre")
    public void testSearchMoviesByGenre() {
        String result = moviesController.searchMovies(null, null, "Adventure", model);
        assertNotNull(result);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("searchPerformed"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertEquals(1, movies.size());
        assertEquals("Pirate Adventure", movies.get(0).getMovieName());
    }

    @Test
    @DisplayName("Arrr! Test combined treasure hunting")
    public void testSearchMoviesCombined() {
        String result = moviesController.searchMovies("Pirate", 2L, "Adventure", model);
        assertNotNull(result);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("searchPerformed"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertEquals(1, movies.size());
        assertEquals("Pirate Adventure", movies.get(0).getMovieName());
    }

    @Test
    @DisplayName("Shiver me timbers! Test search with no results")
    public void testSearchMoviesNoResults() {
        String result = moviesController.searchMovies("Nonexistent", null, null, model);
        assertNotNull(result);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("searchPerformed"));
        assertEquals(true, model.getAttribute("searchPerformed"));
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertTrue(movies.isEmpty());
        
        String searchMessage = (String) model.getAttribute("searchMessage");
        assertTrue(searchMessage.contains("No treasures found"));
    }

    @Test
    @DisplayName("Ye landlubber! Test search with invalid id")
    public void testSearchMoviesInvalidId() {
        String result = moviesController.searchMovies(null, -1L, null, model);
        assertNotNull(result);
        assertEquals("error", result);
        assertTrue(model.containsAttribute("title"));
        assertTrue(model.containsAttribute("message"));
        
        String message = (String) model.getAttribute("message");
        assertTrue(message.contains("positive number"));
    }

    @Test
    @DisplayName("Ye scallywag! Test search parameter preservation")
    public void testSearchParameterPreservation() {
        String result = moviesController.searchMovies("Test", 1L, "Drama", model);
        assertNotNull(result);
        assertEquals("movies", result);
        
        assertEquals("Test", model.getAttribute("searchName"));
        assertEquals("1", model.getAttribute("searchId"));
        assertEquals("Drama", model.getAttribute("searchGenre"));
    }

    @Test
    @DisplayName("Batten down the hatches! Test search with empty parameters")
    public void testSearchMoviesEmptyParameters() {
        String result = moviesController.searchMovies("", null, "", model);
        assertNotNull(result);
        assertEquals("movies", result);
        assertTrue(model.containsAttribute("searchPerformed"));
        
        // Should return all movies when all parameters are empty, arrr!
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertEquals(2, movies.size());
    }

    @Test
    @DisplayName("Shiver me timbers! Test MovieService integration")
    public void testMovieServiceIntegration() {
        List<Movie> movies = mockMovieService.getAllMovies();
        assertEquals(2, movies.size());
        assertEquals("Test Movie", movies.get(0).getMovieName());
        assertEquals("Pirate Adventure", movies.get(1).getMovieName());
    }

    @Test
    @DisplayName("Arrr! Test search movies method in service")
    public void testSearchMoviesServiceIntegration() {
        // Test name search, me hearty!
        List<Movie> results = mockMovieService.searchMovies("Test", null, null);
        assertEquals(1, results.size());
        assertEquals("Test Movie", results.get(0).getMovieName());
        
        // Test id search, ye scallywag!
        results = mockMovieService.searchMovies(null, 2L, null);
        assertEquals(1, results.size());
        assertEquals("Pirate Adventure", results.get(0).getMovieName());
        
        // Test genre search, arrr!
        results = mockMovieService.searchMovies(null, null, "Drama");
        assertEquals(1, results.size());
        assertEquals("Test Movie", results.get(0).getMovieName());
        
        // Test no matches, batten down the hatches!
        results = mockMovieService.searchMovies("Nonexistent", null, null);
        assertTrue(results.isEmpty());
    }
}
