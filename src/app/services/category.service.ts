import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { enviroment } from "../enviroments/enviroment";
import { Category } from "../models/category";

@Injectable({
    providedIn: 'root'
})
export class CategoryService{
    private apiBaseUrl = enviroment.apiBaseUrl;

    constructor(private http:HttpClient) {}

    getCategories(page: number, limit: number):Observable<Category[]> {
        const params = new HttpParams()
            .set('page', page.toString())
            .set('limit', limit.toString());
            return this.http.get<Category[]>(`${enviroment.apiBaseUrl}/categories`, { params });
    }
}