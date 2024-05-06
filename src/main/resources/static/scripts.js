const selectedNumbers = [];
const selectedCells = new Set();

function selectNumber(cell) {
    const id = cell.id;  // Use ID to uniquely identify cells
    if (selectedCells.has(id)) {
        cell.classList.remove('selected');
        selectedCells.delete(id);
    } else {
        cell.classList.add('selected');
        selectedCells.add(id);
    }
    updateNumbersInput();
}

function updateNumbersInput() {
    const numbers = Array.from(selectedCells).map(id => {
        const [row, col] = id.split('-').slice(1).map(Number);  // Extract indices
        const cell = document.getElementById(`cell-${row}-${col}`);
        return cell.textContent.trim();
    });
    document.getElementById('numbersInput').value = numbers.join(',');
}

function submitForm() {
    const numbersInput = document.getElementById('numbersInput');
    if (numbersInput.value === "") {
        alert("Please select some numbers first.");
        return;
    }
    document.forms[0].submit();
}

window.onload = function() {
    document.querySelectorAll('td').forEach(cell => {
        cell.addEventListener('click', function() {
            selectNumber(this);
        });
    });
    document.getElementById('submitBtn').addEventListener('click', submitForm);
};
