import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoanRequest } from '../../models/loan/loan-request.model';
import { LoanResult } from '../../models/loan/loan-result.model';
import { Observable, of } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({ providedIn: 'root' })
export class LoanCalculationService {

    constructor(private http: HttpClient) { }

    public calculate(request: LoanRequest): Observable<LoanResult[]> {
        let url = environment.url;

        return this.http.post<LoanResult[]>(url + '/loan/calculate', request);
    }
}
