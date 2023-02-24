import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, Routes } from '@angular/router'
import { AppComponent } from './app.component';
import { CuisineListComponent } from './components/cuisine-list.component';
import { RestaurantCuisineComponent } from './components/restaurant-cuisine.component';
import { RestaurantDetailsComponent } from './components/restaurant-details.component';
import { RestaurantService } from './restaurant-service';

const appRoutes: Routes = [
  { path: '', component: CuisineListComponent },
  { path: 'restaurants/:cuisine', component: RestaurantCuisineComponent},
  { path: 'details/:restaurantId', component: RestaurantDetailsComponent},
  { path: '**', redirectTo: '/', pathMatch: 'full'},
  
]

@NgModule({
  declarations: [
    AppComponent,
    CuisineListComponent,
    RestaurantCuisineComponent,
    RestaurantDetailsComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    RouterModule.forRoot(appRoutes, {useHash: true})
  ],
  providers: [RestaurantService],
  bootstrap: [AppComponent]
})
export class AppModule { }
