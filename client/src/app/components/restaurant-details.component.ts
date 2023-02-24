import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Restaurant } from '../models';
import { RestaurantService } from '../restaurant-service';

@Component({
  selector: 'app-restaurant-details',
  templateUrl: './restaurant-details.component.html',
  styleUrls: ['./restaurant-details.component.css']
})
export class RestaurantDetailsComponent {
	
  name!: string
  restaurantId!: string
  form!: FormGroup
  restaurant!: Restaurant
	// TODO Task 4 and Task 5
	// For View 3

  constructor(private fb: FormBuilder,private rSvc: RestaurantService, private activatedRoute: ActivatedRoute,private router: Router) { }

  ngOnInit(): void {
    this.name = this.activatedRoute.snapshot.params["name"]
    this.restaurantId = this.activatedRoute.snapshot.params["restaurant_id"]
    this.rSvc.getRestaurant(this.name)
    .then(result => {
        this.restaurant = result
        console.info('result: ', this.restaurant)
      })
      .catch(error => {
        console.error('>>> error: ', error)
      })
    this.form = this.createForm()
  }

  createForm(){
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required]),
      rating: this.fb.control<number>(5, [Validators.email, Validators.required]),
      restaurantId: this.fb.control<string>('', [Validators.required]),
      text: this.fb.control<string>('', [Validators.required])
    })
  }

  postComments(){
    const c = this.form.value as Comment
    console.info("Comment: " ,  c)
    this.rSvc.postComment(c)
    .then(res=>{
      this.router.navigate(['/'])
    })
  }

}
