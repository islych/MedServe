// Doctor Profile JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Add navbar scroll effect
    const navbar = document.querySelector('.navbar');
    window.addEventListener('scroll', function() {
        if (window.scrollY > 50) {
            navbar.classList.add('navbar-scrolled');
        } else {
            navbar.classList.remove('navbar-scrolled');
        }
    });

    // Add ripple effect to all buttons and links
    const rippleElements = document.querySelectorAll('.ripple');
    rippleElements.forEach(function(element) {
        element.addEventListener('click', createRipple);
    });

    function createRipple(event) {
        const button = event.currentTarget;
        
        // Remove any existing ripple elements
        const ripple = button.querySelector('.ripple-effect');
        if (ripple) {
            ripple.remove();
        }
        
        const circle = document.createElement('span');
        const diameter = Math.max(button.clientWidth, button.clientHeight);
        const radius = diameter / 2;
        
        circle.style.width = circle.style.height = `${diameter}px`;
        circle.style.left = `${event.clientX - (button.offsetLeft + radius)}px`;
        circle.style.top = `${event.clientY - (button.offsetTop + radius)}px`;
        circle.classList.add('ripple-effect');
        
        button.appendChild(circle);
        
        // Remove the ripple effect after animation completes
        setTimeout(() => {
            circle.remove();
        }, 600);
    }

    // Add animation to profile details
    const detailItems = document.querySelectorAll('.detail-item');
    detailItems.forEach((item, index) => {
        item.style.animationDelay = `${0.1 * (index + 1)}s`;
        item.classList.add('animated');
    });

    // Add interactive hover effects for better UX
    const actionBtn = document.querySelector('.action-btn');
    if (actionBtn) {
        actionBtn.addEventListener('mouseover', function() {
            this.style.transform = 'translateY(-5px)';
        });
        
        actionBtn.addEventListener('mouseout', function() {
            this.style.transform = '';
        });
    }

    // Initialize any statistic counters (if present)
    const statNumbers = document.querySelectorAll('.stat-number');
    if (statNumbers.length > 0) {
        animateStatNumbers(statNumbers);
    }

    // Function to animate stat numbers with counting effect
    function animateStatNumbers(elements) {
        elements.forEach(element => {
            const target = parseInt(element.textContent);
            let count = 0;
            const duration = 2000; // ms
            const interval = Math.ceil(duration / target);
            
            const counter = setInterval(() => {
                count += 1;
                element.textContent = count;
                
                if (count >= target) {
                    clearInterval(counter);
                }
            }, interval);
        });
    }
});

// Add smooth scrolling for anchor links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function(e) {
        e.preventDefault();
        
        const targetId = this.getAttribute('href');
        const targetElement = document.querySelector(targetId);
        
        if (targetElement) {
            window.scrollTo({
                top: targetElement.offsetTop - 80,
                behavior: 'smooth'
            });
        }
    });
});

// Add fade animations for better user experience
function addFadeInAnimation() {
    const elementsToAnimate = document.querySelectorAll('.profile-container, .profile-card, .action-btn');
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('fade-in');
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.1 });
    
    elementsToAnimate.forEach(element => {
        observer.observe(element);
    });
}

// Call this function if needed
// addFadeInAnimation();

// Enhance the detail items with icons based on their content
function enhanceDetailItems() {
    const detailItems = document.querySelectorAll('.detail-item');
    
    detailItems.forEach(item => {
        const label = item.querySelector('label').textContent.toLowerCase();
        let iconClass = '';
        
        if (label.includes('spécialité')) {
            iconClass = 'fas fa-stethoscope';
        } else if (label.includes('email')) {
            iconClass = 'fas fa-envelope';
        } else if (label.includes('adresse')) {
            iconClass = 'fas fa-map-marker-alt';
        } else if (label.includes('ville')) {
            iconClass = 'fas fa-city';
        } else if (label.includes('téléphone')) {
            iconClass = 'fas fa-phone';
        } else if (label.includes('expérience')) {
            iconClass = 'fas fa-briefcase';
        }
        
        if (iconClass) {
            const icon = document.createElement('i');
            icon.className = iconClass;
            item.querySelector('label').prepend(icon);
            icon.style.marginRight = '10px';
        }
    });
}

// Call function on page load
window.addEventListener('load', enhanceDetailItems);