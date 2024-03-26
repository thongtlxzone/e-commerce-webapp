import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { enviroment } from 'src/app/enviroments/enviroment';
import { Product } from 'src/app/models/product';
import { ProductImage } from 'src/app/models/product.image';
import { CartService } from 'src/app/components/header/services/cart.service';
import { ProductService } from 'src/app/components/header/services/product.service';

@Component({
  selector: 'app-detail-product',
  templateUrl: './detail-product.component.html',
  styleUrls: ['./detail-product.component.scss']
})
export class DetailProductComponent implements OnInit{
  product?: Product;
  productId: number = 0;
  currentImageIndex: number = 0;
  quantity: number = 1;
  
  constructor(
    private router: Router, 
    private productService: ProductService,
    private cartService: CartService,
  ){}
  
  ngOnInit() {
    debugger;
    const idParam = 7; // fake data
    if(idParam !== null){
      this.productId = +idParam;
    }
    if(!isNaN(this.productId)) {
      this.productService.getDetailProduct(this.productId).subscribe({
        next: (response: any) => {
          debugger;
          if(response.product_images && response.product_images.length > 0) {
            response.product_images.forEach((product_image:ProductImage) => {
              product_image.image_url = `${enviroment.apiBaseUrl}/products/images/${product_image.image_url}`;
            });
          }
          debugger;
          this.product = response;
          this.showImage(0)
        },
        complete: () => {
          debugger;
        },
        error: (error: any) => {
          debugger;
          console.error('Error fetching detail: ', error);
        }
      });
    } else {
      console.error('Invalid productId', idParam);
    }
  }

  showImage(index: number):void{
    debugger;
    if(this.product && this.product.product_images && this.product.product_images.length > 0){
      if(index < 0){
        index = 0;
      }else if(index >= this.product.product_images.length){
        index = this.product.product_images.length - 1;
      }

      this.currentImageIndex = index;
    }
  }

  thumbnailClick(index: number){
    debugger;
    this.currentImageIndex = index;
  }

  nextImage():void{
    debugger;
    this.showImage(this.currentImageIndex + 1);
  }

  previousImage():void{
    debugger;
    this.showImage(this.currentImageIndex - 1);
  }
  addToCart():void{
    debugger;
    if(this.product){
      this.cartService.addToCart(this.productId,this.quantity)
    }else{
      console.error("product is null");
    }
  }

  increaseQuantity(): void {
    this.quantity++;
  }

  decreaseQuantity(): void {
    if(this.quantity > 1){
      this.quantity -- ;
    }
  }

  buyNow(){
    this.addToCart();
    this.router.navigate(['/orders']);
  }
}
