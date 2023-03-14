 package com.example.movieinfoservice.resources;
 
import com.example.movieinfoservice.models.Movie;
import com.example.movieinfoservice.models.MovieSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping("/movies")
public class MovieResource {

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    private MongoTemplate Template;
    private RestTemplate restTemplate;
    public MovieResource(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping("/{movieId}")
    public Movie getMovie(@PathVariable("movieId") String movieId) {
        Query q = new Query(Criteria.where("movieId").is(movieId));
        Movie cached = Template.findOne(q, Movie.class);
        if (cached != null) {
            // movie is in cache
            return new Movie(
                cached.getMovieId(),
                cached.getName(),
                cached.getDescription()
            );
        } else {
            final String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey;
            Optional<MovieSummary> movieSummary = Optional.ofNullable(restTemplate.getForObject(url, MovieSummary.class));
            if(movieSummary.isPresent()){
                Movie movieCache = new Movie(
                        movieSummary.get().getId(),
                        movieSummary.get().getTitle(),
                        movieSummary.get().getOverview()
                );
                // save movie in cache
                Template.save(movieCache);
                // return movie
                return new Movie(movieId, movieSummary.get().getTitle(), movieSummary.get().getOverview());
            }
        }
        return null;
    }
    
}
