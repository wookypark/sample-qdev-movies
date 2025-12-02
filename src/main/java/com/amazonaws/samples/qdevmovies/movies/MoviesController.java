package com.amazonaws.samples.qdevmovies.movies;

import com.amazonaws.samples.qdevmovies.utils.MovieIconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@Controller
public class MoviesController {
    private static final Logger logger = LogManager.getLogger(MoviesController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/movies")
    public String getMovies(org.springframework.ui.Model model) {
        logger.info("Fetching movies");
        model.addAttribute("movies", movieService.getAllMovies());
        return "movies";
    }

    /**
     * Ahoy matey! This be the treasure hunting endpoint for searching movie treasures!
     * Ye can search by name, id, or genre - or combine them all like a true pirate captain!
     * 
     * @param name The name of the movie treasure ye be searchin' for (optional)
     * @param id The specific id of the treasure ye want (optional)
     * @param genre The genre of adventures ye be huntin' (optional)
     * @param model The model to carry yer treasures to the view
     * @return The movies template with search results, arrr!
     */
    @GetMapping("/movies/search")
    public String searchMovies(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "genre", required = false) String genre,
            org.springframework.ui.Model model) {
        
        logger.info("Ahoy! Starting treasure hunt with parameters - name: '{}', id: {}, genre: '{}'", 
                   name, id, genre);
        
        try {
            // Validate id parameter if provided, ye scallywag!
            if (id != null && id <= 0) {
                logger.warn("Arrr! Invalid movie id provided: {}", id);
                model.addAttribute("title", "Invalid Search Parameters");
                model.addAttribute("message", "Shiver me timbers! The movie id must be a positive number, ye landlubber!");
                return "error";
            }
            
            // Perform the treasure hunt!
            List<Movie> searchResults = movieService.searchMovies(name, id, genre);
            
            // Check if we found any treasures
            if (searchResults.isEmpty()) {
                logger.info("No movie treasures found matching the search criteria");
                model.addAttribute("movies", searchResults);
                model.addAttribute("searchMessage", "Arrr! No treasures found matching yer search, matey! Try different search terms.");
                model.addAttribute("searchPerformed", true);
            } else {
                logger.info("Found {} movie treasures matching the search criteria", searchResults.size());
                model.addAttribute("movies", searchResults);
                model.addAttribute("searchMessage", String.format("Ahoy! Found %d movie treasure%s for ye, me hearty!", 
                                                                 searchResults.size(), 
                                                                 searchResults.size() == 1 ? "" : "s"));
                model.addAttribute("searchPerformed", true);
            }
            
            // Preserve search parameters for the form
            model.addAttribute("searchName", name != null ? name : "");
            model.addAttribute("searchId", id != null ? id.toString() : "");
            model.addAttribute("searchGenre", genre != null ? genre : "");
            
            return "movies";
            
        } catch (Exception e) {
            logger.error("Arrr! Error during treasure hunt: {}", e.getMessage(), e);
            model.addAttribute("title", "Search Error");
            model.addAttribute("message", "Batten down the hatches! Something went wrong during the treasure hunt: " + e.getMessage());
            return "error";
        }
    }

    /**
     * REST API endpoint for movie treasure hunting - returns JSON like a proper pirate's map!
     * Perfect for other ships (applications) that want to search our treasure chest!
     * 
     * @param name The name of the movie treasure (optional)
     * @param id The specific treasure id (optional)  
     * @param genre The genre of treasures (optional)
     * @return ResponseEntity with list of movie treasures in JSON format, arrr!
     */
    @GetMapping("/api/movies/search")
    @ResponseBody
    public ResponseEntity<?> searchMoviesApi(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "genre", required = false) String genre) {
        
        logger.info("API treasure hunt requested with name: '{}', id: {}, genre: '{}'", name, id, genre);
        
        try {
            // Validate id parameter, ye scallywag!
            if (id != null && id <= 0) {
                logger.warn("Invalid movie id provided to API: {}", id);
                return ResponseEntity.badRequest()
                        .body(new SearchErrorResponse("Arrr! Movie id must be a positive number, ye landlubber!"));
            }
            
            // Perform the treasure hunt!
            List<Movie> searchResults = movieService.searchMovies(name, id, genre);
            
            logger.info("API treasure hunt complete! Found {} treasures", searchResults.size());
            return ResponseEntity.ok(new SearchResponse(searchResults, searchResults.size()));
            
        } catch (Exception e) {
            logger.error("Error during API treasure hunt: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(new SearchErrorResponse("Shiver me timbers! Error during treasure hunt: " + e.getMessage()));
        }
    }

    @GetMapping("/movies/{id}/details")
    public String getMovieDetails(@PathVariable("id") Long movieId, org.springframework.ui.Model model) {
        logger.info("Fetching details for movie ID: {}", movieId);
        
        Optional<Movie> movieOpt = movieService.getMovieById(movieId);
        if (!movieOpt.isPresent()) {
            logger.warn("Movie with ID {} not found", movieId);
            model.addAttribute("title", "Movie Not Found");
            model.addAttribute("message", "Movie with ID " + movieId + " was not found.");
            return "error";
        }
        
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);
        model.addAttribute("movieIcon", MovieIconUtils.getMovieIcon(movie.getMovieName()));
        model.addAttribute("allReviews", reviewService.getReviewsForMovie(movie.getId()));
        
        return "movie-details";
    }

    /**
     * Response class for successful API treasure hunts, arrr!
     */
    public static class SearchResponse {
        private final List<Movie> movies;
        private final int totalCount;
        private final String message;

        public SearchResponse(List<Movie> movies, int totalCount) {
            this.movies = movies;
            this.totalCount = totalCount;
            this.message = totalCount == 0 ? 
                "Arrr! No treasures found, matey!" : 
                String.format("Ahoy! Found %d movie treasure%s!", totalCount, totalCount == 1 ? "" : "s");
        }

        public List<Movie> getMovies() { return movies; }
        public int getTotalCount() { return totalCount; }
        public String getMessage() { return message; }
    }

    /**
     * Response class for when the treasure hunt goes awry, ye scallywag!
     */
    public static class SearchErrorResponse {
        private final String error;
        private final long timestamp;

        public SearchErrorResponse(String error) {
            this.error = error;
            this.timestamp = System.currentTimeMillis();
        }

        public String getError() { return error; }
        public long getTimestamp() { return timestamp; }
    }
}