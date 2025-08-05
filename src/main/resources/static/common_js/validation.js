function validateField(input, errorElement) {
  if (input.checkValidity()) {
    errorElement.style.display = 'none';
    return true;
  } else {
    errorElement.style.display = 'block';
    return false;
  }
}

function validateFormFields(fields) {
  let isValid = true;
  for (const { input, error } of fields) {
    const result = validateField(input, error);
    if (!result) isValid = false;
  }
  return isValid;
}
