document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('form.appointment-form').forEach(form => {
    const reasonInput = form.querySelector('input[name="reason"]');
    const reasonError = form.querySelector('.reason-error');

    if (reasonInput && reasonError) {
      reasonInput.addEventListener('input', () => {
        validateField(reasonInput, reasonError);
      });

      form.addEventListener('submit', e => {
        const isValid = validateFormFields([{ input: reasonInput, error: reasonError }]);
        if (!isValid) e.preventDefault();
      });
    }
  });
});

function toggleForm(vetId) {
  const form = document.getElementById(`form-${doctorId}`);
  if (!form) return;

  if (form.style.display === 'none' || form.style.display === '') {
    resetForm(form);
    form.style.display = 'block';
  } else {
    form.style.display = 'none';
  }
}

function resetForm(form) {
  const reasonInput = form.querySelector('input[name="reason"]');
  const reasonError = form.querySelector('.reason-error');
  const petSelect = form.querySelector('select[name="petId"]');
  const slotRadios = form.querySelectorAll('input[name="slotId"]');

  if (reasonInput) {
    reasonInput.value = '';
    reasonInput.classList.remove('invalid');
  }
  if (reasonError) {
    reasonError.style.display = 'none';
  }
  if (petSelect) {
    petSelect.selectedIndex = 0;
  }
  if (slotRadios) {
    slotRadios.forEach(radio => radio.checked = false);
  }
}

function cancelAppointment(cancelButton) {
  const form = cancelButton.closest('form');
  if (!form) return;

  resetForm(form);
  form.style.display = 'none';
}
