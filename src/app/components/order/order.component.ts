import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { OrderDTO } from 'src/app/dtos/order/order.dto';
import { enviroment } from 'src/app/enviroments/enviroment';
import { Product } from 'src/app/models/product';
import { CartService } from 'src/app/components/header/services/cart.service';
import { OrderService } from 'src/app/components/header/services/order.service';
import { ProductService } from 'src/app/components/header/services/product.service';
import { TokenService } from 'src/app/components/header/services/token.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit {
  orderForm: FormGroup;
  cartItems: { product: Product, quantity: number }[] = [];
  couponcode: string = '';
  totalAmount: number = 0;
  orderData: OrderDTO = {
    user_id: 1,
    fullname: '',
    email: '',
    phone_number: '',
    address: '',
    note: '',
    total_money: 0,
    payment_method: 'cod',
    shipping_method: 'express',
    coupon_code: '',
    cart_items: []
  }

  constructor(
    private router: Router,
    private cartService: CartService,
    private productService: ProductService,
    private orderService: OrderService,
    private fb: FormBuilder,
    private tokenService: TokenService
  ){
    this.orderForm = this.fb.group({
      fullname: ['Xuan Long', Validators.required],
      email: ['xuanlong@gmail.com', [Validators.email]],
      phone_number: ['03221234221',[Validators.required, Validators.minLength(6)]],
      address: ['Cantavil,Q2,TPHCM', [Validators.required, Validators.minLength(5)]],
      note: ['none'],
      shipping_method: ['express'],
      payment_method: ['cod']
    })
  }

  ngOnInit(): void{
    debugger;
    this.orderData.user_id = this.tokenService.getUserId();
    debugger;
    const cart = this.cartService.getCart();
    const productIds = Array.from(cart.keys());
    debugger;
    if(productIds.length === 0){
      return;
    }
    this.productService.getProductsByIds(productIds).subscribe({
      next:(products) => {
        debugger;
        this.cartItems = productIds.map((productId) => {
          debugger;
          const product = products.find((p) => p.id === productId);
          if(product){
            product.thumbnail = `${enviroment.apiBaseUrl}/products/images/${product.thumbnail}`;
          }
          return{
            product: product!,
            quantity: cart.get(productId)!
          }
        });
        console.log('haha');
      },
      complete: () => {
        debugger;
        this.caculateTotal();
      },
      error: (error: any) =>{
        debugger;
        console.error("Error fetching detail", error);
      }
    })
  }

  placeOrder() {
    debugger
    if(this.orderForm.valid){
      //Sử dụng toán tử Spread (...) để sao chép giá trị từ form vào orderData
      this.orderData = {
        ...this.orderData,
        ...this.orderForm.value
      };
      this.orderData.cart_items = this.cartItems.map(cartItem => ({
        product_id: cartItem.product.id,
        quantity: cartItem.quantity
      }));
      this.orderData.total_money = this.totalAmount;
      this.orderService.placeOrder(this.orderData).subscribe({
        next: (response)=>{
          debugger;
          alert('Đặt hàng thành công');
          this.cartService.clearCart();
          this.router.navigate(['/']);
        },
        complete: () => {
          debugger;
          this.caculateTotal();
        },
        error: (error: any) => {
          debugger;
          alert(`Lỗi khi đặt hàng ${error}`);
        }
      })
    }else{
      alert('Dữ liệu không hợp lệ, vui lòng kiểm tra lại.')
    }
  }

  caculateTotal(): void{
    this.totalAmount = this.cartItems.reduce(
      (total, item) => total + item.product.price * item.quantity,
      0
    );
  }
}