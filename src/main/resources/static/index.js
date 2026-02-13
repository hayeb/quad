function getQuestions() {
    const amount = document.getElementById('amount').value ?? 10;
    const category = document.getElementById('category').value ?? "";
    const difficulty = document.getElementById('difficulty').value ?? "";
    const questionType = document.getElementById('questionType').value ?? "";

    fetch(`/api/questions?amount=${amount}&category=${category}&difficulty=${difficulty}&questionType=${questionType}`)
        .then(response => response.json())
        .then(response => {
            if (response.message) {
                throw new Error(response.message);
            }
            if (!questions.length) {
                throw new Error('No questions found, please change your selection.')
            }

            const newQuestionsTableBody = document.createElement('tbody');
            newQuestionsTableBody.id = 'questionsTableBody';
            for (const question of questions) {
                const answers = `<option value="">Select answer...</option>` + question.answers.map(answer => `<option value="${answer}">${answer}</option>`).join('\n');
                const row = newQuestionsTableBody.insertRow();
                row.insertCell().innerHTML = question.question;
                row.insertCell().innerHTML = `<select id="${question.question}">${answers}</select>`;
                row.insertCell().id = `ANSWER-${question.question}`;
            }

            const questionsTableBody = document.getElementById('questionsTableBody');
            questionsTableBody.parentNode.replaceChild(newQuestionsTableBody, questionsTableBody)
        })
        .catch((error) => {
            document.getElementById('error').textContent = `Error retrieving questions: ${error}`;
        });
}

function checkQuestions() {
    let toCheck = [];
    for (let row of document.getElementById('questionsTableBody').rows) {
        const question = row.cells[0].textContent;
        const answer = row.cells[1].childNodes[0].value;
        if (!answer) {
            continue;
        }
        toCheck.push({question, answer})
    }

    if (!toCheck.length) {
        return;
    }

    fetch('/api/checkanswers', {
        method: 'POST',
        body: JSON.stringify(toCheck),
        headers: {'Content-Type': 'application/json'}
    })
        .then(response =>  response.json())

        .then(response => {
            if (response.message) {
                throw new Error(response.message);
            }
            for (let checked of response) {
                const element = document.getElementById(`ANSWER-${checked.question}`);
                element.classList.remove('correct', 'incorrect', 'unknown');
                if (checked.checkResult === 'CORRECT') {
                    element.textContent = 'Correct!';
                    element.classList.add('correct');
                } else if (checked.checkResult === 'INCORRECT') {
                    element.textContent = 'Incorrect!';
                    element.classList.add('incorrect');
                } else if (checked.checkResult === 'UNKNOWN') {
                    element.textContent = 'Unknown question, please get a new list of questions!';
                    element.classList.add('unknown');
                }
            }

        })
        .catch((error) => {
            document.getElementById('error').textContent = `Error checking questions: ${error}`;
        });
}