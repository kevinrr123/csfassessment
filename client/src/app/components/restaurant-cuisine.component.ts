import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Restaurant } from '../models';
import { RestaurantService } from '../restaurant-service';

@Component({
  selector: 'app-restaurant-cuisine',
  templateUrl: './restaurant-cuisine.component.html',
  styleUrls: ['./restaurant-cuisine.component.css']
})
export class RestaurantCuisineComponent implements OnInit{
	
	// TODO Task 3
	// For View 2

  cuisine!: string
  restaurants: Restaurant[] = []

  constructor(private rSvc: RestaurantService, private activatedRoute: ActivatedRoute,) { }

  ngOnInit(): void {
    this.cuisine = this.activatedRoute.snapshot.params["cuisine"]
    this.rSvc.getRestaurantsByCuisine(this.cuisine)
    .then(result => {
      for(var re of result){
        this.restaurants.push(re as Restaurant)
      }
    })
  }

}
