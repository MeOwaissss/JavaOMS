import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../services/api.service';
import { WhatsAppLog } from '../../models/oms.models';

@Component({
  selector: 'app-whatsapp-logs',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './whatsapp-logs.component.html',
  styles: []
})
export class WhatsappLogsComponent implements OnInit {
  private apiService = inject(ApiService);
  logs: WhatsAppLog[] = [];

  ngOnInit(): void {
    this.apiService.getWhatsAppLogs().subscribe(data => this.logs = data);
  }
}
