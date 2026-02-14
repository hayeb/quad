export interface Answer {
  question: string;
  answer: string;
}

export interface Check {
  question: string;
  answer: string;
  checkResult: 'CORRECT' | 'INCORRECT' | 'UNKNOWN';
}
