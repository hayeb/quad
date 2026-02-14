import { Component, effect, input, output } from '@angular/core';
import { Question } from 'src/app/interface/questions.interface';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable,
} from '@angular/material/table';
import { MatOption, MatSelect } from '@angular/material/select';
import { Check } from '../../interface/check-answers.interface';
import { MatIcon } from '@angular/material/icon';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-questions-table',
  imports: [
    MatTable,
    MatRowDef,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatHeaderCellDef,
    MatCellDef,
    MatSelect,
    MatOption,
    MatHeaderRow,
    MatRow,
    MatHeaderRowDef,
    MatIcon,
    ReactiveFormsModule,
  ],
  templateUrl: './questions-table.html',
  styleUrls: ['./questions-table.css'],
})
export class QuestionsTable {
  questions = input.required<Question[]>();
  checks = input<Record<string, Check['checkResult']>>();

  answered = output<{ question: string; answer: string | null }>();

  protected formGroup = new FormGroup<Record<string, FormControl<string | null>>>({});

  constructor() {
    effect(() => {
      const questions = this.questions();
      this.formGroup = new FormGroup<Record<string, FormControl<string | null>>>({});

      for (const question of questions) {
        const answerControl = new FormControl<string | null>(null);
        answerControl.valueChanges.subscribe(answer =>
          this.answered.emit({ question: question.question, answer: answer })
        );
        this.formGroup.controls[question.question] = answerControl;
      }
    });
  }
}
