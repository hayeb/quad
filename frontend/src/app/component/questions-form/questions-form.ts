import {Component, output} from '@angular/core';
import {MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {MatButton} from "@angular/material/button";
import {Difficulty, QuestionType} from "../../interface/questions.interface";
import {MatOption, MatSelect} from "@angular/material/select";

export interface QuestionsFormOutput {
    amount: number;
    category: number | null;
    difficulty: Difficulty | null;
    questionType: QuestionType | null;
}

@Component({
    selector: 'app-questions-form',
    imports: [
        MatFormField,
        MatLabel,
        MatInput,
        ReactiveFormsModule,
        MatButton,
        MatSelect,
        MatOption
    ],
    templateUrl: './questions-form.html',
    styleUrls: ['./questions-form.css'],
})
export class QuestionsForm {

    searchQuestions = output<QuestionsFormOutput>();
    checkAnswers = output();

    protected readonly formGroup = new FormGroup({
        amount: new FormControl<number>(10, {nonNullable: true}),
        category: new FormControl<number | null>(null),
        difficulty: new FormControl<Difficulty | null>(null),
        questionType: new FormControl<QuestionType | null>(null)
    });

    protected onSearchQuestions() {
        this.searchQuestions.emit({
            amount: this.formGroup.controls.amount.value,
            category: this.formGroup.controls.category.value,
            difficulty: this.formGroup.controls.difficulty.value,
            questionType: this.formGroup.controls.questionType.value,
        })
    }

    protected readonly QuestionType = QuestionType;
    protected readonly Difficulty = Difficulty;
}
