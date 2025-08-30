package com.lokisoftware.tmdb.api;

import com.lokisoftware.tmdb.model.Movie;
import com.lokisoftware.tmdb.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
@Slf4j
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable Long id){
        Movie movie = movieService.read(id);
        log.info("Returned movie with id: {}", id);
        return ResponseEntity.ok(movie);
    }

    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie){
        Movie createmovie = movieService.create(movie);
        log.info("Created movie with id: {}", createmovie.getId());
        return ResponseEntity.ok(createmovie);
    }

    @PutMapping("/{id}")
    public void udpateMovie(@PathVariable Long id, @RequestBody Movie movie){
        movieService.update(id, movie);
        log.info("Updated movie with id: {}", id);
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id){
        movieService.delete(id);
        log.info("Deleted movie with id: {}", id);

    }
}
