import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../services/api.service';
import { DashboardStats } from '../../models/oms.models';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-dashboard.component.html',
  styles: []
})
export class AdminDashboardComponent implements OnInit {
  private apiService = inject(ApiService);
  stats: DashboardStats | null = null;
  revenueKeys: string[] = [];
  categoryKeys: string[] = [];

  ngOnInit(): void {
    this.apiService.getDashboardStats().subscribe({
      next: (data) => {
        this.stats = data;
        if (data.monthlyRevenue) {
          this.revenueKeys = Object.keys(data.monthlyRevenue);
        }
        if (data.categoryShares) {
          this.categoryKeys = Object.keys(data.categoryShares);
        }
      }
    });
  }

  getRevenueValue(key: string): number {
    return this.stats?.monthlyRevenue?.[key] || 0;
  }

  getCategoryValue(key: string): number {
    return this.stats?.categoryShares?.[key] || 0;
  }

  getMaxRevenue(): number {
    if (!this.stats || !this.stats.monthlyRevenue) return 1;
    const vals = Object.values(this.stats.monthlyRevenue);
    return vals.length > 0 ? Math.max(...vals) : 1;
  }
}
