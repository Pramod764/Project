const express = require('express');
const cors = require('cors');
const app = express();
const port = 8000;

app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

const products = [
    { id: 1, name: "Neural Pro Headphones", description: "Adaptive noise cancellation with spatial audio.", price: 299.99, image_url: "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=500" },
    { id: 2, name: "Vanguard Watch X", description: "The ultimate health and fitness companion.", price: 399.99, image_url: "https://images.unsplash.com/photo-1523275335684-37898b6baf30?q=80&w=500" },
    { id: 3, name: "Lumina Laptop Pro", description: "Unmatched performance with a stunning OLED display.", price: 1299.99, image_url: "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?q=80&w=500" },
    { id: 4, name: "Horizon Smartphone 15", description: "Capture your world in 8K resolution.", price: 999.99, image_url: "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?q=80&w=500" },
];

app.get('/api/products', (req, res) => {
    res.json(products);
});

app.post('/api/signup', (req, res) => {
    const { username, email, password } = req.body;
    console.log("Signup attempt:", { username, email });
    res.json({ message: "User created successfully" });
});

app.post('/api/login', (req, res) => {
    const { username, password } = req.body;
    console.log("Login attempt:", { username });
    res.json({ message: "Login successful", user: { id: 1, username: username || "Guest", email: "user@example.com" } });
});

app.post('/api/orders', (req, res) => {
    const { user_id, total_price, shipping_address, phone_number, items } = req.body;
    console.log("Order received:", { user_id, total_price, phone_number, itemCount: items.length });
    res.json({ message: "Order placed successfully", order_id: Math.floor(Math.random() * 10000) });
});

app.listen(port, () => {
    console.log(`Backend fix running at http://localhost:${port}`);
});
