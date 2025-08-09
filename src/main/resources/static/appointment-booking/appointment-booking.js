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
  const form = document.getElementById(`form-${vetId}`);
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
  const slotInput = form.querySelector('input[name="slotId"]');
  const petSelect = form.querySelector('select[name="petId"]');
  const slotButtons = form.querySelectorAll('.slot-btn');

  if (reasonInput) {
    reasonInput.value = '';
    reasonInput.classList.remove('invalid');
  }
  if (reasonError) {
    reasonError.style.display = 'none';
  }
  if (slotInput) {
    slotInput.value = '';
  }
  if (petSelect) {
    petSelect.selectedIndex = 0;
  }
  if (slotButtons) {
    slotButtons.forEach(btn => btn.classList.remove('selected'));
  }
}

function selectSlot(button, slotId) {
  const form = button.closest('form');
  if (!form) return;

  const slotInput = form.querySelector('.slot-input');
  if (slotInput) {
    slotInput.value = slotId;
  }

  const baseAction = form.getAttribute('data-base-action');
  form.action = baseAction.replace('__slotId__', slotId);

  form.querySelectorAll('.slot-btn').forEach(btn => btn.classList.remove('selected'));
  button.classList.add('selected');
}

function cancelAppointment(cancelButton) {
  const form = cancelButton.closest('form');
  if (!form) return;

  resetForm(form);
  form.style.display = 'none';
}
