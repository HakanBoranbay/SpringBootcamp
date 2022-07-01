package com.hakanboranbay.moviewebservices;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Component
public class MoviesService implements IMoviesService {

	
	@Override	
	public List<Movie> titleSearch(String movieTitle) {
		RestTemplate template = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("authorization", "apikey 5rkkESHIxcoyNhRDqhGYZG:1DnQuHAsJSlTLddulP4LTy");
		headers.add("content-type", "application/json");
		String url = "https://api.collectapi.com/imdb/imdbSearchByName?query=" + movieTitle;
		
		HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, requestEntity, String.class);
		String resp = response.getBody();
		
		ObjectMapper objectMapper = new ObjectMapper();
		List<Movie> movies = new ArrayList<Movie>();
		
		try {
			JsonNode node = objectMapper.readTree(resp);
			JsonNode resultNode = node.get("result");
			if (resultNode.isArray()) {
				ArrayNode movieNode = (ArrayNode) resultNode;
				for (int i = 0; i < movieNode.size(); i++) {
					JsonNode singleMovie = movieNode.get(i);
					String title=singleMovie.get("Title").toString();
					String year=singleMovie.get("Year").toString();
					String imdbId=singleMovie.get("imdbID").toString();
					String type=singleMovie.get("Type").toString();
					Movie movieByTitle = new Movie();
					movieByTitle.setImdbID(imdbId);
					movieByTitle.setTitle(title);
					movieByTitle.setType(type);
					movieByTitle.setYear(year);
					movies.add(movieByTitle);
				}
			}
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Movie idSearch(String id) {
		RestTemplate template = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("authorization", "apikey 5rkkESHIxcoyNhRDqhGYZG:1DnQuHAsJSlTLddulP4LTy");
		headers.add("content-type", "application/json");
		String url = "https://api.collectapi.com/imdb/imdbSearchByName?query=" + id;
		
		HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, requestEntity, String.class);
		String resp = response.getBody();
		
		ObjectMapper objectMapper = new ObjectMapper();
		Movie movieById = new Movie();
		
		try {
			JsonNode node = objectMapper.readTree(resp);
			JsonNode resultNode = node.get("result");
			String title=resultNode.get("Title").toString();
			String year=resultNode.get("Year").toString();
			String imdbId=resultNode.get("imdbID").toString();
			String type=resultNode.get("Type").toString();
			movieById.setImdbID(imdbId);
			movieById.setTitle(title);
			movieById.setType(type);
			movieById.setYear(year);
			}
						
		catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movieById;
	}

	
	
}
