const formContainer = document.getElementById('editFormContainer');

function showAddForm() {
    const diagnosisInput = document.getElementById('diagnosis');
    const treatmentInput = document.getElementById('treatment');
    diagnosisInput.value = '';
    treatmentInput.value = '';
    formContainer.style.display = 'block';
}

function cancel() {
    formContainer.style.display = 'none';
}
