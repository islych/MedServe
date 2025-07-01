// Animation au chargement de la page
document.addEventListener('DOMContentLoaded', () => {
    animateCards();
});

function animateCards() {
    const cards = document.querySelectorAll('.dashboard-card');
    cards.forEach((card, index) => {
        setTimeout(() => {
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, index * 150);
    });
}

// Gestion des interactions
document.querySelectorAll('.dashboard-card').forEach(card => {
    card.addEventListener('click', (e) => {
        if (!e.target.closest('a')) return;
        // Ajouter un effet de chargement
        card.classList.add('clicked');
        setTimeout(() => card.classList.remove('clicked'), 500);
    });
});

window.addEventListener('load', function() {
    setTimeout(function() {
        document.querySelector('.loading-overlay').classList.add('hidden');
    }, 800);
});

// Navbar scroll effect
window.addEventListener('scroll', function() {
    const navbar = document.querySelector('.navbar');
    if (window.scrollY > 50) {
        navbar.classList.add('navbar-scrolled');
    } else {
        navbar.classList.remove('navbar-scrolled');
    }
});

// Ripple effect on buttons
document.querySelectorAll('.ripple').forEach(button => {
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

// Notification click event
document.querySelector('.floating-notification').addEventListener('click', function() {
    alert('Vous avez 3 nouvelles notifications!');
});

// Animate elements on scroll
const observerOptions = {
    threshold: 0.1
};

const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.classList.add('animate__animated', 'animate__fadeIn');
            observer.unobserve(entry.target);
        }
    });
}, observerOptions);

document.querySelectorAll('.feature-card').forEach(card => {
    observer.observe(card);
});