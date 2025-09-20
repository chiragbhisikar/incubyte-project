import React, { useState } from 'react';
import './SearchForm.css';

const SearchForm = ({ onSearch, onClear, loading }) => {
  const [searchParams, setSearchParams] = useState({
    name: '',
    category: '',
    minPrice: '',
    maxPrice: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setSearchParams(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    // Filter out empty values and trim whitespace
    const filteredParams = Object.fromEntries(
      Object.entries(searchParams).filter(([_, value]) => value && value.trim() !== '')
    );
    
    console.log('Search parameters:', filteredParams); // Debug log
    console.log('Raw search params:', searchParams); // Debug log
    
    if (Object.keys(filteredParams).length === 0) {
      console.log('No search parameters provided');
      return;
    }
    
    onSearch(filteredParams);
  };

  const handleClear = () => {
    setSearchParams({
      name: '',
      category: '',
      minPrice: '',
      maxPrice: ''
    });
    onClear();
  };

  return (
    <div className="search-form-container">
      <h3>Search & Filter Sweets</h3>
      <form onSubmit={handleSubmit} className="search-form">
        <div className="search-row">
          <div className="search-field">
            <label htmlFor="name">Name</label>
            <input
              type="text"
              id="name"
              name="name"
              value={searchParams.name}
              onChange={handleChange}
              placeholder="Search by sweet name"
            />
          </div>

          <div className="search-field">
            <label htmlFor="category">Category</label>
            <input
              type="text"
              id="category"
              name="category"
              value={searchParams.category}
              onChange={handleChange}
              placeholder="Filter by category"
            />
          </div>

          <div className="search-field">
            <label htmlFor="minPrice">Min Price (₹)</label>
            <input
              type="number"
              id="minPrice"
              name="minPrice"
              value={searchParams.minPrice}
              onChange={handleChange}
              placeholder="Minimum price"
              min="0"
              step="0.01"
            />
          </div>

          <div className="search-field">
            <label htmlFor="maxPrice">Max Price (₹)</label>
            <input
              type="number"
              id="maxPrice"
              name="maxPrice"
              value={searchParams.maxPrice}
              onChange={handleChange}
              placeholder="Maximum price"
              min="0"
              step="0.01"
            />
          </div>
        </div>

        <div className="search-actions">
          <button type="submit" className="search-btn" disabled={loading}>
            {loading ? 'Searching...' : 'Search'}
          </button>
          <button type="button" className="clear-btn" onClick={handleClear} disabled={loading}>
            Clear
          </button>
        </div>
      </form>
    </div>
  );
};

export default SearchForm;
