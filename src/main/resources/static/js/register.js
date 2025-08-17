const form = document.getElementById('registerForm');

const firstNameInput = document.getElementById('firstName');
const lastNameInput = document.getElementById('lastName');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');

const firstNameError = document.getElementById('firstNameError');
const lastNameError = document.getElementById('lastNameError');
const emailError = document.getElementById('emailError');
const passwordError = document.getElementById('passwordError');

function validateField(input, errorElement) {
    if (input.checkValidity()) {
        errorElement.style.display = 'none';
        return true;
    } else {
        errorElement.style.display = 'block';
        return false;
    }
}

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
    let isValid = true;
    for (const { input, error } of fieldsToValidate) {
      const result = validateField(input, error);
      if (!result) isValid = false;
    }
    if (!isValid) e.preventDefault();
});
