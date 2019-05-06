package com.gdrive.api.requestor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class WebServiceRequestor {

	private final int TIMEOUT = 10000;

	public String requestFolderContent(String URL, String authorization) {
		String jsonResponse = "";
		JsonElement finalResponse = null;
		JsonParser jsonParser = new JsonParser();
		String errorResponse = "{\"error\":\"There is an error while making REST client\"}";
		int responseCode = 0;
		try {

			CloseableHttpResponse response = null;
			CloseableHttpClient httpClient = null;
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(TIMEOUT)
					.setSocketTimeout(TIMEOUT)
					.setConnectionRequestTimeout(TIMEOUT).build();

			httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();

			HttpGet httpGet = new HttpGet(URL);

			httpGet.addHeader("Authorization", authorization);
			httpGet.addHeader("Accept", "application/json");

			response = httpClient.execute(httpGet);
			responseCode = response.getStatusLine().getStatusCode();

			HttpEntity entity = response.getEntity();
			jsonResponse = EntityUtils.toString(entity);


		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(responseCode==200) {
			finalResponse = jsonParser.parse(jsonResponse);
			return jsonResponse;
		} else {
			return jsonResponse;
		}
	}

	public String downloadFile(String URL, String authorization, String filePath) {
		String finalResponse = null;
		String errorResponse = "{\"error\":\"There is an error while making REST client. FILE NOT FOUND\"}";
		int responseCode = 0;
		String fileName = filePath.substring(filePath.lastIndexOf("/")+1, filePath.length());
		File file = new File(fileName);
		FileOutputStream fileOutputStream = null;
		HttpEntity entity = null;
		try {

			CloseableHttpResponse response = null;
			CloseableHttpClient httpClient = null;
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(TIMEOUT)
					.setSocketTimeout(TIMEOUT)
					.setConnectionRequestTimeout(TIMEOUT).build();

			httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();

			HttpGet httpGet = new HttpGet(URL);

			httpGet.addHeader("Authorization", authorization);
			//httpGet.addHeader("Accept", "application/json");

			response = httpClient.execute(httpGet);
			responseCode = response.getStatusLine().getStatusCode();

			entity = response.getEntity();

			if(entity != null) {
				fileOutputStream = new FileOutputStream(file);
				entity.writeTo(fileOutputStream);
			}

			if(responseCode==200) {

				finalResponse = "{\"status\": \"File Downloaded Successfully\", \"fileName\":\""+file+"\"}";

			} else {
				finalResponse = errorResponse;
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return finalResponse;
	}

	public String uploadFile(String URL, String authorization, String filePath) {

		File file = new File(filePath);
		int responseCode = 0;
		String jsonResponse = "";
		String errorResponse = "{\"error\":\"There is an error while making REST client\"}";

		try {

			CloseableHttpResponse response = null;
			CloseableHttpClient httpClient = null;
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectTimeout(TIMEOUT)
					.setSocketTimeout(TIMEOUT)
					.setConnectionRequestTimeout(TIMEOUT).build();

			httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();

			HttpPost httpPost = new HttpPost(URL);
			httpPost.addHeader("Authorization", authorization);
			httpPost.addHeader("Accept", "application/json");
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addPart("file", new FileBody(file));

			httpPost.setEntity(builder.build());


			response = httpClient.execute(httpPost);
			responseCode = response.getStatusLine().getStatusCode();
			
			HttpEntity entity = response.getEntity();
			jsonResponse = EntityUtils.toString(entity);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(responseCode==200) {
			return jsonResponse;
		} else {
			return jsonResponse;
		}

	}

}
