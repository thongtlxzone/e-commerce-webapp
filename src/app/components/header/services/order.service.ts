import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { enviroment } from "../../../enviroments/enviroment";
import { Category } from "../../../models/category";
import { OrderDTO } from "../../../dtos/order/order.dto";

@Injectable({
    providedIn: 'root'
})
export class OrderService{
    private apiUrl = `${enviroment.apiBaseUrl}/orders`;
    
    constructor(private http: HttpClient){}

    placeOrder(orderData: OrderDTO): Observable<any> {
        return this.http.post(this.apiUrl, orderData);
    }

    getOrderById(orderId: number): Observable<any> {
        return this.http.get(this.apiUrl+'/'+orderId);
    }
}