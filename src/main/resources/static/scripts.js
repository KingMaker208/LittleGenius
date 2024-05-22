const selectedNumbers = [];
const selectedCells = new Set();

function selectNumber(cell) {
    const id = cell.id;  // Use ID to uniquely identify cells
    if (selectedCells.has(id)) {
        cell.classList.remove('selected');
        selectedCells.delete(id);
        const index = selectedNumbers.indexOf(parseInt(cell.textContent));
        if (index > -1) {
            selectedNumbers.splice(index, 1);  // Remove number from the list
        }
    } else {
        if (selectedNumbers.length < 3) {  // Only add if less than three numbers are selected
            cell.classList.add('selected');
            selectedCells.add(id);
            selectedNumbers.push(parseInt(cell.textContent));
        }
    }
    updateNumbersInput();
    // Check if exactly three numbers are selected, then submit
    if (selectedNumbers.length === 3) {
        submitForm();  // Automatically submit when three numbers are selected
    }
}

function updateNumbersInput() {
    // Update the hidden input field with selected numbers
    const numbers = Array.from(selectedCells).map(id => {
        const [row, col] = id.split('-').slice(1).map(Number);  // Extract indices
        const cell = document.getElementById(`cell-${row}-${col}`);
        return cell.textContent.trim();
    });
    document.getElementById('numbersInput').value = numbers.join(',');
}

function submitForm() {
    const numbersInput = document.getElementById('numbersInput');
    console.log("Submitting numbers:", numbersInput.value); // This will log the numbers being submitted
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

window.onload = function() {
    loadDarkMode();
    document.querySelectorAll('td').forEach(cell => {
        cell.addEventListener('click', function() {
            selectNumber(this);
        });
    });
    // Ensure there is a button with ID 'submitBtn' if it's referenced
    const submitButton = document.getElementById('submitBtn');
    if (submitButton) {
        submitButton.addEventListener('click', submitForm);
    }
};