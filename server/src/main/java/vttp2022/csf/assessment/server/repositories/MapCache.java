package vttp2022.csf.assessment.server.repositories;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import vttp2022.csf.assessment.server.models.LatLng;

@Repository
public class MapCache {

	@Autowired
    private AmazonS3 s3Client;


	// TODO Task 4
	// Use this method to retrieve the map
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public byte[] getMap(LatLng coorLng) throws IOException {
		// Implmementation in here
		StringBuilder dc = new StringBuilder();
        dc.append(Float.toString(coorLng.getLatitude()));
        dc.append(Float.toString(coorLng.getLongitude()));
        String key = dc.toString();

		try {
			GetObjectRequest get = new GetObjectRequest("bigbucket", key);
			S3Object image = s3Client.getObject(get);
			S3ObjectInputStream is = image.getObjectContent(); 
			return is.readAllBytes();
		} catch (AmazonS3Exception ex) {

		String url = UriComponentsBuilder.fromUriString("http://map.chuklee.com/map")
				.queryParam("lat", Float.toString(coorLng.getLatitude()))
				.queryParam("lng", Float.toString(coorLng.getLongitude()))
				.toUriString();
		RequestEntity req = RequestEntity.get(url).accept(MediaType.IMAGE_PNG).build();
		RestTemplate template = new RestTemplate();
		ResponseEntity<byte[]> resp = template.exchange(req, byte[].class);
		InputStream is2 = new ByteArrayInputStream(resp.getBody());
		upload(is2, key);
		return is2.readAllBytes();
		}
		
	}

	// You may add other methods to this class
	public void upload(InputStream file, String key) {

		ObjectMetadata metadata = new ObjectMetadata();
        // Create a put request
        PutObjectRequest putReq = new PutObjectRequest(
            "bigbucket", // bucket name
            "myobjects/%s".formatted(key), //key
            file, //inputstream
            metadata);

        // Allow public access
        putReq.withCannedAcl(CannedAccessControlList.PublicRead);

        s3Client.putObject(putReq);
    }


}
