import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoanRequest } from '../../models/loan/loan-request.model';
import { LoanResult } from '../../models/loan/loan-result.model';
import { LoanCalculationService } from '../../services/loan/loan-calculator.service';
import { LoanFormComponent } from '../../components/loan-form/loan-form.component';
import { LoanResultsGridComponent } from '../../components/loan-result-grid/loan-result-grid.component';

@Component({
    selector: 'app-loan-calculator-page',
    standalone: true,
    imports: [CommonModule, LoanFormComponent, LoanResultsGridComponent],
    templateUrl: './loan-calculator-page.component.html',
    styleUrls: ['./loan-calculator-page.component.scss']
})
export class LoanCalculatorComponent {
    records: LoanResult[] = [];
    loading = false;

    constructor(private loanService: LoanCalculationService) { }

    onCalculate(request: LoanRequest) {
        this.loading = true;
        
        this.loanService.calculate(request).subscribe({
            next: (response) => {
                this.records = response;
                this.loading = false;
            },
            error: () => {
                alert('Erro ao calcular empréstimo');
                this.loading = false;
            }
        });
    }
}
