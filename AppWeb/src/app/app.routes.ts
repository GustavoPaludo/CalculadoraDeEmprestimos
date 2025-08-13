import { Routes } from '@angular/router';
import { LoanCalculatorComponent } from './pages/loan-calculator-page/loan-calculator-page.component';

export const routes: Routes = [
    {
        path: "calculator",
        component: LoanCalculatorComponent,
    },

    { path: "", component: LoanCalculatorComponent },
    { path: "**", redirectTo: "calculator", pathMatch: "full" }
];
