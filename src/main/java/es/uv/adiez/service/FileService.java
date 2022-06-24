package es.uv.adiez.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import es.uv.adiez.domain.File;

@Service
public class FileService {
	@Value("${enpoint.usersAPI}")
	private String usersURL;
	@Value("${enpoint.filesAPI}")
	private String filesURL;
	private Gson gson = new Gson();
	
	public File updateStatus(File file, File.Status status) {
		RestTemplate restTemplate = new RestTemplate();
	     
	    final String baseUrl = "http://"+filesURL+"/files";
	    URI uri;
		try {
			uri = new URI(baseUrl);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new File();
		}
	    file.setStatus(status);
	    ResponseEntity<String> result = restTemplate.postForEntity(uri, file, String.class);
	     
	    //Verify request succeed
	   if(result.getStatusCodeValue() == 201) {
		   return gson.fromJson(result.getBody(), File.class);
	   }
	   return new File();
	}
}
