import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { 
  Category, Product, Stock, StockHistory, Order, 
  Invoice, Delivery, CustomerIssue, IssueReply, 
  Notification, WhatsAppLog, DashboardStats 
} from '../models/oms.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private http = inject(HttpClient);
  private base = environment.apiUrl;

  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.base}/categories`);
  }
  createCategory(c: Category): Observable<Category> {
    return this.http.post<Category>(`${this.base}/categories`, c);
  }
  updateCategory(id: number, c: Category): Observable<Category> {
    return this.http.put<Category>(`${this.base}/categories/${id}`, c);
  }
  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/categories/${id}`);
  }

  getProducts(query?: string, categoryId?: number): Observable<Product[]> {
    let params = new HttpParams();
    if (query) params = params.set('query', query);
    if (categoryId) params = params.set('categoryId', categoryId.toString());
    return this.http.get<Product[]>(`${this.base}/products`, { params });
  }
  getProduct(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.base}/products/${id}`);
  }
  createProduct(p: any): Observable<Product> {
    return this.http.post<Product>(`${this.base}/products`, p);
  }
  updateProduct(id: number, p: any): Observable<Product> {
    return this.http.put<Product>(`${this.base}/products/${id}`, p);
  }
  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/products/${id}`);
  }

  getStocks(): Observable<Stock[]> {
    return this.http.get<Stock[]>(`${this.base}/stocks`);
  }
  getLowStocks(): Observable<Stock[]> {
    return this.http.get<Stock[]>(`${this.base}/stocks/low`);
  }
  adjustStock(adj: any): Observable<Stock> {
    return this.http.post<Stock>(`${this.base}/stocks/adjust`, adj);
  }
  getStockHistory(productId: number): Observable<StockHistory[]> {
    return this.http.get<StockHistory[]>(`${this.base}/stocks/${productId}/history`);
  }
  getAllStockHistory(): Observable<StockHistory[]> {
    return this.http.get<StockHistory[]>(`${this.base}/stocks/history`);
  }

  getOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.base}/orders`);
  }
  getOrder(id: number): Observable<Order> {
    return this.http.get<Order>(`${this.base}/orders/${id}`);
  }
  placeOrder(orderReq: any): Observable<Order> {
    return this.http.post<Order>(`${this.base}/orders`, orderReq);
  }
  adminPlaceOrder(customerId: number, orderReq: any): Observable<Order> {
    return this.http.post<Order>(`${this.base}/orders/admin/${customerId}`, orderReq);
  }
  updateOrderStatus(id: number, status: string): Observable<Order> {
    return this.http.put<Order>(`${this.base}/orders/${id}/status`, null, {
      params: new HttpParams().set('status', status)
    });
  }

  // Customer Profile Endpoints
  getCustomerProfile(): Observable<any> {
    return this.http.get<any>(`${this.base}/customers/profile`);
  }
  updateCustomerProfile(updateDto: any): Observable<any> {
    return this.http.put<any>(`${this.base}/customers/profile`, updateDto);
  }
  getAllCustomers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.base}/customers`);
  }
  adminCreateCustomer(req: any): Observable<any> {
    return this.http.post<any>(`${this.base}/customers`, req);
  }
  adminUpdateCustomer(id: number, updateDto: any): Observable<any> {
    return this.http.put<any>(`${this.base}/customers/admin/${id}`, updateDto);
  }
  adminDeleteCustomer(id: number): Observable<any> {
    return this.http.delete<any>(`${this.base}/customers/${id}`);
  }

  getDelivery(orderId: number): Observable<Delivery> {
    return this.http.get<Delivery>(`${this.base}/deliveries/${orderId}`);
  }
  updateDelivery(orderId: number, update: any): Observable<Delivery> {
    return this.http.put<Delivery>(`${this.base}/deliveries/${orderId}`, update);
  }

  getInvoice(orderId: number): Observable<Invoice> {
    return this.http.get<Invoice>(`${this.base}/invoices/${orderId}`);
  }
  downloadInvoiceUrl(orderId: number): string {
    return `${this.base}/invoices/${orderId}/download`;
  }

  getIssues(): Observable<CustomerIssue[]> {
    return this.http.get<CustomerIssue[]>(`${this.base}/issues`);
  }
  getIssue(id: number): Observable<CustomerIssue> {
    return this.http.get<CustomerIssue>(`${this.base}/issues/${id}`);
  }
  createIssue(issue: any): Observable<CustomerIssue> {
    return this.http.post<CustomerIssue>(`${this.base}/issues`, issue);
  }
  replyToIssue(issueId: number, message: string): Observable<IssueReply> {
    return this.http.post<IssueReply>(`${this.base}/issues/${issueId}/replies`, { message });
  }
  getIssueReplies(issueId: number): Observable<IssueReply[]> {
    return this.http.get<IssueReply[]>(`${this.base}/issues/${issueId}/replies`);
  }

  getNotifications(): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.base}/notifications`);
  }
  markNotificationRead(id: number): Observable<void> {
    return this.http.put<void>(`${this.base}/notifications/${id}/read`, null);
  }
  markAllNotificationsRead(): Observable<void> {
    return this.http.put<void>(`${this.base}/notifications/read-all`, null);
  }

  getWhatsAppLogs(): Observable<WhatsAppLog[]> {
    return this.http.get<WhatsAppLog[]>(`${this.base}/whatsapp-logs`);
  }

  getReport(startDate?: string, endDate?: string, customerId?: number, categoryId?: number): Observable<Order[]> {
    let params = new HttpParams();
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);
    if (customerId) params = params.set('customerId', customerId.toString());
    if (categoryId) params = params.set('categoryId', categoryId.toString());
    return this.http.get<Order[]>(`${this.base}/reports`, { params });
  }
  getReportExcelUrl(startDate?: string, endDate?: string, customerId?: number, categoryId?: number): string {
    let url = `${this.base}/reports/excel?`;
    if (startDate) url += `startDate=${startDate}&`;
    if (endDate) url += `endDate=${endDate}&`;
    if (customerId) url += `customerId=${customerId}&`;
    if (categoryId) url += `categoryId=${categoryId}&`;
    return url;
  }

  getDashboardStats(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${this.base}/dashboard/stats`);
  }
}
