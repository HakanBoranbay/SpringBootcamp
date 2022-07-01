package com.hakanboranbay.moviewebservices;

import java.util.List;

public interface IMoviesService {

	public List<Movie> titleSearch(String movieTitle);
	
	public Movie idSearch(String id);
}
