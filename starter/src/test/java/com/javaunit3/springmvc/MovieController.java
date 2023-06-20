package com.javaunit3.springmvc;


import com.javaunit3.springmvc.model.MovieEntity;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private BestMovieService bestMovieService;
    @Autowired
    private SessionFactory sessionFactory;

    @RequestMapping("/")
    public String getIndexPage() {
        return "index";
    }
    @RequestMapping("/bestMovie")
    public String getBestMoviePage(Model model) {
        model.addAttribute("BestMovie", BestMovieService.getBestMovie().getTitle());
        return "bestMovie";
    }

    @RequestMapping("/voteForBestMovieForm")
    public String voteForBestMovieFormPage() {
        return "voteForBestMovie";
    }

    @RequestMapping("/voteForBestMovie")
    public String voteForBestMovie(HttpServletRequest request, Model model) {

        String movieTitle = request.getParameter("movieTitle");

        model.addAttribute("BestMovieVote", movieTitle);

        return "voteForBestMovie";
    }

    @RequestMapping("/addMovieForm")
    public String addMovieForm() {
        return "addMovie";
    }

    @RequestMapping("/addMovie")
    public String addMovie(HttpServletRequest request) {
        String title = request.getParameter("title");
        String maturityRating = request.getParameter("maturityRating");
        String genre = request.getParameter("genre");



        MovieEntity  movieEntity = new MovieEntity();
        movieEntity.setTitle(title);
        movieEntity.setGenre(genre);
        movieEntity.setMaturityRating(maturityRating);

        //start a session with the factory made in hibernate config

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        session.save(movieEntity);

        session.getTransaction().commit();
        return "addMovie";
    }

}

