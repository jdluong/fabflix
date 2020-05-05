package com.fabflix.fabflix;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class RecaptchaService {
    public static final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    public static final String SECRET_KEY = "6LdCRfEUAAAAAIiuFG-NB_j4mEbduOVx9mUaHBMj";

    public static boolean verify(String recaptcha) throws Exception {
        // create verify URL and establish connection
        URL verifyURL = new URL(SITE_VERIFY_URL);
        HttpsURLConnection connection = (HttpsURLConnection) verifyURL.openConnection();

        // set request method to post and create data to send
        connection.setRequestMethod("POST");
        String postParams = "secret=" + SECRET_KEY + "&response=" + recaptcha;

        // send request
        connection.setDoOutput(true);

        // get output stream of connection, write the params created to it
        OutputStream out = connection.getOutputStream();
        out.write(postParams.getBytes());

        // clean and close the out stream
        out.flush();
        out.close();

        // get response code from server
        int responseCode = connection.getResponseCode();
        System.out.println("responseCode=" + responseCode);

        // get input stream f connection, read data received
        InputStream in = connection.getInputStream();
        InputStreamReader inStreamReader = new InputStreamReader(in);

        // render json from server data and close instream reader
        JsonObject json = new Gson().fromJson(inStreamReader, JsonObject.class);
        inStreamReader.close();

        // return whether successful
        return json.get("success").getAsBoolean();
    }

}
