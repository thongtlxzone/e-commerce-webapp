import { OrderDetail } from "src/app/models/order.detail";

export interface OrderResponses {
    id: number;
    user_id: number;
    fullname: string;
    phone_number: string;
    email: string;
    address: string;
    note: string;
    order_date: Date;
    status: string;
    total_money: number;
    shipping_method: string;
    shipping_address: string;
    shipping_date: Date;
    payment_method: string;
    order_details: OrderDetail[];
}