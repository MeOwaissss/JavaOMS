import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { Order, Delivery, Product } from '../../models/oms.models';

@Component({
  selector: 'app-admin-orders',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './admin-orders.component.html',
  styles: []
})
export class AdminOrdersComponent implements OnInit {
  private fb = inject(FormBuilder);
  private apiService = inject(ApiService);

  orders: Order[] = [];
  selectedOrder: Order | null = null;
  deliveryDetails: Delivery | null = null;

  // New Order properties
  showCreateOrder = false;
  customers: any[] = [];
  products: Product[] = [];
  
  newOrderForm: FormGroup = this.fb.group({
    customerId: ['', Validators.required]
  });
  
  cartItems: { product: Product, quantity: number }[] = [];
  selectedProductId: string = '';
  selectedQuantity: number = 1;

  deliveryForm: FormGroup = this.fb.group({
    deliveryPartner: ['', Validators.required],
    trackingNumber: ['', Validators.required],
    estimatedDelivery: ['', Validators.required]
  });

  statusList: string[] = ['PENDING', 'CONFIRMED', 'PACKED', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED'];

  ngOnInit(): void {
    this.loadOrders();
    this.apiService.getAllCustomers().subscribe(data => this.customers = data);
    this.apiService.getProducts().subscribe(data => this.products = data);
  }

  loadOrders(): void {
    this.apiService.getOrders().subscribe(data => this.orders = data);
  }

  onSelectOrder(order: Order): void {
    this.selectedOrder = order;
    this.apiService.getDelivery(order.id!).subscribe({
      next: (del) => {
        this.deliveryDetails = del;
        this.deliveryForm.patchValue({
          deliveryPartner: del.deliveryPartner || '',
          trackingNumber: del.trackingNumber || '',
          estimatedDelivery: del.estimatedDelivery || ''
        });
      },
      error: () => {
        this.deliveryDetails = null;
        this.deliveryForm.reset();
      }
    });
  }

  onUpdateStatus(orderId: number, status: string): void {
    this.apiService.updateOrderStatus(orderId, status).subscribe(() => {
      this.loadOrders();
      if (this.selectedOrder && this.selectedOrder.id === orderId) {
        this.selectedOrder.status = status;
      }
    });
  }

  onSubmitDelivery(): void {
    if (this.deliveryForm.invalid || !this.selectedOrder) return;

    this.apiService.updateDelivery(this.selectedOrder.id!, this.deliveryForm.value).subscribe({
      next: (del) => {
        this.deliveryDetails = del;
        this.loadOrders();
      }
    });
  }

  // Create Order Methods
  toggleCreateOrder(): void {
    this.showCreateOrder = !this.showCreateOrder;
    this.cartItems = [];
    this.newOrderForm.reset();
  }

  addToCart(): void {
    if (!this.selectedProductId || this.selectedQuantity < 1) return;
    const prod = this.products.find(p => p.id === +this.selectedProductId);
    if (!prod) return;

    const existing = this.cartItems.find(i => i.product.id === prod.id);
    if (existing) {
      existing.quantity += this.selectedQuantity;
    } else {
      this.cartItems.push({ product: prod, quantity: this.selectedQuantity });
    }
  }

  removeFromCart(index: number): void {
    this.cartItems.splice(index, 1);
  }

  submitNewOrder(): void {
    if (this.newOrderForm.invalid || this.cartItems.length === 0) return;
    
    const req = {
      items: this.cartItems.map(c => ({
        productId: c.product.id,
        quantity: c.quantity
      }))
    };

    const customerId = this.newOrderForm.value.customerId;
    this.apiService.adminPlaceOrder(customerId, req).subscribe({
      next: (res) => {
        this.loadOrders();
        this.toggleCreateOrder();
      },
      error: (err) => {
        alert('Failed to place order: ' + (err.error?.message || err.message));
      }
    });
  }
}
