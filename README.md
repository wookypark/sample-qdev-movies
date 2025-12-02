# Movie Service - Spring Boot Demo Application 🏴‍☠️

Ahoy matey! Welcome to the Pirate's Movie Treasure Chest - a swashbuckling movie catalog web application built with Spring Boot, demonstrating Java application development best practices with a pirate twist!

## Features ⚔️

- **Movie Catalog**: Browse 12 classic movie treasures with detailed information
- **Movie Details**: View comprehensive information including director, year, genre, duration, and description
- **🆕 Treasure Hunt Search**: Search for movie treasures by name, ID, or genre with our new pirate-themed search functionality!
- **Customer Reviews**: Each movie includes authentic customer reviews with ratings and avatars
- **Responsive Design**: Mobile-first design that works on all devices
- **Modern UI**: Dark theme with gradient backgrounds and smooth animations
- **🆕 REST API**: JSON endpoints for programmatic access to movie search functionality

## Technology Stack ⚓

- **Java 8**
- **Spring Boot 2.7.18**
- **Maven** for dependency management
- **Thymeleaf** for templating
- **Log4j 2.20.0**
- **JUnit 5.8.2**

## Quick Start 🚀

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Run the Application

```bash
git clone https://github.com/<youruser>/sample-qdev-movies.git
cd sample-qdev-movies
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access the Application

- **Movie List & Search**: http://localhost:8080/movies
- **Movie Details**: http://localhost:8080/movies/{id}/details (where {id} is 1-12)
- **🆕 Search Movies**: http://localhost:8080/movies/search?name=Prison&genre=Drama
- **🆕 API Search**: http://localhost:8080/api/movies/search?name=Adventure

## 🆕 Movie Search & Filtering API

Ahoy! Our new treasure hunting functionality allows ye to search for movie treasures in multiple ways:

### Web Interface Search

Navigate to `/movies` and use the pirate-themed search form to hunt for treasures:

- **Movie Name**: Search by partial or full movie name (case-insensitive)
- **Movie ID**: Find a specific treasure by its ID
- **Genre**: Search by genre (supports partial matches)

### REST API Endpoints

#### Search Movies (HTML Response)
```
GET /movies/search
```

**Query Parameters:**
- `name` (optional): Movie name to search for
- `id` (optional): Specific movie ID (must be positive)
- `genre` (optional): Genre to filter by

**Examples:**
```bash
# Search by name, arrr!
curl "http://localhost:8080/movies/search?name=Prison"

# Search by genre, me hearty!
curl "http://localhost:8080/movies/search?genre=Drama"

# Combined search, ye scallywag!
curl "http://localhost:8080/movies/search?name=The&genre=Crime"
```

#### Search Movies (JSON API)
```
GET /api/movies/search
```

**Query Parameters:** Same as above

**Response Format:**
```json
{
  "movies": [
    {
      "id": 1,
      "movieName": "The Prison Escape",
      "director": "John Director",
      "year": 1994,
      "genre": "Drama",
      "description": "Two imprisoned men bond...",
      "duration": 142,
      "imdbRating": 5.0,
      "icon": "🎬"
    }
  ],
  "totalCount": 1,
  "message": "Ahoy! Found 1 movie treasure!"
}
```

**Error Response:**
```json
{
  "error": "Arrr! Movie id must be a positive number, ye landlubber!",
  "timestamp": 1640995200000
}
```

### Search Features 🔍

- **Case-insensitive**: Search works regardless of capitalization
- **Partial matching**: Find movies with partial name or genre matches
- **Combined filters**: Use multiple search criteria together
- **Parameter validation**: Invalid IDs return helpful error messages
- **Empty result handling**: Friendly messages when no treasures are found
- **Parameter preservation**: Search form remembers your last search

## Building for Production 🏗️

```bash
mvn clean package
java -jar target/sample-qdev-movies-0.1.0.jar
```

## Project Structure 📁

```
src/
├── main/
│   ├── java/
│   │   └── com/amazonaws/samples/qdevmovies/
│   │       ├── movies/
│   │       │   ├── MoviesApplication.java    # Main Spring Boot application
│   │       │   ├── MoviesController.java     # REST controller with search endpoints
│   │       │   ├── MovieService.java         # Service with search methods
│   │       │   ├── Movie.java                # Movie data model
│   │       │   ├── Review.java               # Review data model
│   │       │   └── ReviewService.java        # Review service
│   │       └── utils/
│   │           ├── MovieIconUtils.java       # Movie icon utilities
│   │           └── MovieUtils.java           # Movie validation utilities
│   └── resources/
│       ├── application.yml                   # Application configuration
│       ├── movies.json                       # Movie data
│       ├── mock-reviews.json                 # Mock review data
│       ├── log4j2.xml                        # Logging configuration
│       └── templates/
│           ├── movies.html                   # Enhanced with search form
│           ├── movie-details.html            # Movie details page
│           └── error.html                    # Error page with pirate theme
└── test/                                     # Comprehensive unit tests
    └── java/
        └── com/amazonaws/samples/qdevmovies/movies/
            ├── MovieServiceTest.java         # Tests for search functionality
            └── MoviesControllerTest.java     # Tests for search endpoints
```

## API Endpoints 🗺️

### Get All Movies
```
GET /movies
```
Returns an HTML page displaying all movies with ratings, basic information, and the new search form.

### 🆕 Search Movies (Web Interface)
```
GET /movies/search?name={name}&id={id}&genre={genre}
```
Returns an HTML page with search results and the search form with preserved parameters.

### 🆕 Search Movies (JSON API)
```
GET /api/movies/search?name={name}&id={id}&genre={genre}
```
Returns JSON response with matching movies and metadata.

### Get Movie Details
```
GET /movies/{id}/details
```
Returns an HTML page with detailed movie information and customer reviews.

**Parameters:**
- `id` (path parameter): Movie ID (1-12)

## Testing 🧪

Run the comprehensive test suite:

```bash
# Run all tests, arrr!
mvn test

# Run specific test class
mvn test -Dtest=MovieServiceTest

# Run with coverage
mvn test jacoco:report
```

### Test Coverage

Our treasure chest includes comprehensive tests for:
- ✅ Movie search by name, ID, and genre
- ✅ Combined search functionality
- ✅ Edge cases and error handling
- ✅ Parameter validation
- ✅ Empty result scenarios
- ✅ Web interface search endpoints
- ✅ JSON API endpoints

## Troubleshooting 🔧

### Port 8080 already in use

Run on a different port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Build failures

Clean and rebuild:
```bash
mvn clean compile
```

### Search not working

Check the logs for pirate-themed error messages:
```bash
tail -f logs/application.log
```

## Contributing 🤝

This project is designed as a demonstration application. Feel free to:
- Add more movies to the treasure chest
- Enhance the UI/UX with more pirate themes
- ✅ Add new features like search or filtering (Already implemented, arrr!)
- Improve the responsive design
- Add more search criteria (rating, year, director)
- Implement advanced search features

## Pirate Language Guide 🏴‍☠️

Our application uses authentic pirate terminology:
- **Treasures** = Movies
- **Treasure Chest** = Movie collection
- **Treasure Hunt** = Movie search
- **Matey/Me hearty/Ye scallywag** = Friendly addresses
- **Arrr!** = Expression of excitement
- **Shiver me timbers!** = Expression of surprise
- **Batten down the hatches!** = Prepare for action

## License 📜

This sample code is licensed under the MIT-0 License. See the LICENSE file.

---

*Arrr! May fair winds fill yer sails as ye explore this treasure chest of movie adventures! 🏴‍☠️⚓*
