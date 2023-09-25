package com.meet.meet.controllers;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.meet.meet.storage.StorageService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class StorageContoller {

    @Autowired
    private StorageService storageService;
    
    @GetMapping("/uploads/**")
	public ResponseEntity<Resource> getFile(
		HttpServletRequest request
	) throws MalformedURLException {

		String fileName = new AntPathMatcher()
            .extractPathWithinPattern( "/{id}/**", request.getRequestURI());
		
	    Resource file = storageService.loadAsResource(fileName);

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	  }
}
