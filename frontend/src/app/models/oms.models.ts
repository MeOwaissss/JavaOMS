export interface Role {
  id?: number;
  name: string;
}

export interface User {
  id?: number;
  username: string;
  email: string;
  role: Role;
  enabled: boolean;
}

export interface Customer {
  id?: number;
  user: User;
  firstName: string;
  lastName: string;
  phone: string;
  address?: string;
  city?: string;
  state?: string;
  zipCode?: string;
}

export interface Category {
  id?: number;
  name: string;
  description?: string;
}

export interface Product {
  id?: number;
  category: Category;
  name: string;
  description?: string;
  price: number;
  gstPercent: number;
  imageUrl?: string;
  sku: string;
}

export interface Stock {
  id?: number;
  product: Product;
  quantity: number;
  minimumRequired: number;
}

export interface StockHistory {
  id?: number;
  stock: Stock;
  changeQuantity: number;
  type: string;
  reason?: string;
  createdAt: string;
}

export interface OrderItem {
  id?: number;
  product: Product;
  quantity: number;
  price: number;
  gstAmount: number;
}

export interface Order {
  id?: number;
  orderNumber: string;
  customer: Customer;
  totalAmount: number;
  status: string;
  createdAt: string;
  orderItems: OrderItem[];
}

export interface Invoice {
  id?: number;
  order: Order;
  invoiceNumber: string;
  issuedAt: string;
  pdfData: string;
}

export interface Delivery {
  id?: number;
  order: Order;
  deliveryPartner?: string;
  trackingNumber?: string;
  status: string;
  estimatedDelivery?: string;
  updatedAt: string;
}

export interface CustomerIssue {
  id?: number;
  customer: Customer;
  subject: string;
  description: string;
  status: string;
  createdAt: string;
}

export interface IssueReply {
  id?: number;
  issue: CustomerIssue;
  user: User;
  message: string;
  createdAt: string;
}

export interface Notification {
  id?: number;
  user: User;
  message: string;
  isRead: boolean;
  createdAt: string;
}

export interface WhatsAppLog {
  id?: number;
  recipientNumber: string;
  messageBody: string;
  sentAt: string;
  status: string;
}

export interface DashboardStats {
  totalProducts: number;
  totalOrders: number;
  totalRevenue: number;
  totalCustomers: number;
  recentOrders: Order[];
  monthlyRevenue: { [key: string]: number };
  categoryShares: { [key: string]: number };
}
export interface LoginResponse {
  token: string;
  username: string;
  email: string;
  role: string;
}
