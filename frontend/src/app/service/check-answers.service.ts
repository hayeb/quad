import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Answer, Check } from '../interface/check-answers.interface';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CheckAnswersService {
  private readonly http = inject(HttpClient);

  public checkAnswers(answers: Answer[]): Observable<Check[]> {
    return this.http.post<Check[]>('http://localhost:8080/api/checkanswers', answers);
  }
}
