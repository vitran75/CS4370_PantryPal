window.addEventListener('load', () => {
    // Select all elements with the 'submittable' class
    const fields = document.querySelectorAll('.submittable');

    fields.forEach(field => {
        // Attach keypress event listener to each field
        field.addEventListener('keypress', (event) => {
            // Detect Enter key without Shift
            if (event.key === 'Enter' && !event.shiftKey) {
                event.preventDefault(); // Prevent default action
                const form = field.closest('form'); // Locate the enclosing form
                if (form) {
                    form.submit(); // Submit the form
                    console.log(`Form submitted: ${form.id || 'no-id'}`);
                }
            }
        });
    });
});
