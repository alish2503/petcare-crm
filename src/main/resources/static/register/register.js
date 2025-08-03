const form = document.getElementById('registerForm');

const firstNameInput = document.getElementById('firstName');
const lastNameInput = document.getElementById('lastName');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');

const firstNameError = document.getElementById('firstNameError');
const lastNameError = document.getElementById('lastNameError');
const emailError = document.getElementById('emailError');
const passwordError = document.getElementById('passwordError');

form?.addEventListener('submit', (e) => {
  let valid = true;

  if (!firstNameInput.checkValidity()) {
    firstNameError.style.display = 'block';
    valid = false;
  } else {
    firstNameError.style.display = 'none';
  }

  if (!lastNameInput.checkValidity()) {
    lastNameError.style.display = 'block';
    valid = false;
  } else {
    lastNameError.style.display = 'none';
  }

  if (!emailInput.checkValidity()) {
    emailError.style.display = 'block';
    valid = false;
  } else {
    emailError.style.display = 'none';
  }

  if (!passwordInput.checkValidity()) {
    passwordError.style.display = 'block';
    valid = false;
  } else {
    passwordError.style.display = 'none';
  }

  if (!valid) {
    e.preventDefault();
  }
});
