// Animation au chargement des cartes
document.addEventListener('DOMContentLoaded', () => {
    animateCards();
    initCardInteractions();
});

function animateCards() {
    const cards = document.querySelectorAll('.doctor-card');
    cards.forEach((card, index) => {
        setTimeout(() => {
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, index * 100);
    });
}

function initCardInteractions() {
    document.querySelectorAll('.doctor-card').forEach(card => {
        card.addEventListener('click', (e) => {
            if (e.target.tagName === 'A') return;
            card.classList.toggle('active');
        });
    });
}

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

  // Animate doctor cards on scroll
  const observerOptions = {
    threshold: 0.1
  };

  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.style.opacity = "1";
        entry.target.style.transform = "translateY(0)";
        observer.unobserve(entry.target);
      }
    });
  }, observerOptions);

  document.querySelectorAll('.doctor-card').forEach(card => {
    observer.observe(card);
  });

  // Filter functionality
  document.querySelectorAll('.filter-tag').forEach(tag => {
    tag.addEventListener('click', function() {
      // Remove active class from all tags
      document.querySelectorAll('.filter-tag').forEach(t => {
        t.classList.remove('active');
      });
      // Add active class to clicked tag
      this.classList.add('active');
      
      // Here you would typically filter the doctor cards
      // For now, we'll just show a message
      if (this.textContent !== 'Tous') {
        alert('Filtrage par: ' + this.textContent);
      }
    });
  });

  // Notification click event
  document.querySelector('.floating-notification').addEventListener('click', function() {
    alert('5 nouveaux médecins ont rejoint notre plateforme!');
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

// Filtrage combiné - Version corrigée
function filterDoctors() {
  const searchTerm = document.getElementById('searchInput').value.toLowerCase();
  const activeSpecialty = document.querySelector('.filter-tag.active').dataset.specialty;
  const selectedCity = document.getElementById('cityFilter').value.toLowerCase();

  document.querySelectorAll('.doctor-card').forEach(card => {
      const name = card.dataset.name.toLowerCase();
      const specialty = card.dataset.specialty;
      const city = card.dataset.city?.toLowerCase() || '';
      
      const matchesSearch = name.includes(searchTerm) || 
                          specialty?.toLowerCase().includes(searchTerm);
      const matchesSpecialty = activeSpecialty === 'all' || specialty === activeSpecialty;
      const matchesCity = selectedCity === 'all' || city === selectedCity.toLowerCase();

      card.style.display = (matchesSearch && matchesSpecialty && matchesCity) ? 'block' : 'none';
  });
}

// Gestion des filtres - Version corrigée
function filterBySpecialty(specialty) {
  document.querySelectorAll('.filter-tag').forEach(tag => {
      tag.classList.remove('active');
      if(tag.dataset.specialty === specialty) {
          tag.classList.add('active');
      }
  });
  filterDoctors();
}

//test
function updateFilters() {
  const searchTerm = document.getElementById('globalSearch').value.toLowerCase();
  const selectedCity = document.getElementById('cityFilter').value.toLowerCase();
  const selectedSpecialty = document.getElementById('specialtyFilter').value.toLowerCase();

  document.querySelectorAll('.doctor-card').forEach(card => {
    const doctorName = card.dataset.name.toLowerCase();
    const doctorSpecialty = card.dataset.specialty.toLowerCase();
    const doctorCity = card.dataset.city.toLowerCase();

    const matchesSearch = doctorName.includes(searchTerm) || 
                         doctorSpecialty.includes(searchTerm) || 
                         doctorCity.includes(searchTerm);

    const matchesCity = selectedCity === 'all' || 
                       doctorCity === selectedCity;

    const matchesSpecialty = selectedSpecialty === 'all' || 
                            doctorSpecialty === selectedSpecialty;

    card.style.display = (matchesSearch && matchesCity && matchesSpecialty) ? 'block' : 'none';
  });
}

// Initialisation des filtres
document.addEventListener('DOMContentLoaded', () => {
  document.getElementById('globalSearch').value = '';
  document.getElementById('cityFilter').value = 'all';
  document.getElementById('specialtyFilter').value = 'all';
});
