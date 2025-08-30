package com.lokisoftware.tmdb.repo;

import com.lokisoftware.tmdb.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
