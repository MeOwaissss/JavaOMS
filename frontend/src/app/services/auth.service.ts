import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { LoginResponse } from '../models/oms.models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/auth`;
  private currentUserSubject = new BehaviorSubject<LoginResponse | null>(null);

  constructor() {
    const storedUser = localStorage.getItem('currentUser');
    if (storedUser) {
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }

  public get currentUserValue(): LoginResponse | null {
    return this.currentUserSubject.value;
  }

  login(credentials: any): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(user => {
        localStorage.setItem('currentUser', JSON.stringify(user));
        this.currentUserSubject.next(user);
      })
    );
  }

  checkHealth(): Observable<any> {
    return this.http.get(`${this.apiUrl}/health`);
  }

  register(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userData);
  }

  logout(): void {
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }

  getToken(): string | null {
    return this.currentUserValue ? this.currentUserValue.token : null;
  }

  isLoggedIn(): boolean {
    return this.currentUserValue !== null;
  }

  getRole(): string | null {
    return this.currentUserValue ? this.currentUserValue.role : null;
  }

  getUsername(): string | null {
    return this.currentUserValue ? this.currentUserValue.username : null;
  }
}
