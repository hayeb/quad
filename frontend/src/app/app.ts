import {Component, signal} from '@angular/core';
import {QuestionsForm, QuestionsFormOutput} from "./component/questions-form/questions-form";
import {QuestionsTable} from "./component/questions-table/questions-table";
import {Question} from "./interface/questions.interface";
import {QuestionsService} from "./service/questions.service";
import {CheckAnswersService} from "./service/check-answers.service";
import {Answer, Check} from "./interface/check-answers.interface";

@Component({
  selector: 'app-root',
  imports: [
    QuestionsForm,
    QuestionsTable
  ],
  templateUrl: `./app.html`,
  styleUrls: ['./app.css'],
})
export class App {
  protected readonly questions = signal<Question[]>([]);
  protected readonly answers  = signal<{question: string, answer: string | null}[]>([]);
  protected readonly checks  = signal<Map<string, Check['checkResult']>>(new Map());

  constructor(private readonly questionsService: QuestionsService, private readonly checkAnswerService: CheckAnswersService) {}

  protected searchQuestions($event: QuestionsFormOutput) {
    this.questionsService.getQuestions($event.amount, $event.category, $event.difficulty, $event.questionType)
        .subscribe(questions => {
          this.questions.set(questions);
          this.answers.set(this.questions().map(question => ({question: question.question, answer: null})));
        })
  }

  protected checkAnswers() {
    this.checkAnswerService.checkAnswers(this.answers().filter((answer): answer is Answer => !!answer.answer))
        .subscribe(checks => {
          const checkMap: Map<string, Check['checkResult']> = new Map();
          for (let check of checks) {
            checkMap.set(check.question, check.checkResult);
          }
          this.checks.set(checkMap)
        })
  }
}
