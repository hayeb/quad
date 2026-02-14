import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Category } from '../interface/category.interface';

@Injectable({
  providedIn: 'root',
})
export class CategoriesService {
  private readonly http = inject(HttpClient);

  public getCategories() {
    return this.http.get<Category[]>('http://localhost:8080/api/categories');
  }
}
