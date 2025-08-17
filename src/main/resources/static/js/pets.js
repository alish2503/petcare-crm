const form = document.getElementById('editForm');
const formContainer = document.getElementById('editFormContainer');
const nameInput = document.getElementById('edit-name');
const speciesSelect = document.getElementById('edit-species');
const genderSelect = document.getElementById('edit-gender');
const birthDateInput = document.getElementById('edit-birthDate');

document.addEventListener("DOMContentLoaded", () => {
  flatpickr(birthDateInput, {
    dateFormat: "Y-m-d",
    altInput: true,
    altFormat: "d M Y",
    maxDate: "today",
    allowInput: false
  });
});

form.addEventListener("submit", (e) => {
  if (!birthDateInput.value || birthDateInput.value.trim() === "") {
    e.preventDefault();
    alert("Please select a date of birth");
  }
});

function showEditForm(button) {
  const card = button.closest('.card');
  const petId = card.dataset.petId;

  const name = card.querySelector('.name-span').textContent.trim();
  const species = card.querySelector('.species-span').textContent.trim();
  const gender = card.querySelector('.gender-span').textContent.trim();
  const birthDate = card.querySelector('.birthDate-span').textContent.trim();

  form.action = `/pets/${petId}`;

  let methodInput = form.querySelector('input[name="_method"]');
  if (!methodInput) {
    methodInput = document.createElement("input");
    methodInput.type = "hidden";
    methodInput.name = "_method";
    form.appendChild(methodInput);
  }
  methodInput.value = "PUT";

  nameInput.value = name;
  speciesSelect.value = species;
  genderSelect.value = gender;

  const flatpickrInstance = birthDateInput._flatpickr;
  if (flatpickrInstance) {
    flatpickrInstance.setDate(birthDate);
  }

  formContainer.style.display = 'block';
  card.appendChild(formContainer);
}

function showAddForm() {
  form.action = "/pets";

  const methodInput = form.querySelector('input[name="_method"]');
  if (methodInput) {
    methodInput.remove();
  }

  nameInput.value = '';
  speciesSelect.value = 'Dog';
  genderSelect.value = 'Male';

  const flatpickrInstance = birthDateInput._flatpickr;
  if (flatpickrInstance) {
    flatpickrInstance.clear();
  }

  formContainer.style.display = 'block';
  document.getElementById('petList').appendChild(formContainer);
}

function cancelEdit() {
  formContainer.style.display = 'none';
  document.getElementById('petList').appendChild(formContainer);
}

