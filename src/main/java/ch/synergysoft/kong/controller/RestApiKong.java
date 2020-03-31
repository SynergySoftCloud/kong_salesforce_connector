package ch.synergysoft.kong.controller;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class RestApiKong {
	
	 	static final String USERNAME     = "filipp.berzenin@gmail.com";
	    static final String PASSWORD     = "German@2020_LrB52gfJ7R0qgNFPU8Bg4CDW";
	    static final String LOGINURL     = "https://login.salesforce.com";
	    static final String GRANTSERVICE = "/services/oauth2/token?grant_type=password";
	    static final String CLIENTID     = "3MVG95G8WxiwV5Pt3Ga7nAVcEj0Yzxt64iuOBPS2zaydmpZLD7CcUuSCvVXBAKAce5B_vqYoY4FKuvkakmnkK";
	    static final String CLIENTSECRET = "B218F9A8E887BE0D2363164CCD04F3105C30B060A6CEEBF68F1F09788C61A0CE";

	    public static void main(String[] args) {

	        HttpClient httpclient = HttpClientBuilder.create().build();

	        // Assemble the login request URL
	        String loginURL = LOGINURL +
	                          GRANTSERVICE +
	                          "&client_id=" + CLIENTID +
	                          "&client_secret=" + CLIENTSECRET +
	                          "&username=" + USERNAME +
	                          "&password=" + PASSWORD;

	        // Login requests must be POSTs
	        HttpPost httpPost = new HttpPost(loginURL);
	        HttpResponse response = null;

	        try {
	            // Execute the login POST request
	            response = httpclient.execute(httpPost);
	        } catch (ClientProtocolException cpException) {
	            cpException.printStackTrace();
	        } catch (IOException ioException) {
	            ioException.printStackTrace();
	        }

	        // verify response is HTTP OK
	        final int statusCode = response.getStatusLine().getStatusCode();
	        if (statusCode != HttpStatus.SC_OK) {
	            System.out.println("Error authenticating to Force.com: "+statusCode);
	            // Error is in EntityUtils.toString(response.getEntity())
	            return;
	        }

	        String getResult = null;
	        try {
	            getResult = EntityUtils.toString(response.getEntity());
	        } catch (IOException ioException) {
	            ioException.printStackTrace();
	        }
	        JSONObject jsonObject = null;
	        String loginAccessToken = null;
	        String loginInstanceUrl = null;
	        try {
	            jsonObject = (JSONObject) new JSONTokener(getResult).nextValue();
	            loginAccessToken = jsonObject.getString("access_token");
	            loginInstanceUrl = jsonObject.getString("instance_url");
	        } catch (JSONException jsonException) {
	            jsonException.printStackTrace();
	        }
	        System.out.println(response.getStatusLine());
	        System.out.println("Successful login");
	        System.out.println("  instance URL: "+loginInstanceUrl);
	        System.out.println("  access token/session ID: "+loginAccessToken);

	        // release connection
	        httpPost.releaseConnection();
	    }


}
