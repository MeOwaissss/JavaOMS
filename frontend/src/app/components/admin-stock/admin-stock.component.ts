import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { Stock, StockHistory } from '../../models/oms.models';

@Component({
  selector: 'app-admin-stock',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-stock.component.html',
  styles: []
})
export class AdminStockComponent implements OnInit {
  private fb = inject(FormBuilder);
  private apiService = inject(ApiService);

  stocks: Stock[] = [];
  histories: StockHistory[] = [];

  selectedStock: Stock | null = null;

  adjustmentForm: FormGroup = this.fb.group({
    productId: [null, Validators.required],
    changeQuantity: [1, [Validators.required, Validators.min(1)]],
    type: ['ADD', Validators.required],
    reason: ['']
  });

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.apiService.getStocks().subscribe(data => this.stocks = data);
    this.apiService.getAllStockHistory().subscribe(data => this.histories = data);
  }

  onSelectStock(stock: Stock): void {
    this.selectedStock = stock;
    this.adjustmentForm.patchValue({
      productId: stock.product.id,
      changeQuantity: 1,
      type: 'ADD',
      reason: ''
    });
  }

  onSubmitAdjustment(): void {
    if (this.adjustmentForm.invalid) return;

    this.apiService.adjustStock(this.adjustmentForm.value).subscribe({
      next: () => {
        this.selectedStock = null;
        this.adjustmentForm.reset({
          productId: null,
          changeQuantity: 1,
          type: 'ADD',
          reason: ''
        });
        this.loadData();
      }
    });
  }
}
