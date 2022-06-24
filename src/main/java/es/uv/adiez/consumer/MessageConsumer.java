package es.uv.adiez.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import es.uv.adiez.domain.File;
import es.uv.adiez.service.FileService;

@Service
public class MessageConsumer {
	@Autowired
	private FileService fs;
	
	public void consumeMessage(String message) {
		
        System.out.println("Received queue message: " + message);
        Gson gson = new Gson();
        File f = gson.fromJson(message, File.class);
        if(f.getStatus().equals(File.Status.error)) {
        	fs.updateStatus(f, File.Status.error);
        	System.out.println("Message processed with error! " + f.getId());
        }
        else { 
        	fs.updateStatus(f, File.Status.published);
        	System.out.println("Message processed OK! " + f.getId());
        }
        
       
    }
}

