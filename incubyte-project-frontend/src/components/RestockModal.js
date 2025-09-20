import React, { useState, useEffect } from 'react';
import './RestockModal.css';

const RestockModal = ({ sweet, isOpen, onClose, onRestock, loading }) => {
  const [quantity, setQuantity] = useState(1);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (sweet) {
      setQuantity(1);
      setErrors({});
    }
  }, [sweet, isOpen]);

  const handleQuantityChange = (change) => {
    const newQuantity = quantity + change;
    if (newQuantity >= 1) {
      setQuantity(newQuantity);
      if (errors.quantity) {
        setErrors({});
      }
    }
  };

  const handleInputChange = (e) => {
    const value = parseInt(e.target.value) || 1;
    if (value >= 1) {
      setQuantity(value);
      if (errors.quantity) {
        setErrors({});
      }
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!quantity || quantity <= 0) {
      newErrors.quantity = 'Quantity must be greater than 0';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (validateForm()) {
      onRestock(sweet.id, quantity);
    }
  };

  if (!isOpen || !sweet) return null;

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
          <h3>Restock {sweet.name}</h3>
          <button className="close-btn" onClick={onClose}>×</button>
        </div>
        
        <div className="modal-body">
          <div className="sweet-info">
            <p><strong>Category:</strong> {sweet.category}</p>
            <p><strong>Price per unit:</strong> {formatPrice(sweet.price)}</p>
            <p><strong>Current quantity:</strong> {sweet.quantity} units</p>
          </div>
          
          <form onSubmit={handleSubmit} className="restock-form">
            <div className="quantity-selector">
              <label htmlFor="quantity">Add Quantity:</label>
              <div className="quantity-controls">
                <button 
                  type="button"
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
                  onChange={handleInputChange}
                  min="1"
                  className={`quantity-input ${errors.quantity ? 'error' : ''}`}
                />
                <button 
                  type="button"
                  className="quantity-btn plus" 
                  onClick={() => handleQuantityChange(1)}
                >
                  +
                </button>
              </div>
              {errors.quantity && <span className="error-message">{errors.quantity}</span>}
            </div>
            
            <div className="quantity-preview">
              <p><strong>New total quantity:</strong> {sweet.quantity + quantity} units</p>
            </div>
          </form>
        </div>
        
        <div className="modal-footer">
          <button className="cancel-btn" onClick={onClose} disabled={loading}>
            Cancel
          </button>
          <button 
            className="restock-btn" 
            onClick={handleSubmit}
            disabled={loading}
          >
            {loading ? 'Restocking...' : `Restock ${quantity} ${quantity === 1 ? 'unit' : 'units'}`}
          </button>
        </div>
      </div>
    </div>
  );
};

export default RestockModal;
