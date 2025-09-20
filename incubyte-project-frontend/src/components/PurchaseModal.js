import React, { useState } from 'react';
import './PurchaseModal.css';

const PurchaseModal = ({ sweet, isOpen, onClose, onPurchase }) => {
  const [quantity, setQuantity] = useState(1);

  if (!isOpen || !sweet) return null;

  const handleQuantityChange = (change) => {
    const newQuantity = quantity + change;
    if (newQuantity >= 1 && newQuantity <= sweet.quantity) {
      setQuantity(newQuantity);
    }
  };

  const handlePurchase = () => {
    onPurchase(sweet.id, quantity);
    setQuantity(1); // Reset quantity
    onClose();
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: 'INR'
    }).format(price);
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h3>Purchase {sweet.name}</h3>
          <button className="close-btn" onClick={onClose}>×</button>
        </div>
        
        <div className="modal-body">
          <div className="sweet-info">
            <p><strong>Category:</strong> {sweet.category}</p>
            <p><strong>Price per unit:</strong> {formatPrice(sweet.price)}</p>
            <p><strong>Available quantity:</strong> {sweet.quantity} units</p>
          </div>
          
          <div className="quantity-selector">
            <label htmlFor="quantity">Select Quantity:</label>
            <div className="quantity-controls">
              <button 
                className="quantity-btn minus" 
                onClick={() => handleQuantityChange(-1)}
                disabled={quantity <= 1}
              >
                -
              </button>
              <input
                type="number"
                id="quantity"
                value={quantity}
                onChange={(e) => {
                  const value = parseInt(e.target.value) || 1;
                  if (value >= 1 && value <= sweet.quantity) {
                    setQuantity(value);
                  }
                }}
                min="1"
                max={sweet.quantity}
                className="quantity-input"
              />
              <button 
                className="quantity-btn plus" 
                onClick={() => handleQuantityChange(1)}
                disabled={quantity >= sweet.quantity}
              >
                +
              </button>
            </div>
          </div>
          
          <div className="total-price">
            <strong>Total: {formatPrice(sweet.price * quantity)}</strong>
          </div>
        </div>
        
        <div className="modal-footer">
          <button className="cancel-btn" onClick={onClose}>
            Cancel
          </button>
          <button className="purchase-btn" onClick={handlePurchase}>
            Purchase {quantity} {quantity === 1 ? 'unit' : 'units'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default PurchaseModal;
