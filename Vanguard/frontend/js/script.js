const API_BASE = "http://localhost:8000/api";
let products = [];
let cart = JSON.parse(localStorage.getItem('vanguard_cart')) || [];
let currentUser = JSON.parse(localStorage.getItem('vanguard_user')) || null;

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    fetchProducts();
    updateCartUI();
    checkAuthUI();

    // Scroll effect for navbar
    window.addEventListener('scroll', () => {
        const nav = document.getElementById('navbar');
        if (nav) {
            if (window.scrollY > 50) {
                nav.classList.add('scrolled');
            } else {
                nav.classList.remove('scrolled');
            }
        }
    });

    // Form Handlers
    document.getElementById('signupForm').addEventListener('submit', handleSignup);
    document.getElementById('loginForm').addEventListener('submit', handleLogin);

    // Hero Buttons
    const browseBtn = document.getElementById('hero-browse');
    const promoBtn = document.getElementById('hero-promo');

    if (browseBtn) {
        browseBtn.addEventListener('click', () => {
            const section = document.getElementById('products-section');
            if (section) section.scrollIntoView({ behavior: 'smooth' });
        });
    }

    if (promoBtn) {
        promoBtn.addEventListener('click', () => {
            alert('The Vanguard Promo is coming soon to your neural link! 🚀');
        });
    }
});

// UI Logic
function openModal(id) {
    document.getElementById(id).style.display = 'flex';
}

function closeModal(id) {
    document.getElementById(id).style.display = 'none';
}

function checkAuthUI() {
    const userInfo = document.getElementById('user-info');
    const guestBtns = document.getElementById('guest-btns');
    const displayName = document.getElementById('display-name');

    if (currentUser) {
        userInfo.style.display = 'flex';
        guestBtns.style.display = 'none';
        displayName.innerText = currentUser.username;
    } else {
        userInfo.style.display = 'none';
        guestBtns.style.display = 'flex';
    }
}

// Data Fetching
async function fetchProducts() {
    try {
        const response = await fetch(`${API_BASE}/products`);
        products = await response.json();
        renderProducts();
    } catch (error) {
        console.error("Error fetching products:", error);
        document.getElementById('products-container').innerHTML = `<p style="color: #ef4444;">Failed to connect to the backend server. Please ensure it is running.</p>`;
    }
}

function renderProducts() {
    const container = document.getElementById('products-container');
    container.innerHTML = products.map(product => `
        <div class="product-card glass">
            <img src="${product.image_url}" alt="${product.name}" class="product-img">
            <div class="product-info">
                <h3>${product.name}</h3>
                <p>${product.description}</p>
                <div class="product-footer">
                    <span class="price">$${product.price}</span>
                    <button class="btn btn-primary" onclick="addToCart(${product.id})">Add to Cart</button>
                </div>
            </div>
        </div>
    `).join('');
}

// Auth Handlers
async function handleSignup(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = Object.fromEntries(formData.entries());
    try {
        const response = await fetch(`${API_BASE}/signup`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        const result = await response.json();
        if (response.ok) {
            alert("Account created successfully! Please login.");
            closeModal('signupModal');
            openModal('loginModal');
        } else {
            alert(result.detail || "Signup failed");
        }
    } catch (error) {
        alert("Server error during signup.");
    }
}

async function handleLogin(e) {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = Object.fromEntries(formData.entries());
    try {
        const response = await fetch(`${API_BASE}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        const result = await response.json();
        if (response.ok) {
            currentUser = result.user;
            localStorage.setItem('vanguard_user', JSON.stringify(currentUser));
            checkAuthUI();
            closeModal('loginModal');
        } else {
            alert(result.detail || "Login failed");
        }
    } catch (error) {
        alert("Server error during login.");
    }
}

function logout() {
    currentUser = null;
    localStorage.removeItem('vanguard_user');
    checkAuthUI();
}

// Cart Logic
function addToCart(productId) {
    console.log("Adding product to cart, ID:", productId);
    const product = products.find(p => p.id === productId);
    if (!product) {
        console.error("Product not found in local products array!");
        return;
    }
    
    const existing = cart.find(item => item.id === productId);
    
    if (existing) {
        existing.quantity += 1;
    } else {
        cart.push({ ...product, quantity: 1 });
    }
    
    saveCart();
    updateCartUI();
    
    // Show Toast Notification
    const toast = document.getElementById('toast');
    toast.style.display = 'block';
    setTimeout(() => toast.style.display = 'none', 3000);
    
    console.log("Cart updated:", cart);
    
    // Quick notification animation
    const count = document.getElementById('cart-count');
    count.style.transform = 'scale(1.5)';
    setTimeout(() => count.style.transform = 'scale(1)', 200);
}

function removeFromCart(productId) {
    cart = cart.filter(item => item.id !== productId);
    saveCart();
    updateCartUI();
}

function saveCart() {
    localStorage.setItem('vanguard_cart', JSON.stringify(cart));
}

function updateCartUI() {
    const list = document.getElementById('cart-items');
    const footer = document.getElementById('cart-footer');
    const totalEl = document.getElementById('cart-total');
    const countEl = document.getElementById('cart-count');

    countEl.innerText = cart.length;

    if (cart.length === 0) {
        list.innerHTML = `<p style="color: var(--text-muted); text-align: center;">Cart is empty.</p>`;
        footer.style.display = 'none';
        return;
    }

    footer.style.display = 'block';
    let total = 0;
    list.innerHTML = cart.map(item => {
        total += item.price * item.quantity;
        return `
            <div style="display: flex; gap: 1rem; align-items: center; margin-bottom: 1rem; border-bottom: 1px solid var(--glass-border); padding-bottom: 0.5rem;">
                <img src="${item.image_url}" style="width: 50px; height: 50px; border-radius: 8px; object-fit: cover;">
                <div style="flex-grow: 1;">
                    <div style="font-weight: 600;">${item.name}</div>
                    <div style="font-size: 0.8rem; color: var(--text-muted);">$${item.price} x ${item.quantity}</div>
                </div>
                <button onclick="removeFromCart(${item.id})" style="background: none; border: none; color: #ef4444; cursor: pointer;">&times;</button>
            </div>
        `;
    }).join('');
    
    totalEl.innerText = `$${total.toFixed(2)}`;
}

async function checkout() {
    window.location.href = 'checkout.html';
}
