package vttp2022.csf.assessment.server.controllers;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.Response;
import vttp2022.csf.assessment.server.models.Restaurant;
import vttp2022.csf.assessment.server.services.RestaurantService;

@RestController
@RequestMapping("/api")
public class RestaurantRestController {

    @Autowired
    RestaurantService rSvc;

    @GetMapping("/cuisines")
    public ResponseEntity<String> getCuisines() {
        List<String> cus = new LinkedList<>();
        try {
            cus = rSvc.getCuisines();
        } catch (Exception e) {
            JsonObject errJson = Json
                                    .createObjectBuilder()
                                    .add("error", "Cannot get")
                                    .build();
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errJson.toString());
        }
        JsonArrayBuilder ab = Json.createArrayBuilder();
        cus.stream().map(c -> ab.add(c));
        JsonObject resp = Json
							.createObjectBuilder()
							.add("data", ab.toString())
							.build();
        return ResponseEntity.ok(resp.toString());
    }

    @GetMapping("/{cuisine}/restaurants")
    public ResponseEntity<String> getRestaurants(@PathVariable String cuisine) {
        List<Restaurant> res = new LinkedList<>();
        StringBuilder dc = new StringBuilder();
        try {
            res = rSvc.getRestaurantsByCuisine(cuisine);
        } catch (Exception e) {
            JsonObject errJson = Json
                                    .createObjectBuilder()
                                    .add("error", "Cannot get")
                                    .build();
            return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errJson.toString());
        }
        for (Restaurant re : res) {
            dc.append(re);
        }
        JsonObject resp = Json
							.createObjectBuilder()
							.add("name", dc.toString())
							.build();
        return ResponseEntity.ok(resp.toString());
    }

    @GetMapping("/{name}")
    public ResponseEntity<String> getRestaurant(@PathVariable String name) {
        
        Optional<Restaurant> opt = rSvc.getRestaurant(name);

        if(opt.isEmpty()){
			JsonObject j = Json.createObjectBuilder().add("error", "Customer " + name + " not found").build();
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(j.toString());
        }

        Restaurant res = opt.get();
        JsonObject jObj = Json.createObjectBuilder()
                    .add("restaurant_id", res.getRestaurantId())
                    .add("name", res.getName())
                    .add("cuisine", res.getCuisine())
                    .add("address", res.getAddress())
                    .add("coordinates", Json.createArrayBuilder().add(res.getCoordinates().getLatitude())
                                            .add(res.getCoordinates().getLongitude()))
                    .build();
        JsonObject resp = Json
							.createObjectBuilder()
							.add("data", jObj.toString())
							.build();
        return ResponseEntity.ok(resp.toString());
    }

    @PostMapping(path = "/comments")
    public ResponseEntity<String> postComment(@RequestBody String payload) {
            JsonReader jReader = Json.createReader(new StringReader(payload));
		    JsonObject jsonObj = jReader.readObject();
            Comment c = new Comment();
            c.setName(jsonObj.getString("name"));
            c.setRating(jsonObj.getInt("rating"));
            c.setText(jsonObj.getString("text"));
            c.setRestaurantId(jsonObj.getString("restaurant_id"));
            Response resp = new Response();
            try {
                rSvc.addComment(c);
            } catch (Exception e){
                resp.setCode(400);
                resp.setMessage("Unable to post comments.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(resp.toJson().toString());
            }
            
            resp.setCode(201);
            resp.setMessage("Comment posted");
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(resp.toJson().toString());
    }
       
}
