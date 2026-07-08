import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from './services/auth.service';
import { ApiService } from './services/api.service';
import { Notification } from './models/oms.models';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './app.component.html',
  styles: []
})
export class AppComponent implements OnInit {
  public authService = inject(AuthService);
  private apiService = inject(ApiService);
  private router = inject(Router);

  notifications: Notification[] = [];
  unreadCount: number = 0;

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.loadNotifications();
    }
  }

  loadNotifications(): void {
    if (!this.authService.isLoggedIn()) return;
    this.apiService.getNotifications().subscribe({
      next: (data) => {
        this.notifications = data;
        this.unreadCount = data.filter(n => !n.isRead).length;
      }
    });
  }

  markRead(n: Notification): void {
    if (!n.id || n.isRead) return;
    this.apiService.markNotificationRead(n.id).subscribe({
      next: () => this.loadNotifications()
    });
  }

  markAllRead(): void {
    this.apiService.markAllNotificationsRead().subscribe({
      next: () => this.loadNotifications()
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  focusSearch(): void {
    setTimeout(() => {
      const el = document.getElementById('catalogSearchInput');
      if (el) {
        el.focus();
      }
    }, 150);
  }
}
