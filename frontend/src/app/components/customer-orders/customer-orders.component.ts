import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../services/api.service';
import { Order, Delivery } from '../../models/oms.models';

@Component({
  selector: 'app-customer-orders',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './customer-orders.component.html',
  styles: []
})
export class CustomerOrdersComponent implements OnInit {
  private apiService = inject(ApiService);

  orders: Order[] = [];
  selectedOrder: Order | null = null;
  deliveryDetails: Delivery | null = null;

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.apiService.getOrders().subscribe(data => this.orders = data);
  }

  onSelectOrder(order: Order): void {
    this.selectedOrder = order;
    this.apiService.getDelivery(order.id!).subscribe({
      next: (del) => this.deliveryDetails = del,
      error: () => this.deliveryDetails = null
    });
  }

  onDownloadInvoice(orderId: number): void {
    const url = this.apiService.downloadInvoiceUrl(orderId);
    window.open(url, '_blank');
  }
}
