import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { enviroment } from "../../../enviroments/enviroment";
import { Product } from "../../../models/product";

@Injectable({
    providedIn: 'root'
})
export class ProductService {
    private apiGetProducts = `${enviroment.apiBaseUrl}/products`;
    constructor(private http: HttpClient){}
    getProducts(keyword: string, selectedCategoryId: number, page: number, limit: number): Observable<Product[]> {
        const params = new HttpParams()
            .set('keyword', keyword)
            .set('category_id', selectedCategoryId.toString())
            .set('page', page.toString())
            .set('limit', limit.toString());
        return this.http.get<Product[]>(this.apiGetProducts, { params })
    }
    getDetailProduct(productId: number){
        return this.http.get(`${enviroment.apiBaseUrl}/products/${productId}`);
    }
    getProductsByIds(productIds: number[]): Observable<Product[]>{
        debugger;
        const params = new HttpParams().set('ids', productIds.join(','));
        return this.http.get<Product[]>(`${this.apiGetProducts}/by-ids`, { params });
    }
}