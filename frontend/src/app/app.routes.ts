import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { 
    path: 'login', 
    loadComponent: () => import('./components/login/login.component').then(m => m.LoginComponent) 
  },
  { 
    path: 'register', 
    loadComponent: () => import('./components/register/register.component').then(m => m.RegisterComponent) 
  },
  { 
    path: 'admin', 
    loadComponent: () => import('./components/admin-dashboard/admin-dashboard.component').then(m => m.AdminDashboardComponent),
    canActivate: [authGuard],
    data: { role: 'ROLE_ADMIN' }
  },
  { 
    path: 'admin/products', 
    loadComponent: () => import('./components/admin-products/admin-products.component').then(m => m.AdminProductsComponent),
    canActivate: [authGuard],
    data: { role: 'ROLE_ADMIN' }
  },
  { 
    path: 'admin/customers', 
    loadComponent: () => import('./components/admin-customers/admin-customers.component').then(m => m.AdminCustomersComponent),
    canActivate: [authGuard],
    data: { role: 'ROLE_ADMIN' }
  },
  { 
    path: 'admin/stock', 
    loadComponent: () => import('./components/admin-stock/admin-stock.component').then(m => m.AdminStockComponent),
    canActivate: [authGuard],
    data: { role: 'ROLE_ADMIN' }
  },
  { 
    path: 'admin/orders', 
    loadComponent: () => import('./components/admin-orders/admin-orders.component').then(m => m.AdminOrdersComponent),
    canActivate: [authGuard],
    data: { role: 'ROLE_ADMIN' }
  },
  { 
    path: 'admin/reports', 
    loadComponent: () => import('./components/admin-reports/admin-reports.component').then(m => m.AdminReportsComponent),
    canActivate: [authGuard],
    data: { role: 'ROLE_ADMIN' }
  },
  { 
    path: 'admin/tickets', 
    loadComponent: () => import('./components/admin-tickets/admin-tickets.component').then(m => m.AdminTicketsComponent),
    canActivate: [authGuard],
    data: { role: 'ROLE_ADMIN' }
  },
  { 
    path: 'admin/whatsapp', 
    loadComponent: () => import('./components/whatsapp-logs/whatsapp-logs.component').then(m => m.WhatsappLogsComponent),
    canActivate: [authGuard],
    data: { role: 'ROLE_ADMIN' }
  },
  { 
    path: 'catalog', 
    loadComponent: () => import('./components/customer-catalog/customer-catalog.component').then(m => m.CustomerCatalogComponent),
    canActivate: [authGuard],
    data: { role: 'ROLE_CUSTOMER' }
  },
  { 
    path: 'orders', 
    loadComponent: () => import('./components/customer-orders/customer-orders.component').then(m => m.CustomerOrdersComponent),
    canActivate: [authGuard],
    data: { role: 'ROLE_CUSTOMER' }
  },
  { 
    path: 'tickets', 
    loadComponent: () => import('./components/customer-tickets/customer-tickets.component').then(m => m.CustomerTicketsComponent),
    canActivate: [authGuard],
    data: { role: 'ROLE_CUSTOMER' }
  },
  { 
    path: 'profile', 
    loadComponent: () => import('./components/customer-profile/customer-profile.component').then(m => m.CustomerProfileComponent),
    canActivate: [authGuard],
    data: { role: 'ROLE_CUSTOMER' }
  },
  { path: '**', redirectTo: 'login' }
];
