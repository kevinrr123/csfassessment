import { Injectable } from '@angular/core'
import { HttpClient } from "@angular/common/http";
import { firstValueFrom, map } from "rxjs";
import { Restaurant, Comment, Cuisines } from './models'

@Injectable()
export class RestaurantService {

	constructor(private http: HttpClient) { }

	// TODO Task 2 
	// Use the following method to get a list of cuisines
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public getCuisineList(): Promise<Cuisines[]> {
		// Implememntation in here
		console.info("getcu:")
		return firstValueFrom(
			this.http.get<any>(`/api/cuisines`)
			.pipe(
			  map(result => {
				  const data = JSON.parse(result.data)
				  return data.map((v: any) => v as Cuisines)
			  })
		  )
		  )
	}

	// TODO Task 3 
	// Use the following method to get a list of restaurants by cuisine
	// You can add any parameters (if any) and the return type 
	// DO NOT CHNAGE THE METHOD'S NAME
	public getRestaurantsByCuisine(cuisine: string): Promise<Restaurant[]> {
		// Implememntation in here
		console.info("getre:")
    	return firstValueFrom(
      	this.http.get<any>(`/api/${cuisine}/restaurants`)
      	.pipe(
        map(result => {
            const data = JSON.parse(result.name)
            return data.map((v: any) => v as Restaurant)
        })
    	)
    	)
  	}
	
	
	// TODO Task 4
	// Use this method to find a specific restaurant
	// You can add any parameters (if any) 
	// DO NOT CHNAGE THE METHOD'S NAME OR THE RETURN TYPE
	public getRestaurant(name:string): Promise<Restaurant> {
		// Implememntation in here
		console.info("getName:")
    	return firstValueFrom(
      	this.http.get<any>(`/api/${name}`)
      	.pipe(
        map(result => {
            const data = JSON.parse(result.data)
            return data.map((v: any) => v as Restaurant)
        })
    	)
    	)
	}

	// TODO Task 5
	// Use this method to submit a comment
	// DO NOT CHANGE THE METHOD'S NAME OR SIGNATURE
	public postComment(comment: Comment): Promise<any> {
		// Implememntation in here
		console.log("comments")
		return firstValueFrom(
		this.http.post<any>('/api/comments', comment)
		).then(res => {
		console.info("res", res)
		})
	}
}
