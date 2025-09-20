// ========================================
// IMPORTS - External Dependencies
// ========================================
import React from 'react';                                        // React library for component creation

// ========================================
// IMPORTS - Styles
// ========================================
import './SweetCard.css';                                         // Component-specific styles

/**
 * ========================================
 * SWEET CARD COMPONENT
 * ========================================
 * 
 * This component displays individual sweet information in a card format.
 * It shows different actions based on user role:
 * - Regular users: Purchase button
 * - Admin users: Edit, Restock, and Delete buttons
 * 
 * FEATURES:
 * - Displays sweet information (name, category, price, quantity)
 * - Shows creation and update dates
 * - Handles out-of-stock state (quantity = 0)
 * - Role-based action buttons
 * - Responsive design
 * 
 * PROPS:
 * - sweet: Sweet object containing all sweet data
 * - onPurchase: Function to handle sweet purchase (regular users)
 * - onEdit: Function to handle sweet editing (admin users)
 * - onDelete: Function to handle sweet deletion (admin users)
 * - onRestock: Function to handle sweet restocking (admin users)
 * - isAdmin: Boolean indicating if current user is admin
 */
const SweetCard = ({ sweet, onPurchase, onEdit, onDelete, onRestock, isAdmin }) => {
  
  // ========================================
  // UTILITY FUNCTIONS
  // ========================================
  
  /**
   * Formats a date string into a readable format
   * 
   * @param {string} dateString - Date string from API (e.g., "2024-01-01 12:00:00")
   * @returns {string} Formatted date (e.g., "Jan 1, 2024")
   */
  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',    // Show full year (2024)
      month: 'short',     // Show abbreviated month (Jan)
      day: 'numeric'      // Show day number (1)
    });
  };

  /**
   * Formats a price number into Indian Rupee currency format
   * 
   * @param {number} price - Price value (e.g., 12.99)
   * @returns {string} Formatted price (e.g., "₹12.99")
   */
  const formatPrice = (price) => {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',    // Show as currency
      currency: 'INR'       // Indian Rupee
    }).format(price);
  };

  // ========================================
  // RENDER - Component JSX
  // ========================================
  return (
    <div className="sweet-card">
      
      {/* ========================================
          CARD HEADER - Name and Category
          ======================================== */}
      <div className="sweet-header">
        <h3 className="sweet-name">{sweet.name}</h3>                    {/* Sweet name as main heading */}
        <span className="sweet-category">{sweet.category}</span>        {/* Category as badge */}
      </div>
      
      {/* ========================================
          CARD DETAILS - Price and Quantity Info
          ======================================== */}
      <div className="sweet-details">
        
        {/* Price Information */}
        <div className="price-section">
          <span className="price-label">Price:</span>                   {/* Price label */}
          <span className="price-value">{formatPrice(sweet.price)}</span> {/* Formatted price */}
        </div>
        
        {/* Quantity Information */}
        <div className="quantity-section">
          <span className="quantity-label">Available:</span>             {/* Quantity label */}
          <span className={`quantity-value ${sweet.quantity > 0 ? 'in-stock' : 'out-of-stock'}`}>
            {sweet.quantity} units                                      {/* Quantity with stock status styling */}
          </span>
        </div>
        
      </div>
      
      {/* ========================================
          CARD FOOTER - Dates and Action Buttons
          ======================================== */}
      <div className="sweet-footer">
        
        {/* Date Information */}
        <div className="date-info">
          <small>Created: {formatDate(sweet.createdAt)}</small>         {/* Creation date */}
          <small>Updated: {formatDate(sweet.updatedAt)}</small>         {/* Last update date */}
        </div>
        
        {/* ========================================
            ACTION BUTTONS - Role-Based Actions
            ======================================== */}
        {isAdmin ? (
          /* Admin Actions - Edit, Restock, Delete */
          <div className="admin-actions">
            <button 
              className="edit-btn"
              onClick={() => onEdit(sweet)}                              // Call edit function with sweet data
              title="Edit Sweet"                                        // Tooltip text
            >
              ✏️ Edit                                                    {/* Edit button with icon */}
            </button>
            <button 
              className="restock-btn"
              onClick={() => onRestock(sweet)}                          // Call restock function with sweet data
              title="Restock Sweet"                                     // Tooltip text
            >
              📦 Restock                                                {/* Restock button with icon */}
            </button>
            <button 
              className="delete-btn"
              onClick={() => onDelete(sweet)}                           // Call delete function with sweet data
              title="Delete Sweet"                                      // Tooltip text
            >
              🗑️ Delete                                                 {/* Delete button with icon */}
            </button>
          </div>
        ) : (
          /* Regular User Action - Purchase */
          <button 
            className={`purchase-btn ${sweet.quantity === 0 ? 'disabled' : ''}`}  // Disabled styling if out of stock
            onClick={() => onPurchase(sweet)}                          // Call purchase function with sweet data
            disabled={sweet.quantity === 0}                            // Disable button if no stock
          >
            {sweet.quantity === 0 ? 'Out of Stock' : 'Purchase'}       {/* Dynamic button text */}
          </button>
        )}
        
      </div>
    </div>
  );
};

export default SweetCard;
