import { Component, computed, effect, input, output } from '@angular/core';
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
import { MatTooltip } from '@angular/material/tooltip';

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
    MatTooltip,
  ],
  templateUrl: './questions-table.html',
  styleUrls: ['./questions-table.css'],
})
export class QuestionsTable {
  questions = input.required<Question[]>();
  checks = input.required<Check[]>();

  answered = output<{ question: string; answer: string | null }>();

  protected formGroup = new FormGroup<Record<string, FormControl<string | null>>>({});

  checkResult = computed(() => {
    const results: Record<string, Check['checkResult']> = {};
    for (const check of this.checks()) {
      results[check.question] = check.checkResult;
    }
    return results;
  });

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
