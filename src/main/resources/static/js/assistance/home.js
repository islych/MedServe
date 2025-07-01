document.addEventListener('DOMContentLoaded', function() {
    // Variables
    const navbar = document.querySelector('.navbar');
    const loadingOverlay = document.querySelector('.loading-overlay');
    const dashboardCards = document.querySelectorAll('.dashboard-card');
    const statItems = document.querySelectorAll('.stat-number');
    let notificationCount = 3; // Example notification count
    
    // Function to handle page loading
    function handlePageLoad() {
        if (loadingOverlay) {
            setTimeout(() => {
                loadingOverlay.classList.add('hidden');
                setTimeout(() => {
                    loadingOverlay.style.display = 'none';
                }, 500);
            }, 1000);
        }
    }
    
    // Function to handle navbar on scroll
    function handleScroll() {
        if (window.scrollY > 50) {
            navbar.classList.add('navbar-scrolled');
        } else {
            navbar.classList.remove('navbar-scrolled');
        }
    }
    
    // Function to animate counting for stats
    function animateStatNumbers() {
        statItems.forEach(item => {
            const target = parseInt(item.getAttribute('data-target'));
            const duration = 2000; // Animation duration in milliseconds
            const step = target / (duration / 16); // 60fps (16ms per frame)
            let current = 0;
            
            const counter = setInterval(() => {
                current += step;
                if (current >= target) {
                    item.textContent = target;
                    clearInterval(counter);
                } else {
                    item.textContent = Math.floor(current);
                }
            }, 16);
        });
    }
    
    // Function to add ripple effect to buttons
    function addRippleEffect() {
        const buttons = document.querySelectorAll('.action-btn, .logout-btn');
        
        buttons.forEach(button => {
            button.addEventListener('click', function(e) {
                const x = e.clientX - e.target.getBoundingClientRect().left;
                const y = e.clientY - e.target.getBoundingClientRect().top;
                
                const ripple = document.createElement('span');
                ripple.classList.add('ripple-effect');
                ripple.style.left = `${x}px`;
                ripple.style.top = `${y}px`;
                
                this.appendChild(ripple);
                
                setTimeout(() => {
                    ripple.remove();
                }, 600);
            });
        });
    }
    
    // Function to create notification element
    function createNotification() {
        // Check if notification already exists
        if (document.querySelector('.floating-notification')) {
            return;
        }
        
        const notificationArea = document.createElement('div');
        notificationArea.classList.add('notification-area');
        
        const notification = document.createElement('div');
        notification.classList.add('floating-notification');
        notification.innerHTML = `
            <i class="fas fa-bell"></i>
            <span>Vous avez des notifications</span>
            <div class="notification-badge">${notificationCount}</div>
        `;
        
        notification.addEventListener('click', function() {
            // You would typically open a notifications panel here
            alert('Notifications: \n- Nouveau rendez-vous ajouté\n- Dossier patient mis à jour\n- Rappel: Réunion à 14h');
            notification.remove();
        });
        
        notificationArea.appendChild(notification);
        document.body.appendChild(notificationArea);
    }
    
    // Function to initialize dashboard tooltips
    function initTooltips() {
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        if (typeof bootstrap !== 'undefined') {
            tooltipTriggerList.map(function(tooltipTriggerEl) {
                return new bootstrap.Tooltip(tooltipTriggerEl);
            });
        }
    }
    
    // Function to handle dashboard card interactions
    function handleCardInteractions() {
        dashboardCards.forEach(card => {
            card.addEventListener('mouseenter', function() {
                const icon = this.querySelector('.card-icon');
                if (icon) {
                    icon.style.transform = 'scale(1.1) rotate(5deg)';
                }
            });
            
            card.addEventListener('mouseleave', function() {
                const icon = this.querySelector('.card-icon');
                if (icon) {
                    icon.style.transform = 'scale(1)';
                }
            });
        });
    }
    
    // Create today's appointment summary (example data)
    function createAppointmentSummary() {
        const today = new Date();
        const dateOptions = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
        const formattedDate = today.toLocaleDateString('fr-FR', dateOptions);
        
        const appointmentSummarySection = document.getElementById('appointment-summary');
        if (appointmentSummarySection) {
            appointmentSummarySection.innerHTML = `
                <h3>Rendez-vous du ${formattedDate}</h3>
                <p>Total: <strong>15</strong> rendez-vous programmés aujourd'hui</p>
                <div class="appointment-status">
                    <span class="status-item">
                        <i class="fas fa-check-circle text-success"></i> Confirmés: 12
                    </span>
                    <span class="status-item">
                        <i class="fas fa-clock text-warning"></i> En attente: 2
                    </span>
                    <span class="status-item">
                        <i class="fas fa-exclamation-circle text-danger"></i> Urgences: 1
                    </span>
                </div>
            `;
        }
    }
    
    // Function to initialize the dashboard
    function initDashboard() {
        handlePageLoad();
        window.addEventListener('scroll', handleScroll);
        
        // Init animations with slight delay to ensure DOM is fully rendered
        setTimeout(() => {
            animateStatNumbers();
            handleCardInteractions();
            addRippleEffect();
            initTooltips();
            createAppointmentSummary();
            
            // Show notification after a delay
            setTimeout(createNotification, 3000);
        }, 500);
    }
    
    // Initialize the dashboard
    initDashboard();
    
    // Calendar initialization (if using a calendar library)
    const calendarEl = document.getElementById('appointments-calendar');
    if (calendarEl && typeof FullCalendar !== 'undefined') {
        const calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay'
            },
            locale: 'fr',
            events: [
                // Example events - would typically come from your backend
                {
                    title: 'Dr. Martin - Patient: Dubois',
                    start: '2025-05-08T09:00:00',
                    end: '2025-05-08T09:30:00',
                    color: '#0056b3'
                },
                {
                    title: 'Dr. Laurent - Patient: Bernard',
                    start: '2025-05-08T10:00:00',
                    end: '2025-05-08T10:45:00',
                    color: '#28a745'
                },
                {
                    title: 'Dr. Petit - Patient: Thomas',
                    start: '2025-05-08T14:00:00',
                    end: '2025-05-08T14:30:00',
                    color: '#fd7e14'
                }
            ],
            eventClick: function(info) {
                alert('Détails du rendez-vous: ' + info.event.title);
            }
        });
        
        calendar.render();
    }
    
    // Example search functionality
    const searchInput = document.getElementById('search-input');
    if (searchInput) {
        searchInput.addEventListener('keyup', function(e) {
            if (e.key === 'Enter') {
                alert('Recherche pour: ' + this.value);
                this.value = '';
            }
        });
    }
    
    // Example form submission handling
    const quickSearchForm = document.getElementById('quick-search-form');
    if (quickSearchForm) {
        quickSearchForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const formData = new FormData(this);
            const searchType = formData.get('search-type');
            const searchQuery = formData.get('search-query');
            
            alert(`Recherche ${searchType}: ${searchQuery}`);
            this.reset();
        });
    }
});