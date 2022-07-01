package com.hakanboranbay.moviewebservices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoviesController {
	
	@Autowired
	private IMoviesService service;

	@RequestMapping(path = "/movies/search", method = RequestMethod.GET)
	public List<Movie> search(@RequestParam(name = "movie_name") String name) {

		return this.service.titleSearch(name);
	}
	
	@RequestMapping(path = "/movies/addToWatchList", method = RequestMethod.GET)
	public boolean addToWatchList(@RequestParam(name = "id") String id) {
		Movie movieById = this.service.idSearch(id);
		String fileLine = movieById.getImdbID() + "," + movieById.getTitle() + "," + movieById.getType() + "," + movieById.getYear();
		try {
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("WatchList.txt")));
		writer.write(fileLine);
		writer.newLine();
		writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	@RequestMapping(path = "/movies/movieDetails", method = RequestMethod.GET)
	public Movie findById(@RequestParam(name = "id") String id) throws IOException {
		
		String imdbId = "\"" + id + "\"";
		
		try {
			try (BufferedReader reader = new BufferedReader(new FileReader("WatchList.txt"))) {
				String line = reader.readLine();
				while (line != null) {
					String[] details = line.split(",");
					if (details[0].equals(imdbId)) {
						Movie m = new Movie();
						m.setImdbID(details[0]);
						m.setTitle(details[1]);
						m.setType(details[2]);
						m.setYear(details[3]);
						return m;
					}
					else {
						line = reader.readLine();
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return this.service.idSearch(imdbId);
		
	}
}
