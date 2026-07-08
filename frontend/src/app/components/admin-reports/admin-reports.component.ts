import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { Category, Order } from '../../models/oms.models';

@Component({
  selector: 'app-admin-reports',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-reports.component.html',
  styles: []
})
export class AdminReportsComponent implements OnInit {
  private fb = inject(FormBuilder);
  private apiService = inject(ApiService);

  categories: Category[] = [];
  reportOrders: Order[] = [];
  totalRevenue: number = 0;

  filterForm: FormGroup = this.fb.group({
    startDate: [''],
    endDate: [''],
    categoryId: [''],
    customerId: ['']
  });

  ngOnInit(): void {
    this.apiService.getCategories().subscribe(cats => this.categories = cats);
    this.onGenerateReport();
  }

  onGenerateReport(): void {
    const f = this.filterForm.value;
    const catId = f.categoryId ? Number(f.categoryId) : undefined;
    const custId = f.customerId ? Number(f.customerId) : undefined;

    this.apiService.getReport(f.startDate || undefined, f.endDate || undefined, custId, catId).subscribe({
      next: (data) => {
        this.reportOrders = data;
        this.totalRevenue = data
          .filter(o => o.status !== 'CANCELLED')
          .reduce((sum, o) => sum + o.totalAmount, 0);
      }
    });
  }

  onExportExcel(): void {
    const f = this.filterForm.value;
    const catId = f.categoryId ? Number(f.categoryId) : undefined;
    const custId = f.customerId ? Number(f.customerId) : undefined;
    const url = this.apiService.getReportExcelUrl(f.startDate || undefined, f.endDate || undefined, custId, catId);
    window.open(url, '_blank');
  }
}
