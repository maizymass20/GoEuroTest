/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package goeurotest;

// import packages 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author maizymass
 */
public class GoEuroTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Api URI default if not args provided
        String URI = "http://api.goeuro.com/api/v2/position/suggest/en/karachi";
        if (args.length > 0) { // if provide command line arguments 
            if (args[0] != null) { // if the argument 0 index and has not null concat the string with uri
                URI = "http://api.goeuro.com/api/v2/position/suggest/en/" + args[0];
            }
        }

        try {
            URL url = new URL(URI); // Locate the file on the domain identifying the protocol
            try {
                // Open the conectoin using HttpURLConnection
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET"); // Set the Method of request
                connection.setRequestProperty("Accept", "application/json");// Set the Application responce type to accept data
                InputStream is = (InputStream) connection.getContent(); // Get the content of the connection and cast into InputStream
                JSONArray jo = GetJSON(is);
                for (int i = 0; i < jo.length(); i++) {
                    JSONObject jsonobject = jo.getJSONObject(i); // Get date from JSONArray and set to JSONObject
                    ExportCSV(jsonobject); // write and append the data on data.csv file
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage()); // Just Print the Error Or Exceptional Message
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());  // Just Print the Error Or Exceptional Message
        }

    }

    /**
     * Name : GetJSON
     *
     * @param InputStream is get the input stream object as a pram
     * @return JSONArray
     */
    private static JSONArray GetJSON(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JSONArray jo = null;
        try {
            jo = new JSONArray(sb.toString());

        } catch (JSONException ex) {
            System.out.println(ex.getMessage());
        }
        return jo;
    }

    /**
     * Name : ExportCSV
     *
     * @param Data JSONObject Get JSONObject as a pram and get keys then Append
     * to file
     * @throws IOException // throw exception if files is not write or something
     * else
     * @throws JSONException
     */
    private static void ExportCSV(JSONObject Data) {
        try {
            String ID = Data.get("_id").toString();
            String Name = Data.getString("name");
            String Type = Data.getString("type");
            String Latitude = Data.getJSONObject("geo_position").get("latitude").toString();
            String Longitude = Data.getJSONObject("geo_position").get("longitude").toString();
            File yourFile = new File("data.csv");
            FileWriter fw;
            try {
                fw = new FileWriter(yourFile, true);
                fw.append(ID + ",");
                fw.append(Name + ",");
                fw.append(Type + ",");
                fw.append(Latitude + ",");
                fw.append(Longitude + ",\n");
                fw.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage()); // show the error message on input output exception
            }
        } catch (JSONException ex) {
            System.out.println(ex.getMessage()); // show the error message on input output exception
        }

    }
}
