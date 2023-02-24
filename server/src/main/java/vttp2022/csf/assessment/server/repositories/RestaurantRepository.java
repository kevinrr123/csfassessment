package vttp2022.csf.assessment.server.repositories;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import vttp2022.csf.assessment.server.models.Comment;
import vttp2022.csf.assessment.server.models.Restaurant;


@Repository
public class RestaurantRepository {

	@Autowired
	MongoTemplate mTemplate;

	// TODO Task 2
	// Use this method to retrive a list of cuisines from the restaurant collection
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method
	//  
	public List<String> getCuisines() {
		// Implmementation in here

		// db.restaurants.distinct("cuisine")
		List<String> cuisines = mTemplate.findDistinct(new Query(), "cuisine", "restaurants", String.class);
		return cuisines;
	}

	// TODO Task 3
	// Use this method to retrive a all restaurants for a particular cuisine
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	// Write the Mongo native query above for this method
	//  
	public List<Restaurant> getRestaurantsByCuisine(String cuisine) {
		// Implmementation in here

		// db.restaurants.find({"cuisine": "Afghan"}, { name: 1, _id: 0 })		
		Query query = Query.query(Criteria.where("cuisine").is(cuisine));
		query.fields().exclude("_id").include("name");
		return mTemplate.find(query, Document.class, "restuarants")
				.stream().map(d-> create(d)).toList();
		//List<String> restaurants = results.getList("comments", Document.class).stream();
	}

	// TODO Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any) 
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// Write the Mongo native query above for this method
	//  
	public Optional<Restaurant> getRestaurant(String name) {
		// Implmementation in here

		// db.restaurant.aggregrate([{$match:{name: "name"}}, {$project:{_id:-1,
		// restaurant_id:1, name:1, cuisine:1, address:1, address.coord:1}}])
		/* db.restaurants.aggregate([
		{
			$match: { name: 'name' }
		},
		{
			$unwind: "$address"
		},
		{
			$group: {
			    "_id": null,
				address: {
					$push: "$address.building",
					$push: "$address.street",
					$push: "$address.zipcode",
					$push: "$borough"
				},
			}
		},
		{
            $project: { name: 1, comments: 1, _id:-1,
				restaurant_id:1,
				name:1,
				cuisine:1,
				address:1,
				"address.coord":1 }
        }
		])
		 */
		MatchOperation matchName = Aggregation.match(Criteria.where("name").is(name));
		ProjectionOperation projectFields = Aggregation
				.project("restaurant_id", "name", "cuisine", "address").and("address.coord").as("coordinates")
				.andExclude("_id");
		Aggregation pipeline = Aggregation.newAggregation(matchName, projectFields);
		Document doc = mTemplate.aggregate(pipeline, "restaurants", Document.class).iterator().next();
		Restaurant r = create(doc);
		return Optional.of(r);
	}

	// TODO Task 5
	// Use this method to insert a comment into the restaurant database
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	// Write the Mongo native query above for this method
	//  
	public void addComment(Comment comment) {
		// Implmementation in here
		//db.comments.insert({})
		Document doc = Document.parse(comment.toString());
		mTemplate.insert(doc, "comments");
	}
	
	// You may add other methods to this class
	public static Restaurant create(Document d) {
        Restaurant g = new Restaurant();
        g.setName(d.getString("name"));
		g.setAddress(d.getString("address"));
		g.setCuisine(d.getString("cuisine"));
		g.setRestaurantId(d.getString("restaurant_id"));
		return g;
    }
}
