// Script pour la page d'accueil du docteur

// Attendre que le DOM soit complètement chargé
document.addEventListener('DOMContentLoaded', function() {
    // Animation de chargement
    const loadingOverlay = document.querySelector('.loading-overlay');
    if (loadingOverlay) {
        // Masquer l'overlay de chargement après un court délai
        setTimeout(function() {
            loadingOverlay.classList.add('hidden');
            // Supprimer complètement après la fin de l'animation
            setTimeout(function() {
                loadingOverlay.style.display = 'none';
            }, 500);
        }, 800);
    }

    // Ajouter l'effet ripple aux boutons
    const rippleButtons = document.querySelectorAll('.ripple');
    rippleButtons.forEach(button => {
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

    // Animation de la navbar au scroll
    window.addEventListener('scroll', function() {
        const navbar = document.querySelector('.navbar');
        if (window.scrollY > 50) {
            navbar.classList.add('navbar-scrolled');
        } else {
            navbar.classList.remove('navbar-scrolled');
        }
    });

    // Ajouter des classes Ripple manquantes
    const navLinks = document.querySelectorAll('.nav-left a');
    navLinks.forEach(link => {
        if (!link.classList.contains('ripple')) {
            link.classList.add('ripple');
        }
    });

    const logoutBtn = document.querySelector('.logout-btn');
    if (logoutBtn && !logoutBtn.classList.contains('ripple')) {
        logoutBtn.classList.add('ripple');
    }

    // Gestion de la notification flottante
    const floatingNotification = document.querySelector('.floating-notification');
    if (floatingNotification) {
        // Animation de pulsation toutes les 5 secondes
        setInterval(() => {
            floatingNotification.classList.add('pulse-animation');
            setTimeout(() => {
                floatingNotification.classList.remove('pulse-animation');
            }, 1000);
        }, 5000);

        // Clic sur la notification
        floatingNotification.addEventListener('click', function() {
            // Rediriger vers la page des messages ou afficher un modal
            // Exemple: window.location.href = '/doctor/messages';
            alert('Vous avez 3 nouveaux messages de patients.');
        });
    }

    // Animation d'apparition progressive des cartes
    const featureCards = document.querySelectorAll('.feature-card');
    featureCards.forEach((card, index) => {
        // Ajouter un délai progressif pour chaque carte
        setTimeout(() => {
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, 300 + (index * 150));
    });

    // Citation aléatoire (pourrait être enrichi avec plusieurs citations)
    const quotes = [
        {
            text: "La médecine est un art qui s'apprend par l'étude, la réflexion et la pratique.",
            author: "Hippocrate"
        },
        {
            text: "Le bon médecin traite la maladie; le grand médecin traite le patient qui a la maladie.",
            author: "William Osler"
        },
        {
            text: "Guérir parfois, soulager souvent, consoler toujours.",
            author: "Edward Livingston Trudeau"
        }
    ];

    // Sélectionner une citation aléatoire si un élément blockquote existe
    const blockquote = document.querySelector('blockquote p');
    const cite = document.querySelector('blockquote cite');
    
    if (blockquote && cite) {
        const randomQuote = quotes[Math.floor(Math.random() * quotes.length)];
        blockquote.textContent = `"${randomQuote.text}"`;
        cite.textContent = `- ${randomQuote.author}`;
    }

    // Ajouter une classe pour l'animation de hover sur les cartes
    document.querySelectorAll('.feature-card').forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.classList.add('feature-hover');
        });
        card.addEventListener('mouseleave', function() {
            this.classList.remove('feature-hover');
        });
    });

    // Initialiser les styles pour l'animation des cartes
    document.querySelectorAll('.feature-card').forEach(card => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        card.style.transition = 'all 0.5s ease-out';
    });

    // Ajouter des messages d'accueil personnalisés
    const currentHour = new Date().getHours();
    let greeting;
    
    if (currentHour < 12) {
        greeting = "Bonjour";
    } else if (currentHour < 18) {
        greeting = "Bon après-midi";
    } else {
        greeting = "Bonsoir";
    }
    
    const welcomeTitle = document.querySelector('.welcome-banner h1');
    if (welcomeTitle) {
        welcomeTitle.textContent = `${greeting}, Docteur !`;
    }
});

// Ajouter une classe CSS pour l'animation de pulsation
const style = document.createElement('style');
style.textContent = `
    @keyframes pulse-animation {
        0% {
            box-shadow: 0 0 0 0 rgba(26, 115, 232, 0.7);
        }
        70% {
            box-shadow: 0 0 0 15px rgba(26, 115, 232, 0);
        }
        100% {
            box-shadow: 0 0 0 0 rgba(26, 115, 232, 0);
        }
    }
    
    .pulse-animation {
        animation: pulse-animation 1s;
    }
    
    .feature-hover {
        transform: translateY(-5px) !important;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
        background: rgba(255, 255, 255, 0.15) !important;
    }
`;
document.head.appendChild(style);