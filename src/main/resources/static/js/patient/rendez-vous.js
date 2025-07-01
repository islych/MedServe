// Attendre que le DOM soit chargé
document.addEventListener('DOMContentLoaded', function() {
    // Gestion du loading screen
    const loadingOverlay = document.querySelector('.loading-overlay');
    
    // Cacher le loading screen après le chargement complet
    window.addEventListener('load', function() {
        setTimeout(function() {
            loadingOverlay.classList.add('hidden');
            setTimeout(function() {
                loadingOverlay.style.display = 'none';
            }, 500);
        }, 800);
    });
    
    // Effet de scroll pour la navbar
    const navbar = document.querySelector('.navbar');
    window.addEventListener('scroll', function() {
        if (window.scrollY > 50) {
            navbar.classList.add('navbar-scrolled');
        } else {
            navbar.classList.remove('navbar-scrolled');
        }
    });
    
    // Effet ripple sur les boutons
    const rippleButtons = document.querySelectorAll('.ripple');
    rippleButtons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            const ripple = document.createElement('span');
            ripple.classList.add('ripple-effect');
            
            const rect = this.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);
            
            ripple.style.width = ripple.style.height = size + 'px';
            ripple.style.left = e.clientX - rect.left - size / 2 + 'px';
            ripple.style.top = e.clientY - rect.top - size / 2 + 'px';
            
            this.appendChild(ripple);
            
            setTimeout(function() {
                ripple.remove();
            }, 600);
        });
    });
    
    // Animation des éléments du formulaire
    const formGroups = document.querySelectorAll('.form-group');
    formGroups.forEach((group, index) => {
        group.style.animationDelay = `${(index + 1) * 0.1}s`;
        group.classList.add('animate__animated', 'animate__fadeInUp');
    });
    
    // Validation du formulaire
    const form = document.getElementById('appointmentForm');
    const inputs = form.querySelectorAll('input, select, textarea');
    
    inputs.forEach(input => {
        input.addEventListener('blur', function() {
            validateInput(this);
        });
    });
    
    function validateInput(input) {
        if (input.hasAttribute('required') && !input.value) {
            input.style.borderColor = '#ff4d4d';
            input.style.boxShadow = '0 0 0 3px rgba(255, 77, 77, 0.2)';
        } else {
            input.style.borderColor = '';
            input.style.boxShadow = '';
        }
    }
});

// Fonction pour gérer la soumission du formulaire avec modal de confirmation
function handleSubmit() {
    const form = document.getElementById('appointmentForm');
    let isValid = true;
    
    // Validation basique
    const requiredInputs = form.querySelectorAll('[required]');
    requiredInputs.forEach(input => {
        if (!input.value.trim()) {
            isValid = false;
            input.style.borderColor = '#ff4d4d';
            input.style.boxShadow = '0 0 0 3px rgba(255, 77, 77, 0.2)';
        }
    });
    
    if (isValid) {
        // Afficher le modal de confirmation
        const modal = document.getElementById('confirmationModal');
        modal.classList.add('active');
        
        // Soumettre le formulaire après un délai
        setTimeout(function() {
            form.submit();
        }, 2000);
    } else {
        // Animation shake pour indiquer une erreur
        const formContainer = document.querySelector('.form-container');
        formContainer.classList.add('animate__animated', 'animate__shakeX');
        
        setTimeout(function() {
            formContainer.classList.remove('animate__animated', 'animate__shakeX');
        }, 1000);
    }
}