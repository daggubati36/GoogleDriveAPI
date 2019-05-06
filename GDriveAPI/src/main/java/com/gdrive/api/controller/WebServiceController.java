package com.gdrive.api.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gdrive.api.requestor.WebServiceRequestor;


@RestController
public class WebServiceController {

	//private final int TIMEOUT = 5000;
	@Value("${client.baseURL}")
	String baseURL;

	@Value("${client.authorization}")
	String authorization;

	WebServiceRequestor requestor = new WebServiceRequestor();


	/**
	 * 
	 * API to get files and folders in a folder
	 * 
	 * @param filePath
	 * @return
	 */
	@RequestMapping(value="getFolderContents", method=RequestMethod.GET)
	public String getFolderContents(@RequestParam("filePath") String filePath) {

		String URL = baseURL + "folders/contents?path=/"+filePath;
		URL = URL.replaceAll(" ", "%20");
		String response = requestor.requestFolderContent(URL, authorization);

		return response.toString();
	}
	
	/**
	 * API to download files from Folder
	 * @param filePath
	 * @return
	 */

	@RequestMapping(value="download", method=RequestMethod.GET)
	public String downloadFile(@RequestParam("filePath") String filePath) {
		
		String URL = baseURL + "files?path=/"+filePath;
		URL = URL.replaceAll(" ", "%20");
		
		String response = requestor.downloadFile(URL, authorization, filePath);
		
		return response;
	}
	
	@RequestMapping(value="upload", method=RequestMethod.GET)
	public String uploadFile(@RequestParam("filePath") String filePath, @RequestParam("gDrivePath") String gDrivePath) {
		String URL = baseURL + "files?path="+gDrivePath;
		URL = URL.replaceAll(" ", "%20");
		
		String response = requestor.uploadFile(URL, authorization, filePath);
		
		return response;
	}

}
