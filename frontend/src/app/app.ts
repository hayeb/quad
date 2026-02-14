import { Component, inject, OnInit, signal } from '@angular/core';
import { QuestionsForm, QuestionsFormOutput } from './component/questions-form/questions-form';
import { QuestionsTable } from './component/questions-table/questions-table';
import { Question } from './interface/questions.interface';
import { QuestionsService } from './service/questions.service';
import { CheckAnswersService } from './service/check-answers.service';
import { Check } from './interface/check-answers.interface';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatButton } from '@angular/material/button';
import { MatProgressBar } from '@angular/material/progress-bar';
import { catchError, map, of } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { Category } from './interface/category.interface';
import { CategoriesService } from './service/categories.service';

enum ApiState {
  IDLE = 'IDLE',
  LOADING = 'LOADING',
  SUCCESS = 'SUCCESS',
  ERROR = 'ERROR',
}

type ApiCallState<T> =
  | { kind: ApiState.IDLE }
  | { kind: ApiState.LOADING }
  | { kind: ApiState.SUCCESS; data: T }
  | { kind: ApiState.ERROR; error: string };

@Component({
  selector: 'app-root',
  imports: [QuestionsForm, QuestionsTable, MatProgressSpinner, MatButton, MatProgressBar],
  templateUrl: `./app.html`,
  styleUrls: ['./app.css'],
})
export class App implements OnInit {
  private readonly categoriesService = inject(CategoriesService);
  private readonly questionsService = inject(QuestionsService);
  private readonly checkAnswerService = inject(CheckAnswersService);

  protected readonly categories = signal<ApiCallState<Category[]>>({ kind: ApiState.IDLE });
  protected readonly questions = signal<ApiCallState<Question[]>>({ kind: ApiState.IDLE });
  protected readonly checks = signal<ApiCallState<Check[]>>({ kind: ApiState.IDLE });

  protected answers = signal<Record<string, string | null>>({});

  ngOnInit(): void {
    this.loadCategories();
  }

  protected searchQuestions($event: QuestionsFormOutput) {
    this.questions.set({ kind: ApiState.LOADING });
    this.questionsService
      .getQuestions($event.amount, $event.category, $event.difficulty, $event.questionType)
      .pipe(
        map(questions => ({ kind: ApiState.SUCCESS, data: questions }) as ApiCallState<Question[]>),
        catchError((e: HttpErrorResponse) =>
          of({ kind: ApiState.ERROR, error: e.error.message as string } as ApiCallState<Question[]>)
        )
      )
      .subscribe(questions => {
        this.questions.set(questions);
        this.answers.set({});
      });
  }

  protected checkAnswers() {
    this.checks.set({ kind: ApiState.LOADING });
    const answered = Object.entries(this.answers())
      .filter(answer => !!answer[1])
      .map(answer => ({ question: answer[0], answer: answer[1] as string }));
    this.checkAnswerService
      .checkAnswers(answered)
      .pipe(
        map(questions => ({ kind: ApiState.SUCCESS, data: questions }) as ApiCallState<Check[]>),
        catchError((e: HttpErrorResponse) =>
          of({ kind: ApiState.ERROR, error: e.error.message as string } as ApiCallState<Check[]>)
        )
      )
      .subscribe(checks => {
        this.checks.set(checks);
      });
  }

  protected updateAnswer({ question, answer }: { question: string; answer: string | null }) {
    this.answers.set({ ...this.answers(), [question]: answer });
  }

  private loadCategories() {
    this.categoriesService
      .getCategories()
      .pipe(
        map(categories => ({ kind: ApiState.SUCCESS, data: categories }) as ApiCallState<Category[]>),
        catchError((e: HttpErrorResponse) =>
          of({ kind: ApiState.ERROR, error: e.error.message as string } as ApiCallState<Category[]>)
        )
      )
      .subscribe(questions => {
        this.categories.set(questions);
      });
  }

  protected readonly Object = Object;
  protected readonly ApiState = ApiState;
}
