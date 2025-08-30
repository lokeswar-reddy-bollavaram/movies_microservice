package com.lokisoftware.tmdb.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lokisoftware.tmdb.model.Movie;
import com.lokisoftware.tmdb.repo.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerIntTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MovieRepository movieRepository;

    @BeforeEach
    void cleanUp(){
        movieRepository.deleteAllInBatch();
    }

    @Test
    void givenMovie_whenCreateMovie_thenReturnSavedMovie() throws Exception{
        Movie movie = new Movie();
        movie.setDirector("Rajamouli");
        movie.setActors(List.of("ntr", "ramcharam", "aliabut"));
        movie.setName("rrr");

        //when create movie
        var response = mockMvc.perform(post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)));

        //then verify movie created
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is(movie.getName())))
                .andExpect(jsonPath("director", is(movie.getDirector())))
                .andExpect(jsonPath("$.actors", is(movie.getActors())));
    }

    @Test
    void givenMovieId_whenFetchMovie_thenReturnMovie() throws Exception{
        Movie movie = new Movie();
        movie.setDirector("Rajamouli");
        movie.setActors(List.of("ntr", "ramcharam", "aliabut"));
        movie.setName("rrr");

        Movie savedMovie = movieRepository.save(movie);

        var response = mockMvc.perform(get("/movies/"+savedMovie.getId()));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedMovie.getId().intValue())))
                .andExpect(jsonPath("$.name", is(movie.getName())))
                .andExpect(jsonPath("director", is(movie.getDirector())))
                .andExpect(jsonPath("$.actors", is(movie.getActors())));
    }

    @Test
    void givenSavedMovie_whenUpdatedMovie_thenMovieUpdateInDb() throws Exception{
        Movie movie = new Movie();
        movie.setDirector("Rajamouli");
        movie.setActors(List.of("ntr", "ramcharam", "aliabut"));
        movie.setName("rrr");

        Movie savedMovie = movieRepository.save(movie);
        Long id = savedMovie.getId();

        movie.setActors(List.of("ntr", "ramcharam", "aliabut", "vijay"));

        var response = mockMvc.perform(put("/movies/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)));

        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        var fetchresponse = mockMvc.perform(get("/movies/"+savedMovie.getId()));

        fetchresponse.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(movie.getName())))
                .andExpect(jsonPath("director", is(movie.getDirector())))
                .andExpect(jsonPath("$.actors", is(movie.getActors())));
    }

    @Test
    void givenMovieId_whenDeleteMovie_thenMovieRemovedFromDb() throws Exception{

        Movie movie = new Movie();
        movie.setDirector("Rajamouli");
        movie.setActors(List.of("ntr", "ramcharam", "aliabut"));
        movie.setName("rrr");

        Movie savedMovie = movieRepository.save(movie);
        Long id = savedMovie.getId();

        mockMvc.perform(delete("/movies/"+id)).andExpect(status().isOk());

        assertFalse(movieRepository.existsById(id));
    }
}