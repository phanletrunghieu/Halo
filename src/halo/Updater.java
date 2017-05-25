/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package halo;

import com.json.parsers.JSONParser;
import com.json.parsers.JsonParserFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 *
 * @author Phan Hieu
 */
public class Updater {

    static String strURLUpdate = "http://democode.byethost16.com/Halo/latest-release";

    public static String CheckUpdate() throws MalformedURLException, IOException {
        URL url = new URL(strURLUpdate);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestProperty("Cookie", "__test=9c9db6c3df85690275645eaf416881a8");
        int responseCode = httpConn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String jsonString = "";

            //read from InputStream
            InputStream inputStream = httpConn.getInputStream();
            byte[] buffer = new byte[1];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                jsonString += new String(buffer);
            }
            inputStream.close();
            jsonString = jsonString.replaceAll("\\\\([\"/])", "$1");

            //Decode Json
            JsonParserFactory factory = JsonParserFactory.getInstance();
            JSONParser parser = factory.newJsonParser();
            Map jsonMap = parser.parseJson(jsonString);

            int newVersion = Integer.parseInt((String) jsonMap.get("version"));
            if (newVersion > Setting.GetVersion()) {
                return (String) jsonMap.get("url");
            }
        }
        return null;
    }

    public static int DownloadFile(String fileURL, String saveDir) throws MalformedURLException, IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestProperty("Cookie", "__test=9c9db6c3df85690275645eaf416881a8");
        int responseCode = httpConn.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");

            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10, disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
            }

            // opens input stream from the HTTP connection
            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = saveDir + File.separator + fileName;
            File saveFile = new File(saveFilePath);
            saveFile.createNewFile();

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFile);

            int bytesRead = -1;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            System.out.println("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        httpConn.disconnect();
        return responseCode;
    }
}
