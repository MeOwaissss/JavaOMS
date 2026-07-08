import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { Category, Product } from '../../models/oms.models';

interface CartItem {
  product: Product;
  quantity: number;
}

@Component({
  selector: 'app-customer-catalog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customer-catalog.component.html',
  styles: []
})
export class CustomerCatalogComponent implements OnInit {
  private apiService = inject(ApiService);

  products: Product[] = [];
  categories: Category[] = [];
  searchQuery: string = '';
  selectedCategoryId: string = '';
  isAscending: boolean = true;

  cart: CartItem[] = [];
  bookingSuccess: boolean = false;
  bookingMessage: string = '';
  bookingError: string = '';

  ngOnInit(): void {
    this.loadCategories();
    this.onSearch();
  }

  loadCategories(): void {
    this.apiService.getCategories().subscribe(cats => this.categories = cats);
  }

  onSearch(): void {
    const catId = this.selectedCategoryId ? Number(this.selectedCategoryId) : undefined;
    this.apiService.getProducts(this.searchQuery, catId).subscribe(data => {
      this.products = data;
      this.sortProducts();
    });
  }

  toggleSort(): void {
    this.isAscending = !this.isAscending;
    this.sortProducts();
  }

  sortProducts(): void {
    if (this.isAscending) {
      this.products.sort((a, b) => a.price - b.price);
    } else {
      this.products.sort((a, b) => b.price - a.price);
    }
  }

  getSelectedCategoryName(): string {
    if (!this.selectedCategoryId) return '';
    const cat = this.categories.find(c => c.id === Number(this.selectedCategoryId));
    return cat ? cat.name : '';
  }

  addToCart(product: Product): void {
    const existing = this.cart.find(item => item.product.id === product.id);
    if (existing) {
      existing.quantity += 1;
    } else {
      this.cart.push({ product, quantity: 1 });
    }
  }

  removeFromCart(productId: number): void {
    this.cart = this.cart.filter(item => item.product.id !== productId);
  }

  updateQuantity(productId: number, qty: number): void {
    const item = this.cart.find(i => i.product.id === productId);
    if (item) {
      item.quantity = Math.max(1, qty);
    }
  }

  getCartTotal(): number {
    return this.cart.reduce((sum, item) => {
      const subtotal = item.product.price * item.quantity;
      const gst = subtotal * (item.product.gstPercent / 100);
      return sum + subtotal + gst;
    }, 0);
  }

  onBookProducts(): void {
    if (this.cart.length === 0) return;

    const req = {
      items: this.cart.map(item => ({
        productId: item.product.id,
        quantity: item.quantity
      }))
    };

    this.apiService.placeOrder(req).subscribe({
      next: (order) => {
        this.bookingSuccess = true;
        this.bookingMessage = `Order Booked Successfully! Order ID: ${order.orderNumber}`;
        this.bookingError = '';
        this.cart = [];
        setTimeout(() => this.bookingSuccess = false, 5000);
      },
      error: (err) => {
        this.bookingError = err.error?.message || 'Failed to place order due to stock constraints';
        this.bookingSuccess = false;
      }
    });
  }
}
