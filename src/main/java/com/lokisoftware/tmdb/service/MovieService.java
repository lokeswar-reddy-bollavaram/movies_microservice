package com.lokisoftware.tmdb.service;

import com.lokisoftware.tmdb.model.Movie;
import com.lokisoftware.tmdb.repo.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    // CRUD = create, read, update, delete

    public Movie create(Movie movie){

        if(movie == null){
            throw new RuntimeException("Invalid Movie");
        }
        return movieRepository.save(movie);

    }

    public Movie read(Long id){

        return movieRepository.findById(id).orElseThrow(()-> new RuntimeException("Movie not found"));
    }

    public void update(Long id, Movie umovie){
        if(umovie == null || id == null){
            throw new RuntimeException("Invalid Movie");
        }
        if (movieRepository.existsById(id)){
            Movie movie = movieRepository.getReferenceById(id);
            movie.setActors(umovie.getActors());
            movie.setName(umovie.getName());
            movie.setDirector(umovie.getDirector());
            movieRepository.save(movie);
        }else{
            throw  new RuntimeException("Movie not found!!");
        }
    }

    public void delete(Long id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
        } else {
            throw new RuntimeException("Movie not found!!");
        }
    }
}
