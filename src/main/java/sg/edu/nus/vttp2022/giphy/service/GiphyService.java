package sg.edu.nus.vttp2022.giphy.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class GiphyService {
    
    String GIPHY_SEARCH = "https://api.giphy.com/v1/gifs/search";

    // GIPHY_API_KEY
    @Value("${giphy.api.key}")
    private String giphyKey;

    public List<String> getGiphs(String q, Integer limit) {
        return getGiphs(q, "pg", limit);
    }

    public List<String> getGiphs(String q, String rating) {
        return getGiphs(q, rating, 10);
    }

    public List<String> getGiphs(String q) {
        return getGiphs(q, "pg", 10);
    }

    public List<String> getGiphs(String q, String rating, Integer limit) {
        List<String> result = new LinkedList<>();

        // i want the image for fixed_width url
        
        String url = UriComponentsBuilder.fromUriString(GIPHY_SEARCH)
                            .queryParam("api_key",giphyKey)
                            .queryParam("q",q)
                            .queryParam("limit",limit)
                            .queryParam("rating",rating)
                            .toUriString();

        RequestEntity<Void> req = RequestEntity
                            .get(url)
                            .accept(MediaType.APPLICATION_JSON)
                            .build();
        
        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = template.exchange(req,String.class);

        try (InputStream is = new ByteArrayInputStream(resp.getBody().getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject data = reader.readObject();

            JsonArray jsonArray = data.getJsonArray("data");
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject gif = jsonArray.getJsonObject(i);
                String image = gif.getJsonObject("images").getJsonObject("fixed_width").getString("url");
                result.add(image);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return result;
        }
        


        return result;
    }


}
