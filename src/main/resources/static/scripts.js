const selectedNumbers = [];
const selectedCells = new Set();

function selectNumber(cell) {
    const id = cell.id;
    if (selectedCells.has(id)) {
        cell.classList.remove('selected');
        selectedCells.delete(id);
        const index = selectedNumbers.indexOf(parseInt(cell.textContent));
        if (index > -1) {
            selectedNumbers.splice(index, 1);
        }
    } else {
        if (selectedNumbers.length < 3) {
            cell.classList.add('selected');
            selectedCells.add(id);
            selectedNumbers.push(parseInt(cell.textContent));
        }
    }
    updateNumbersInput();
    if (selectedNumbers.length === 3) {
        submitForm();
    }
}

function updateNumbersInput() {
    const numbers = Array.from(selectedCells).map(id => {
        const [row, col] = id.split('-').slice(1).map(Number);
        const cell = document.getElementById(`cell-${row}-${col}`);
        return cell.textContent.trim();
    });
    document.getElementById('numbersInput').value = numbers.join(',');
}

function submitForm() {
    const numbersInput = document.getElementById('numbersInput');
    console.log("Submitting numbers:", numbersInput.value);
    if (numbersInput.value === "") {
        alert("Please select some numbers first.");
        return false;
    }
    document.forms[0].submit();
}

function resetSelections() {
    selectedNumbers.length = 0;
    selectedCells.clear();
    document.querySelectorAll('.selected').forEach(cell => cell.classList.remove('selected'));
    updateNumbersInput();
}

function toggleDarkMode() {
    document.body.classList.toggle('dark-mode');
    localStorage.setItem('darkMode', document.body.classList.contains('dark-mode'));
}

function loadDarkMode() {
    const darkMode = localStorage.getItem('darkMode') === 'true';
    if (darkMode) {
        document.body.classList.add('dark-mode');
        document.getElementById('darkModeToggle').checked = true;
    }
}

function setProgress(percent) {
    const circle = document.querySelector('.progress-ring__circle');
    const radius = circle.r.baseVal.value;
    const circumference = 2 * Math.PI * radius;
    circle.style.strokeDasharray = `${circumference} ${circumference}`;
    circle.style.strokeDashoffset = circumference - (percent / 100) * circumference;
}

function updateProgressRing() {
    const totalTokens = 50; // Assuming 50 is the total number of tokens
    const tokensCompleted = parseInt(document.querySelector('.completed-container p').textContent.split(',').length, 10);
    const tokensSkipped = parseInt(document.querySelector('.skipped-container p').textContent.split(',').length, 10);
    const tokensPassed = tokensCompleted + tokensSkipped;
    const progress = (tokensPassed / totalTokens) * 100; // Calculate progress percentage
    setProgress(progress);
}

window.onload = function() {
    loadDarkMode();
    document.querySelectorAll('td').forEach(cell => {
        cell.addEventListener('click', function() {
            selectNumber(this);
        });
    });
    const submitButton = document.getElementById('submitBtn');
    if (submitButton) {
        submitButton.addEventListener('click', submitForm);
    }
    updateProgressRing(); // Call this function to update the progress ring on load
};
