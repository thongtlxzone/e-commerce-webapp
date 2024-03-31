import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { enviroment } from "../enviroments/enviroment";
// import {LocalStorageService} from 'ngx-webstorage';
import { ProductService } from "./product.service";

@Injectable({
    providedIn: 'root'
})

export class CartService {
    private cart: Map<number, number> = new Map(); //Map de luu tru gio hang voi key: id product, value: so luong
    
    constructor(private productService: ProductService){
        //Lay data tu localStorage khi khoi tao service
        const storedCart = localStorage.getItem('cart');
        if(storedCart){
            this.cart = new Map(JSON.parse(storedCart));
        }
    }

    addToCart(productId:number, quantity: number = 1):void {
        debugger;
        if(this.cart.has(productId)){
            //Neu san pham da ton tai trong gio hang => + quantity
            this.cart.set(productId, this.cart.get(productId)! + quantity);
        } else {
            //Neu san pham chua co => add
            this.cart.set(productId, quantity);
        }
        //Luu vao localStorage sau khi update cart
        this.saveCartToLocalStorage();
    }

    getCart(): Map<number,number>{
        return this.cart;
    }

    private saveCartToLocalStorage(): void{
        debugger;
        localStorage.setItem('cart', JSON.stringify(Array.from(this.cart.entries())))
    }

    clearCart(): void{
        this.cart.clear();
        this.saveCartToLocalStorage();
    }
}