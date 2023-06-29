package com.javaunit3.springmvc;


import com.javaunit3.springmvc.model.MovieEntity;
import com.javaunit3.springmvc.model.VoteEntity;
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
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        List<MovieEntity> movieEntityList = session.createQuery("from MovieEntity").list();
        movieEntityList.sort(Comparator.comparing(movieEntity -> movieEntityList.size()));

        MovieEntity movieWithMostVotes = movieEntityList.get(movieEntityList.size()-1);
        List<String> voterNames = new ArrayList<>();

        for(VoteEntity vote: movieWithMostVotes.getVotes()){
            voterNames.add(vote.getVoterName());
        }

        String voterNamesList = String.join(",", voterNames);

        model.addAttribute("bestMovieVoters", voterNamesList);

        model.addAttribute("BestMovie", BestMovieService.getBestMovie().getTitle());

        session.getTransaction().commit();
        return "bestMovie";
    }

//    @RequestMapping("/voteForBestMovieForm")
//    public String voteForBestMovieFormPage() {
//        return "voteForBestMovie";
//    }

    @RequestMapping("/voteForBestMovie")
    public String voteForBestMovie(HttpServletRequest request, Model model) {
        String movieId = request.getParameter("movieId");
        String voterName = request.getParameter("voterName");
        String movieTitle = request.getParameter("movieTitle");

        Session session = sessionFactory.getCurrentSession();

        MovieEntity movieEntity = (MovieEntity) session.get(MovieEntity.class, Integer.parseInt(movieId));
        VoteEntity newVote = new VoteEntity();
        newVote.setVoterName(voterName);
        movieEntity.addVote(newVote);

        model.addAttribute("BestMovieVote", movieTitle);

        return "voteForBestMovie";
    }

    @RequestMapping("voteForBestMovieForm")
    public String voteForBestMovieFormPage(Model model){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<MovieEntity> movieEntityList = session.createQuery("from MovieEntity").list();
        session.getTransaction().commit();
        model.addAttribute("movies", movieEntityList);
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

