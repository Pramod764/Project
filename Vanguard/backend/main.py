from fastapi import FastAPI, Depends, HTTPException, status, Form
from fastapi.middleware.cors import CORSMiddleware
from fastapi.staticfiles import StaticFiles
from sqlalchemy import create_engine, Column, Integer, String, Float, ForeignKey, Text
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker, Session, relationship
from pydantic import BaseModel
from typing import List
import os
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

# Database Setup
SQLALCHEMY_DATABASE_URL = os.getenv("DATABASE_URL", "sqlite:///./vanguard_store.db")
engine = create_engine(SQLALCHEMY_DATABASE_URL, connect_args={"check_same_thread": False} if "sqlite" in SQLALCHEMY_DATABASE_URL else {})
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()

# Models
class User(Base):
    __tablename__ = "users"
    id = Column(Integer, primary_key=True, index=True)
    username = Column(String, unique=True, index=True)
    email = Column(String, unique=True, index=True)
    hashed_password = Column(String)
    
    orders = relationship("Order", back_populates="user")

class Product(Base):
    __tablename__ = "products"
    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, index=True)
    description = Column(String)
    price = Column(Float)
    image_url = Column(String)

class Order(Base):
    __tablename__ = "orders"
    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"))
    total_price = Column(Float)
    shipping_address = Column(Text)
    phone_number = Column(String)
    status = Column(String, default="Pending")

    user = relationship("User", back_populates="orders")
    items = relationship("OrderItem", back_populates="order")

class OrderItem(Base):
    __tablename__ = "order_items"
    id = Column(Integer, primary_key=True, index=True)
    order_id = Column(Integer, ForeignKey("orders.id"))
    product_id = Column(Integer, ForeignKey("products.id"))
    quantity = Column(Integer)
    price = Column(Float)

    order = relationship("Order", back_populates="items")
    product = relationship("Product")

class OrderItemCreate(BaseModel):
    product_id: int
    quantity: int
    price: float

class OrderCreate(BaseModel):
    user_id: int
    total_price: float
    shipping_address: str
    phone_number: str
    items: List[OrderItemCreate]

class UserSignup(BaseModel):
    username: str
    email: str
    password: str

class UserLogin(BaseModel):
    username: str
    password: str

Base.metadata.create_all(bind=engine)

# FastAPI App
app = FastAPI(title="Vanguard Tech Store API")

# Enable CORS for frontend
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Dependency to get DB session
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

# Seed Products if empty
def seed_products():
    db = SessionLocal()
    if db.query(Product).count() == 0:
        products = [
            Product(name="Neural Pro Headphones", description="Adaptive noise cancellation with spatial audio.", price=299.99, image_url="https://images.unsplash.com/photo-1505740420928-5e560c06d30e?q=80&w=500"),
            Product(name="Vanguard Watch X", description="The ultimate health and fitness companion.", price=399.99, image_url="https://images.unsplash.com/photo-1523275335684-37898b6baf30?q=80&w=500"),
            Product(name="Lumina Laptop Pro", description="Unmatched performance with a stunning OLED display.", price=1299.99, image_url="https://images.unsplash.com/photo-1496181133206-80ce9b88a853?q=80&w=500"),
            Product(name="Horizon Smartphone 15", description="Capture your world in 8K resolution.", price=999.99, image_url="https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?q=80&w=500"),
        ]
        db.add_all(products)
        db.commit()
    db.close()

seed_products()

# Routes
@app.get("/api/products")
def get_products(db: Session = Depends(get_db)):
    return db.query(Product).all()

@app.post("/api/signup")
def signup(user: UserSignup, db: Session = Depends(get_db)):
    if db.query(User).filter(User.username == user.username).first():
        raise HTTPException(status_code=400, detail="Username already registered")
    new_user = User(username=user.username, email=user.email, hashed_password=user.password) # In real app, hash this!
    db.add(new_user)
    db.commit()
    return {"message": "User created successfully"}

@app.post("/api/login")
def login(user: UserLogin, db: Session = Depends(get_db)):
    db_user = db.query(User).filter(User.username == user.username, User.hashed_password == user.password).first()
    if not db_user:
        raise HTTPException(status_code=401, detail="Invalid credentials")
    return {"message": "Login successful", "user": {"id": db_user.id, "username": db_user.username, "email": db_user.email}}

@app.post("/api/orders")
def create_order(order: OrderCreate, db: Session = Depends(get_db)):
    # Create the main order
    new_order = Order(
        user_id=order.user_id, 
        total_price=order.total_price, 
        shipping_address=order.shipping_address,
        phone_number=order.phone_number
    )
    db.add(new_order)
    db.commit()
    db.refresh(new_order)

    # Create order items
    for item in order.items:
        order_item = OrderItem(
            order_id=new_order.id,
            product_id=item.product_id,
            quantity=item.quantity,
            price=item.price
        )
        db.add(order_item)
    
    db.commit()
    return {"message": "Order placed successfully", "order_id": new_order.id}

# Serve frontend
# Note: In production, you'd serve this via Nginx or similar
# app.mount("/", StaticFiles(directory="../frontend", html=True), name="frontend")

if __name__ == "__main__":
    import uvicorn
    host = os.getenv("HOST", "127.0.0.1")
    port = int(os.getenv("PORT", "8000"))
    uvicorn.run(app, host=host, port=port)
