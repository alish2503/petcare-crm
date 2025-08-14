document.addEventListener("DOMContentLoaded", () => {
  const editForm = document.getElementById('editForm');
  const nameInput = document.getElementById('edit-name');
  const nameError = document.getElementById('name-error');

  flatpickr("#edit-birthDate", {
    dateFormat: "Y-m-d",
    maxDate: "today"
  });

  nameInput?.addEventListener('input', () => {
    validateField(nameInput, nameError);
  });

  editForm?.addEventListener('submit', (e) => {
    const fieldsToValidate = [{ input: nameInput, error: nameError }];
    const isValid = validateFormFields(fieldsToValidate);
    if (!isValid) e.preventDefault();
  });
});

function showEditForm(button) {
  const card = button.closest('.card');
  const petId = card.dataset.petId;

  const name = card.querySelector('.name-span').textContent.trim();
  const species = card.querySelector('.species-span').textContent.trim();
  const gender = card.querySelector('.gender-span').textContent.trim();
  const birthDate = card.querySelector('.birthDate-span').textContent.trim();

  const formContainer = document.getElementById('editFormContainer');
  const form = document.getElementById('editForm');

  form.action = `/pets/${petId}`;

  let methodInput = form.querySelector('input[name="_method"]');
  if (!methodInput) {
    methodInput = document.createElement("input");
    methodInput.type = "hidden";
    methodInput.name = "_method";
    form.appendChild(methodInput);
  }
  methodInput.value = "PUT";

  document.getElementById('edit-name').value = name;
  document.getElementById('edit-species').value = species;
  document.getElementById('edit-gender').value = gender;

  const flatpickrInstance = document.getElementById('edit-birthDate')._flatpickr;
  if (flatpickrInstance) {
    flatpickrInstance.setDate(birthDate);
  }

  formContainer.style.display = 'block';
  card.appendChild(formContainer);
}

function showAddForm() {
  const formContainer = document.getElementById('editFormContainer');
  const form = document.getElementById('editForm');

  form.action = "/pets";

  const methodInput = form.querySelector('input[name="_method"]');
  if (methodInput) {
    methodInput.remove();
  }

  document.getElementById('edit-name').value = '';
  document.getElementById('edit-species').value = 'Dog';
  document.getElementById('edit-gender').value = 'Male';

  const flatpickrInstance = document.getElementById('edit-birthDate')._flatpickr;
  if (flatpickrInstance) {
    flatpickrInstance.clear();
  }

  formContainer.style.display = 'block';
  document.getElementById('petList').appendChild(formContainer);
}

function cancelEdit() {
    const formContainer = document.getElementById('editFormContainer');
    document.getElementById('editFormContainer').style.display = 'none';
    document.getElementById('petList').appendChild(formContainer);
}
