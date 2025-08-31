function toggleForm(doctorId) {
    const form = document.getElementById(`form-${doctorId}`);
    if (!form) return;

    if (form.style.display === 'none' || form.style.display === '') {
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

        form.style.display = 'block';
    } else {
        form.style.display = 'none';
    }
}

function cancelAppointment(cancelButton) {
    const form = cancelButton.closest('form');
    if (!form) return;
    form.style.display = 'none';
}
