import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Cuisines } from '../models';
import { RestaurantService } from '../restaurant-service'; 

@Component({
  selector: 'app-cuisine-list',
  templateUrl: './cuisine-list.component.html',
  styleUrls: ['./cuisine-list.component.css']
})
export class CuisineListComponent implements OnInit {

  cuisines: Cuisines[] = []

  constructor(private rSvc: RestaurantService, private activatedRoute: ActivatedRoute,) { }
	// TODO Task 2
	// For View 1

  ngOnInit(): void {
    this.rSvc.getCuisineList()
    .then(result => {
      for(var rs of result){
        this.cuisines.push(rs as Cuisines)
      }
    })
  }

}
