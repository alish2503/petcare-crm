const form = document.getElementById('registerForm');

const firstNameInput = document.getElementById('firstName');
const lastNameInput = document.getElementById('lastName');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');

const firstNameError = document.getElementById('firstNameError');
const lastNameError = document.getElementById('lastNameError');
const emailError = document.getElementById('emailError');
const passwordError = document.getElementById('passwordError');

const fieldsToValidate = [
  { input: firstNameInput, error: firstNameError },
  { input: lastNameInput, error: lastNameError },
  { input: emailInput, error: emailError },
  { input: passwordInput, error: passwordError }
];

fieldsToValidate.forEach(({ input, error }) => {
  input.addEventListener('input', () => validateField(input, error));
});

form?.addEventListener('submit', (e) => {
  const isValid = validateFormFields(fieldsToValidate);
  if (!isValid) e.preventDefault();
});
