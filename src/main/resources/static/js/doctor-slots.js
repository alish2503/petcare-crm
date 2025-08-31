const slotPicker = flatpickr("#slotDatetime", {
    enableTime: true,
    dateFormat: "Y-m-d H:i",
    time_24hr: true,
    minDate: new Date()
});

const newSlotsList = document.getElementById("newSlotsList");
const hiddenInputsContainer = document.getElementById("slotsHiddenInputs");
const saveBtn = document.querySelector("#slotsForm .save-btn");

function updateSaveBtn() {
    saveBtn.disabled = hiddenInputsContainer.querySelectorAll("input").length === 0;
}

function formatSlot(date) {
    return new Intl.DateTimeFormat("en-GB", {
        day: "2-digit",
        month: "short",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit",
        hour12: false
    }).format(date).replace(",", "");
}

document.getElementById("addSlotBtn").addEventListener("click", () => {
    const input = document.getElementById("slotDatetime");
    const date = slotPicker.selectedDates[0];
    if (!date) return;

    const rawValue = input.value;
    const formatted = formatSlot(date);

    if ([...hiddenInputsContainer.querySelectorAll('input')]
        .some(i => i.value === rawValue)) {
        alert("Slot has already been chosen");
        return;
    }

    const hiddenInput = document.createElement("input");
    hiddenInput.type = "hidden";
    hiddenInput.name = "slots";
    hiddenInput.value = rawValue;
    hiddenInputsContainer.appendChild(hiddenInput);

    const wrapper = document.createElement("div");
    wrapper.className = "slot-wrapper";

    const btn = document.createElement("button");
    btn.type = "button";
    btn.className = "slot-btn";
    btn.innerHTML = `<span>${formatted}</span>`;

    btn.addEventListener("click", () => {
        if (confirm("Remove this unsaved slot?")) {
            wrapper.remove();
            hiddenInput.remove();
            updateSaveBtn();
        }
    });

    wrapper.appendChild(btn);
    newSlotsList.appendChild(wrapper);

    slotPicker.clear();
    updateSaveBtn();
});

document.getElementById("slotsForm").addEventListener("submit", (e) => {
    if (hiddenInputsContainer.querySelectorAll("input").length === 0) {
        e.preventDefault();
        alert("Please add at least one slot before saving");
    }
});

document.querySelectorAll(".slot-form .slot-btn:not(.booked)").forEach(btn => {
    btn.addEventListener("click", () => {
        if (confirm("Do you really want to cancel this slot?")) {
            const form = btn.closest("form");
            if (form) form.submit();
        }
    });
});

updateSaveBtn();
