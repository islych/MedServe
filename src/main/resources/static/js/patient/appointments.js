// Hide loading overlay when page is loaded
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

// Apply animation to table rows
document.querySelectorAll('.appointments-table tr').forEach(row => {
    if (!row.classList.contains('empty-message')) {
        observer.observe(row);
    }
});

// Add hover effect for better UX on table rows
document.querySelectorAll('.appointments-table tbody tr').forEach(row => {
    row.addEventListener('mouseenter', function() {
        this.style.transform = 'translateY(-2px)';
        this.style.transition = 'transform 0.3s ease';
    });
    
    row.addEventListener('mouseleave', function() {
        this.style.transform = 'translateY(0)';
    });
});