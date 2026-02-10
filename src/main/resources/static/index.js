function getQuestions() {
    const amount = document.getElementById('amount').value ?? 10;
    const category = document.getElementById('category').value ?? "";
    const difficulty = document.getElementById('difficulty').value ?? "";
    const questionType = document.getElementById('questionType').value ?? "";

    fetch(`/api/questions?amount=${amount}&category=${category}&difficulty=${difficulty}&questionType=${questionType}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error: ${response.status}`);
            }
            return response.text();
        })
        .then(response => {
            const questions = JSON.parse(response);

            if (!questions.length) {
                throw new Error('No questions found, please change your selection.')
            }

            const newQuestionsTableBody = document.createElement('tbody');
            newQuestionsTableBody.id = 'questionsTableBody';
            for (const question of questions) {
                const answers = `<option value="">Select answer...</option>` + question.answers.map(answer => `<option value="${answer}">${answer}</option>`).join('\n');
                const row = newQuestionsTableBody.insertRow();
                row.insertCell().innerHTML = question.value;
                row.insertCell().innerHTML = `<select id="${question.value}">${answers}</select>`;
                row.insertCell().id = `ANSWER-${question.value}`;
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
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error: ${response.status}`);
            }
            return response.text();
        })
        .then(response => {
            for (let checked of JSON.parse(response)) {
                document.getElementById(`ANSWER-${checked.question}`).textContent = checked.correct ? 'Correct!' : 'Incorrect :(';
                document.getElementById(`ANSWER-${checked.question}`).classList.remove('correct', 'incorrect');
                document.getElementById(`ANSWER-${checked.question}`).classList.add(checked.correct ? 'correct' : 'incorrect');
            }

        })
        .catch((error) => {
            document.getElementById('error').textContent = `Error checking questions: ${error}`;
        });
}