package com.amazonaws.samples.qdevmovies.movies;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private static final Logger logger = LogManager.getLogger(MovieService.class);
    private final List<Movie> movies;
    private final Map<Long, Movie> movieMap;

    public MovieService() {
        this.movies = loadMoviesFromJson();
        this.movieMap = new HashMap<>();
        for (Movie movie : movies) {
            movieMap.put(movie.getId(), movie);
        }
    }

    private List<Movie> loadMoviesFromJson() {
        List<Movie> movieList = new ArrayList<>();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("movies.json");
            if (inputStream != null) {
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
                String jsonContent = scanner.useDelimiter("\\A").next();
                scanner.close();
                
                JSONArray moviesArray = new JSONArray(jsonContent);
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movieObj = moviesArray.getJSONObject(i);
                    movieList.add(new Movie(
                        movieObj.getLong("id"),
                        movieObj.getString("movieName"),
                        movieObj.getString("director"),
                        movieObj.getInt("year"),
                        movieObj.getString("genre"),
                        movieObj.getString("description"),
                        movieObj.getInt("duration"),
                        movieObj.getDouble("imdbRating")
                    ));
                }
            }
        } catch (Exception e) {
            logger.error("Failed to load movies from JSON: {}", e.getMessage());
        }
        return movieList;
    }

    public List<Movie> getAllMovies() {
        return movies;
    }

    public Optional<Movie> getMovieById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(movieMap.get(id));
    }

    /**
     * Ahoy matey! This here method searches for movie treasures by name.
     * It be case-insensitive and finds partial matches like a true pirate!
     * 
     * @param movieName The name of the treasure (movie) ye be searchin' for
     * @return A list of movie treasures that match yer search, arrr!
     */
    public List<Movie> searchMoviesByName(String movieName) {
        if (movieName == null || movieName.trim().isEmpty()) {
            logger.warn("Arrr! Empty movie name provided for search, returning empty treasure chest");
            return new ArrayList<>();
        }
        
        String searchTerm = movieName.trim().toLowerCase();
        logger.info("Searching for movie treasures with name containing: {}", searchTerm);
        
        return movies.stream()
                .filter(movie -> movie.getMovieName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    /**
     * Batten down the hatches! This method searches for treasures by genre.
     * Perfect for finding all yer favorite types of movie adventures!
     * 
     * @param genre The genre of movies ye be huntin' for
     * @return A list of movie treasures in the specified genre, me hearty!
     */
    public List<Movie> searchMoviesByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            logger.warn("Arrr! Empty genre provided for search, returning empty treasure chest");
            return new ArrayList<>();
        }
        
        String searchGenre = genre.trim().toLowerCase();
        logger.info("Searching for movie treasures in genre: {}", searchGenre);
        
        return movies.stream()
                .filter(movie -> movie.getGenre().toLowerCase().contains(searchGenre))
                .collect(Collectors.toList());
    }

    /**
     * Shiver me timbers! This be the ultimate treasure hunting method!
     * Search by name, id, and genre all at once like a seasoned pirate captain!
     * 
     * @param movieName The name of the treasure (can be null or empty)
     * @param movieId The id of the specific treasure (can be null)
     * @param genre The genre of treasures ye be seekin' (can be null or empty)
     * @return A list of movie treasures that match yer search criteria, arrr!
     */
    public List<Movie> searchMovies(String movieName, Long movieId, String genre) {
        logger.info("Ahoy! Starting treasure hunt with name: '{}', id: {}, genre: '{}'", 
                   movieName, movieId, genre);
        
        List<Movie> treasureChest = new ArrayList<>(movies);
        
        // Filter by movie name if provided, arrr!
        if (movieName != null && !movieName.trim().isEmpty()) {
            String searchName = movieName.trim().toLowerCase();
            treasureChest = treasureChest.stream()
                    .filter(movie -> movie.getMovieName().toLowerCase().contains(searchName))
                    .collect(Collectors.toList());
            logger.debug("After name filter, found {} treasures", treasureChest.size());
        }
        
        // Filter by movie id if provided, me hearty!
        if (movieId != null && movieId > 0) {
            treasureChest = treasureChest.stream()
                    .filter(movie -> movie.getId() == movieId)
                    .collect(Collectors.toList());
            logger.debug("After id filter, found {} treasures", treasureChest.size());
        }
        
        // Filter by genre if provided, ye scallywag!
        if (genre != null && !genre.trim().isEmpty()) {
            String searchGenre = genre.trim().toLowerCase();
            treasureChest = treasureChest.stream()
                    .filter(movie -> movie.getGenre().toLowerCase().contains(searchGenre))
                    .collect(Collectors.toList());
            logger.debug("After genre filter, found {} treasures", treasureChest.size());
        }
        
        logger.info("Treasure hunt complete! Found {} movie treasures matching yer criteria", treasureChest.size());
        return treasureChest;
    }
}
