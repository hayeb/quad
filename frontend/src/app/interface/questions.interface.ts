export interface Question {
    question: string,
    answers: string[]
}

export enum QuestionType {
    MULTIPLE_CHOICE = 'MULTIPLE_CHOICE',
    BOOLEAN = 'BOOLEAN',
}

export enum Difficulty {
    EASY = 'EASY',
    MEDIUM = 'MEDIUM',
    HARD = 'HARD',
}