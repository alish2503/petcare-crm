const editForm = document.getElementById('editForm');
const nameInput = document.getElementById('edit-name');
const nameError = document.getElementById('name-error');

nameInput?.addEventListener('input', () => {
  validateField(nameInput, nameError);
});

editForm?.addEventListener('submit', (e) => {
  const fieldsToValidate = [{ input: nameInput, error: nameError }];

  const isValid = validateFormFields(fieldsToValidate);

  if (!isValid) e.preventDefault();
});

document.addEventListener("DOMContentLoaded", function () {
    flatpickr("#edit-birthDate", {
        dateFormat: "Y-m-d",
        maxDate: "today"
    });
});

function showEditForm(id, name, species, gender, birthDate, button) {
    const formContainer = document.getElementById('editFormContainer');
    const form = document.getElementById('editForm');

    document.getElementById('edit-id').value = id;
    document.getElementById('edit-name').value = name;
    document.getElementById('edit-species').value = species;
    document.getElementById('edit-gender').value = gender;
    document.getElementById('edit-birthDate')._flatpickr.setDate(birthDate);

    const card = button.closest('.pet-card');
    card.appendChild(formContainer);
    formContainer.style.display = 'block';
}


function showAddForm() {
    const formContainer = document.getElementById('editFormContainer');

    document.getElementById('edit-id').value = '';
    document.getElementById('edit-name').value = '';
    document.getElementById('edit-species').value = 'Dog';
    document.getElementById('edit-gender').value = 'Male';
    document.getElementById('edit-birthDate')._flatpickr.clear();
    document.getElementById('petList').appendChild(formContainer);

    formContainer.style.display = 'block';
}

function cancelEdit() {
    document.getElementById('editFormContainer').style.display = 'none';
}