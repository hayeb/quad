import {Component, effect, input, model} from '@angular/core';
import { Question } from "src/app/interface/questions.interface";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef, MatHeaderRow, MatHeaderRowDef, MatRow,
  MatRowDef,
  MatTable
} from "@angular/material/table";
import {MatOption, MatSelect, MatSelectChange} from "@angular/material/select";
import {Check} from "../../interface/check-answers.interface";

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
    MatHeaderRowDef
  ],
  templateUrl: './questions-table.html',
  styleUrls: ['./questions-table.css']
})
export class QuestionsTable {
  questions = input.required<Question[]>();

  answers = model.required<{question: string, answer: string | null}[]>();
  checks = input.required<Map<string, Check['checkResult']>>();

  protected updateAnswer(question: Question, $event: MatSelectChange<string>) {
    this.answers.set(this.answers().map(answer => {
      if (answer.question === question.question) {
        return {...answer, answer: $event.value};
      }
      return answer;
    }))
  }
}
