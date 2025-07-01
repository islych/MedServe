document.addEventListener('DOMContentLoaded', function() {
    setTimeout(function() {
        document.querySelector('.loading-overlay').classList.add('hidden');
    }, 800);

    const calendarEl = document.getElementById('calendar');

    const calendar = new FullCalendar.Calendar(calendarEl, {
        locale: 'fr',
        initialView: 'dayGridMonth',
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        events: '/doctor/api/appointments',
        eventClick: function(info) {
            const event = info.event;
            document.getElementById('modal-patient').textContent = event.extendedProps.patientName || 'Inconnu';
            document.getElementById('modal-date').textContent = event.start.toLocaleString();
            document.getElementById('modal-reason').textContent = event.extendedProps.reason || '---';
            document.getElementById('modal-urgency').textContent = event.extendedProps.urgency || '---';
            document.getElementById('modal-status').textContent = event.extendedProps.status || '---';
            document.getElementById('modal-manage-link').href = '/doctor/appointments/' + event.id;
            document.getElementById('appointmentModal').style.display = 'block';
        }
    });

    calendar.render();

    document.getElementById('closeModal').addEventListener('click', function() {
        document.getElementById('appointmentModal').style.display = 'none';
    });
});
