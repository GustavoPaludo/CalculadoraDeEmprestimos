import { Component, Input } from '@angular/core';
import { LoanResult } from '../../models/loan/loan-result.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loan-result-grid',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './loan-result-grid.component.html',
  styleUrls: ['./loan-result-grid.component.scss']
})
export class LoanResultsGridComponent {
  @Input() records: LoanResult[] = [];
  @Input() totalInstallments: number = 120;
}
