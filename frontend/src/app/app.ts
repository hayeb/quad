import { Component, inject, signal } from '@angular/core';
import { QuestionsForm, QuestionsFormOutput } from './component/questions-form/questions-form';
import { QuestionsTable } from './component/questions-table/questions-table';
import { Question } from './interface/questions.interface';
import { QuestionsService } from './service/questions.service';
import { CheckAnswersService } from './service/check-answers.service';
import { Check } from './interface/check-answers.interface';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatButton } from '@angular/material/button';

type ApiCallState<T> =
  | { kind: 'IDLE' }
  | { kind: 'LOADING' }
  | { kind: 'SUCCESS'; data: T }
  | { kind: 'ERROR'; error: string };

@Component({
  selector: 'app-root',
  imports: [QuestionsForm, QuestionsTable, MatProgressSpinner, MatButton],
  templateUrl: `./app.html`,
  styleUrls: ['./app.css'],
})
export class App {
  private readonly questionsService = inject(QuestionsService);
  private readonly checkAnswerService = inject(CheckAnswersService);

  protected readonly questions = signal<ApiCallState<Question[]>>({ kind: 'IDLE' });
  protected readonly checks = signal<ApiCallState<Record<string, Check['checkResult']>>>({ kind: 'IDLE' });

  protected answers = signal<Record<string, string | null>>({});

  protected searchQuestions($event: QuestionsFormOutput) {
    this.questions.set({ kind: 'LOADING' });
    this.questionsService
      .getQuestions($event.amount, $event.category, $event.difficulty, $event.questionType)
      .subscribe(questions => {
        this.questions.set({ kind: 'SUCCESS', data: questions });
        this.answers.set({});
      });
  }

  protected checkAnswers() {
    this.checks.set({ kind: 'LOADING' });
    const answered = Object.entries(this.answers())
      .filter(answer => !!answer[1])
      .map(answer => ({ question: answer[0], answer: answer[1] as string }));
    this.checkAnswerService.checkAnswers(answered).subscribe(checks => {
      const checkMap: Record<string, Check['checkResult']> = {};
      for (const check of checks) {
        checkMap[check.question] = check.checkResult;
      }
      this.checks.set({ kind: 'SUCCESS', data: checkMap });
    });
  }

  protected updateAnswer({ question, answer }: { question: string; answer: string | null }) {
    this.answers.set({ ...this.answers(), [question]: answer });
  }

  protected readonly Object = Object;
}
