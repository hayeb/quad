import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Difficulty, Question, QuestionType} from "../interface/questions.interface";

@Injectable({
    providedIn: 'root',
})
export class QuestionsService {
    constructor(private readonly http: HttpClient) {
    }

    public getQuestions(amount: number = 10, category: number | null, difficulty: Difficulty | null, questionType: QuestionType | null) {
        const httpParams: Record<string, string | boolean | number> = {};
        httpParams['amount'] = amount;
        if (category) {
            httpParams['category'] = category;
        }

        if (difficulty) {
            httpParams['difficulty'] = difficulty;
        }

        if (questionType) {
            httpParams['questionType'] = questionType;
        }
        return this.http.get<Question[]>('http://localhost:8080/api/questions', { params: httpParams })
    }
}
