// ========================================
// IMPORTS - External Dependencies
// ========================================
import React, { useState, useEffect, useCallback } from "react"; // React hooks for state management
import { useAuth } from "../contexts/AuthContext"; // Authentication context
import { useNavigate } from "react-router-dom"; // Navigation hook
import axios from "axios"; // HTTP client for API calls

// ========================================
// IMPORTS - Internal Components
// ========================================
import SweetCard from "./SweetCard"; // Component to display individual sweets
import SearchForm from "./SearchForm"; // Component for search and filter
import PurchaseModal from "./PurchaseModal"; // Modal for purchasing sweets
import SweetForm from "./SweetForm"; // Modal for adding/editing sweets
import RestockModal from "./RestockModal"; // Modal for restocking sweets

// ========================================
// IMPORTS - Styles
// ========================================
import "./Dashboard.css"; // Component-specific styles

/**
 * ========================================
 * DASHBOARD COMPONENT
 * ========================================
 *
 * This is the main dashboard component that displays:
 * - List of all sweets
 * - Search and filter functionality
 * - Purchase functionality (for regular users)
 * - Admin functionality (add, edit, delete, restock sweets)
 *
 * FEATURES:
 * - Sweet listing with filtering (all/available)
 * - Search by name, category, price range
 * - Purchase sweets with quantity selection
 * - Admin CRUD operations (Create, Read, Update, Delete)
 * - Admin restock functionality
 * - Debug tools for API testing
 *
 * PROPS:
 * - onShowNotification: Function to display notifications
 */
const Dashboard = ({ onShowNotification }) => {
  // ========================================
  // STATE MANAGEMENT - Data States
  // ========================================
  const [sweets, setSweets] = useState([]); // Array of sweet objects from API
  const [loading, setLoading] = useState(true); // Loading state for initial data fetch
  const [error, setError] = useState(null); // Error message if API calls fail

  // ========================================
  // STATE MANAGEMENT - Filter & Search States
  // ========================================
  const [filter, setFilter] = useState("all"); // Filter: 'all', 'available', or 'not-available'
  const [searchParams, setSearchParams] = useState({}); // Current search parameters
  const [isSearching, setIsSearching] = useState(false); // Loading state for search operations

  // ========================================
  // STATE MANAGEMENT - Modal States
  // ========================================
  const [purchaseModal, setPurchaseModal] = useState({
    // Purchase modal state
    isOpen: false, // Whether modal is open
    sweet: null, // Sweet object being purchased
  });
  const [sweetForm, setSweetForm] = useState({
    // Add/Edit sweet modal state
    isOpen: false, // Whether modal is open
    sweet: null, // Sweet object being edited (null for new)
  });
  const [restockModal, setRestockModal] = useState({
    // Restock modal state
    isOpen: false, // Whether modal is open
    sweet: null, // Sweet object being restocked
  });

  // ========================================
  // STATE MANAGEMENT - Loading States
  // ========================================
  const [isLoading, setIsLoading] = useState(false); // Loading state for admin operations

  // ========================================
  // HOOKS - Authentication & Navigation
  // ========================================
  const { user, logout, isAuthenticated } = useAuth(); // Get user data and auth functions
  const navigate = useNavigate(); // Navigation function for routing

  // ========================================
  // COMPUTED VALUES - User Permissions
  // ========================================
  const isAdmin = user?.roles?.includes("ADMIN"); // Check if current user has admin role

  // ========================================
  // API FUNCTIONS - Fetch Sweets
  // ========================================

  /**
   * Fetches sweets from the API based on current filter
   *
   * API ENDPOINTS:
   * - GET /api/sweets - Get all sweets (requires JWT token)
   * - GET /api/sweets/available - Get only available sweets (quantity > 0, requires JWT token)
   *
   * AUTHENTICATION:
   * - Both endpoints require JWT token in Authorization header
   * - Token is automatically added by axios interceptor
   * - Format: "Authorization: Bearer <jwt_token>"
   *
   * RESPONSE FORMAT (Available Sweets API):
   * {
   *   "message": "Sweets retrieved successfully",
   *   "data": [
   *     {
   *       "id": "059fb0f7-99cd-4aac-bb34-baa7c9fc3032",
   *       "name": "Jalebi",
   *       "category": "Milk",
   *       "price": 130.07,
   *       "quantity": 635,
   *       "createdAt": "2024-10-18 11:08:23",
   *       "updatedAt": "2025-07-17 07:14:10"
   *     }
   *   ]
   * }
   *
   * FILTER BEHAVIOR:
   * - 'all': Shows all sweets regardless of quantity (including out-of-stock)
   * - 'available': Shows only sweets with quantity > 0 (in stock)
   * - 'not-available': Shows only sweets with quantity = 0 (out of stock)
   *
   * NOTE: The "available" endpoint returns sweets that are NOT out of stock
   */
  const fetchSweets = useCallback(async () => {
    try {
      // ========================================
      // SETUP - Loading and Error States
      // ========================================
      setLoading(true); // Show loading spinner
      setError(null); // Clear any previous errors

      // ========================================
      // API CALL - Determine Endpoint
      // ========================================
      let url = "http://localhost:8080/api/sweets"; // Default: get all sweets (includes out-of-stock)
      if (filter === "available") {
        url = "http://localhost:8080/api/sweets/available"; // Only sweets with quantity > 0 (in stock)
      }
      // Note: For 'not-available' filter, we use the same endpoint as 'all' and filter client-side

      console.log("🌐 Fetching sweets from:", url); // Debug: log the API endpoint
      console.log("🔐 JWT Token attached:", !!localStorage.getItem("token")); // Debug: check if token exists
      console.log(
        "📊 Filter type:",
        filter === "available"
          ? "Available Only (quantity > 0)"
          : filter === "not-available"
          ? "Not Available Only (quantity = 0)"
          : "All Sweets (including out-of-stock)"
      );

      // ========================================
      // API CALL - Make Request
      // ========================================
      const response = await axios.get(url); // GET request with JWT token (auto-added by interceptor)
      console.log(
        "✅ Sweets fetched successfully:",
        response.data.data.length,
        "items"
      ); // Debug: log success

      // ========================================
      // CLIENT-SIDE FILTERING - Handle Not Available Filter
      // ========================================
      let filteredSweets = response.data.data; // Get all sweets from API response

      if (filter === "not-available") {
        // Filter to show only sweets with quantity = 0 (out of stock)
        filteredSweets = response.data.data.filter(
          (sweet) => sweet.quantity === 0
        );
        console.log(
          "🔍 Filtered to not-available sweets:",
          filteredSweets.length,
          "out of stock items"
        );
      }

      setSweets(filteredSweets); // Update sweets state with filtered results
    } catch (error) {
      // ========================================
      // ERROR HANDLING
      // ========================================
      setError("Failed to fetch sweets. Please try again."); // Set error message for UI
      onShowNotification("Failed to fetch sweets", "error"); // Show error notification
    } finally {
      // ========================================
      // CLEANUP - Always Hide Loading
      // ========================================
      setLoading(false); // Hide loading spinner
    }
  }, [filter, onShowNotification]); // Dependencies: re-run when filter changes

  /**
   * Searches sweets based on provided parameters
   *
   * API ENDPOINT: GET /api/sweets/search
   *
   * SEARCH PARAMETERS:
   * - name: Search by sweet name (partial match)
   * - category: Filter by category (exact match)
   * - minPrice: Minimum price filter
   * - maxPrice: Maximum price filter
   *
   * EXAMPLE URL:
   * /api/sweets/search?name=Cake&category=Festival&minPrice=200&maxPrice=400
   *
   * @param {Object} params - Search parameters object
   * @param {string} params.name - Sweet name to search for
   * @param {string} params.category - Category to filter by
   * @param {string} params.minPrice - Minimum price
   * @param {string} params.maxPrice - Maximum price
   */
  const searchSweets = useCallback(
    async (params) => {
      console.log("🔍 Search function called with params:", params);

      try {
        // ========================================
        // SETUP - Loading and Error States
        // ========================================
        setIsSearching(true); // Show search loading state
        setError(null); // Clear any previous errors

        // ========================================
        // QUERY BUILDING - Manual URL Construction
        // ========================================
        // We build the query string manually to ensure proper formatting
        // This prevents issues with axios params object
        const queryParams = new URLSearchParams();

        // Add each parameter if it has a value
        if (params.name && params.name.trim()) {
          queryParams.append("name", params.name.trim()); // Search by name (case-insensitive)
        }
        if (params.category && params.category.trim()) {
          queryParams.append("category", params.category.trim()); // Filter by category
        }
        if (params.minPrice && params.minPrice.trim()) {
          queryParams.append("minPrice", params.minPrice.trim()); // Minimum price filter
        }
        if (params.maxPrice && params.maxPrice.trim()) {
          queryParams.append("maxPrice", params.maxPrice.trim()); // Maximum price filter
        }

        // ========================================
        // URL CONSTRUCTION
        // ========================================
        const queryString = queryParams.toString(); // Convert to query string
        const url = `http://localhost:8080/api/sweets/search${
          queryString ? `?${queryString}` : ""
        }`;

        console.log("🌐 Search URL:", url); // Debug: log the constructed URL
        console.log("📋 Query params:", queryParams.toString()); // Debug: log query parameters

        // ========================================
        // API CALL - Make Search Request
        // ========================================
        const response = await axios.get(url); // GET request with JWT token

        console.log("✅ Search response:", response.data); // Debug: log API response

        // ========================================
        // SUCCESS HANDLING
        // ========================================
        setSweets(response.data.data); // Update sweets with search results
        setSearchParams(params); // Store current search parameters
        onShowNotification("Search completed successfully", "success"); // Show success notification
      } catch (error) {
        // ========================================
        // ERROR HANDLING - Logging
        // ========================================
        console.error("❌ Search error:", error); // Debug: log error details
        console.error("📄 Error response:", error.response); // Debug: log error response

        // ========================================
        // ERROR HANDLING - JWT Token Expiration
        // ========================================
        if (error.response?.status === 401) {
          const errorMessage = error.response?.data?.error || "";
          const statusCode = error.response?.data?.statusCode || "";

          // Check for JWT-related errors
          if (
            errorMessage.includes("JWT expired") ||
            errorMessage.includes("Invalid JWT token") ||
            errorMessage.includes("JWT") ||
            statusCode === "UNAUTHORIZED"
          ) {
            console.log(
              "🔐 JWT Token expired, letting AuthContext handle logout"
            );
            return; // Let AuthContext handle the logout automatically
          }
        }

        // ========================================
        // ERROR HANDLING - General Errors
        // ========================================
        setError("Failed to search sweets. Please try again."); // Set error message for UI
        onShowNotification("Search failed", "error"); // Show error notification
      } finally {
        // ========================================
        // CLEANUP - Always Hide Loading
        // ========================================
        setIsSearching(false); // Hide search loading state
      }
    },
    [onShowNotification]
  ); // Dependencies: re-run when notification function changes

  const clearSearch = useCallback(async () => {
    setSearchParams({});
    try {
      setLoading(true);
      setError(null);

      let url = "http://localhost:8080/api/sweets";
      if (filter === "available") {
        url = "http://localhost:8080/api/sweets/available";
      }

      const response = await axios.get(url);
      setSweets(response.data.data);
    } catch (error) {
      if (error.response?.status === 401) {
        const errorMessage = error.response?.data?.error || "";
        const statusCode = error.response?.data?.statusCode || "";

        if (
          errorMessage.includes("JWT expired") ||
          errorMessage.includes("Invalid JWT token") ||
          errorMessage.includes("JWT") ||
          statusCode === "UNAUTHORIZED"
        ) {
          // Token expired, let the AuthContext handle the logout
          console.log("JWT Token expired, letting AuthContext handle logout");
          return;
        }
      }
      setError("Failed to fetch sweets. Please try again.");
      onShowNotification("Failed to fetch sweets", "error");
    } finally {
      setLoading(false);
    }
  }, [filter, onShowNotification]);

  useEffect(() => {
    if (!isAuthenticated()) {
      navigate("/login");
      return;
    }

    const loadSweets = async () => {
      try {
        setLoading(true);
        setError(null);

        let url = "http://localhost:8080/api/sweets";
        
        if (filter === "available") {
          url = "http://localhost:8080/api/sweets/available";
        }

        const response = await axios.get(url);
        setSweets(response.data.data);
      } catch (error) {
        if (error.response?.status === 401) {
          const errorMessage = error.response?.data?.error || "";
          if (
            errorMessage.includes("JWT expired") ||
            errorMessage.includes("Invalid JWT token")
          ) {
            // Token expired, let the AuthContext handle the logout
            logout();
          }
        }
        setError("Failed to fetch sweets. Please try again.");
        onShowNotification("Failed to fetch sweets", "error");
      } finally {
        setLoading(false);
      }
    };

    loadSweets();
  }, [navigate, isAuthenticated, filter, onShowNotification]);

  const handleLogout = () => {
    logout();
    onShowNotification("Logged out successfully", "success");
    navigate("/login");
  };

  const handleFilterChange = (newFilter) => {
    setFilter(newFilter);
    // Clear search when changing filter
    if (Object.keys(searchParams).length > 0) {
      setSearchParams({});
    }
  };

  const handlePurchaseClick = (sweet) => {
    setPurchaseModal({ isOpen: true, sweet });
  };

  const handlePurchase = async (sweetId, quantity) => {
    try {
      console.log("Purchase request:", {
        url: `http://localhost:8080/api/sweets/${sweetId}/purchase`,
        data: { quantity: quantity },
        headers: axios.defaults.headers.common,
      });

      const response = await axios.post(
        `http://localhost:8080/api/sweets/${sweetId}/purchase`,
        {
          quantity: quantity,
        },
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
          withCredentials: true,
        }
      );

      console.log("Purchase response:", response.data);
      onShowNotification(
        `Successfully purchased ${quantity} units!`,
        "success"
      );

      // Refresh the sweets list to update quantities
      if (Object.keys(searchParams).length > 0) {
        // If we're in search mode, refresh search results
        const filteredParams = Object.fromEntries(
          Object.entries(searchParams).filter(
            ([_, value]) => value && value.trim() !== ""
          )
        );
        await searchSweets(filteredParams);
      } else {
        // Otherwise refresh the main list
        await fetchSweets();
      }
    } catch (error) {
      console.error("Purchase error:", error);
      console.error("Error response:", error.response);
      console.error("Error message:", error.message);
      console.error("Error config:", error.config);

      if (error.response?.status === 401) {
        const errorMessage = error.response?.data?.error || "";
        const statusCode = error.response?.data?.statusCode || "";

        if (
          errorMessage.includes("JWT expired") ||
          errorMessage.includes("Invalid JWT token") ||
          errorMessage.includes("JWT") ||
          statusCode === "UNAUTHORIZED"
        ) {
          // Token expired, let the AuthContext handle the logout
          console.log("JWT Token expired, letting AuthContext handle logout");
          return;
        }
      }

      // Handle CORS errors
      if (
        error.message.includes("CORS") ||
        error.message.includes("Network Error")
      ) {
        onShowNotification(
          "CORS Error: Please check your backend CORS configuration",
          "error"
        );
        return;
      }

      const errorMessage =
        error.response?.data?.error ||
        error.message ||
        "Purchase failed. Please try again.";
      onShowNotification(errorMessage, "error");
    }
  };

  const closePurchaseModal = () => {
    setPurchaseModal({ isOpen: false, sweet: null });
  };

  // Admin functions
  const handleAddSweet = () => {
    setSweetForm({ isOpen: true, sweet: null });
  };

  const handleEditSweet = (sweet) => {
    setSweetForm({ isOpen: true, sweet });
  };

  const handleDeleteSweet = async (sweet) => {
    if (
      window.confirm(
        `Are you sure you want to delete "${sweet.name}"? This action cannot be undone.`
      )
    ) {
      try {
        setIsLoading(true);
        await axios.delete(`http://localhost:8080/api/sweets/${sweet.id}`);

        onShowNotification(
          `Sweet "${sweet.name}" deleted successfully`,
          "success"
        );

        // Refresh the sweets list
        if (Object.keys(searchParams).length > 0) {
          const filteredParams = Object.fromEntries(
            Object.entries(searchParams).filter(
              ([_, value]) => value && value.trim() !== ""
            )
          );
          await searchSweets(filteredParams);
        } else {
          await fetchSweets();
        }
      } catch (error) {
        console.error("Delete error:", error);

        if (error.response?.status === 401) {
          const errorMessage = error.response?.data?.error || "";
          const statusCode = error.response?.data?.statusCode || "";

          if (
            errorMessage.includes("JWT expired") ||
            errorMessage.includes("Invalid JWT token") ||
            errorMessage.includes("JWT") ||
            statusCode === "UNAUTHORIZED"
          ) {
            console.log("JWT Token expired, letting AuthContext handle logout");
            return;
          }
        }

        const errorMessage =
          error.response?.data?.error ||
          "Failed to delete sweet. Please try again.";
        onShowNotification(errorMessage, "error");
      } finally {
        setIsLoading(false);
      }
    }
  };

  const handleSweetSubmit = async (sweetData) => {
    try {
      setIsLoading(true);

      if (sweetForm.sweet) {
        // Update existing sweet
        await axios.put(
          `http://localhost:8080/api/sweets/${sweetForm.sweet.id}`,
          sweetData
        );
        onShowNotification(
          `Sweet "${sweetData.name}" updated successfully`,
          "success"
        );
      } else {
        // Add new sweet
        await axios.post("http://localhost:8080/api/sweets", sweetData);
        onShowNotification(
          `Sweet "${sweetData.name}" added successfully`,
          "success"
        );
      }

      setSweetForm({ isOpen: false, sweet: null });

      // Refresh the sweets list
      if (Object.keys(searchParams).length > 0) {
        const filteredParams = Object.fromEntries(
          Object.entries(searchParams).filter(
            ([_, value]) => value && value.trim() !== ""
          )
        );
        await searchSweets(filteredParams);
      } else {
        await fetchSweets();
      }
    } catch (error) {
      console.error("Sweet submit error:", error);

      if (error.response?.status === 401) {
        const errorMessage = error.response?.data?.error || "";
        const statusCode = error.response?.data?.statusCode || "";

        if (
          errorMessage.includes("JWT expired") ||
          errorMessage.includes("Invalid JWT token") ||
          errorMessage.includes("JWT") ||
          statusCode === "UNAUTHORIZED"
        ) {
          console.log("JWT Token expired, letting AuthContext handle logout");
          return;
        }
      }

      const errorMessage =
        error.response?.data?.error ||
        "Failed to save sweet. Please try again.";
      onShowNotification(errorMessage, "error");
    } finally {
      setIsLoading(false);
    }
  };

  const closeSweetForm = () => {
    setSweetForm({ isOpen: false, sweet: null });
  };

  // Restock functions
  const handleRestockClick = (sweet) => {
    setRestockModal({ isOpen: true, sweet });
  };

  const handleRestock = async (sweetId, quantity) => {
    try {
      setIsLoading(true);
      console.log("Restock request:", {
        url: `http://localhost:8080/api/sweets/${sweetId}/restock`,
        data: { quantity: quantity },
        headers: axios.defaults.headers.common,
      });

      const response = await axios.post(
        `http://localhost:8080/api/sweets/${sweetId}/restock`,
        {
          quantity: quantity,
        },
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      console.log("Restock response:", response.data);
      onShowNotification(
        `Successfully restocked ${quantity} units!`,
        "success"
      );
      setRestockModal({ isOpen: false, sweet: null });

      // Refresh the sweets list
      if (Object.keys(searchParams).length > 0) {
        const filteredParams = Object.fromEntries(
          Object.entries(searchParams).filter(
            ([_, value]) => value && value.trim() !== ""
          )
        );
        await searchSweets(filteredParams);
      } else {
        await fetchSweets();
      }
    } catch (error) {
      console.error("Restock error:", error);
      console.error("Error response:", error.response);
      console.error("Error message:", error.message);
      console.error("Error config:", error.config);

      if (error.response?.status === 401) {
        const errorMessage = error.response?.data?.error || "";
        const statusCode = error.response?.data?.statusCode || "";

        if (
          errorMessage.includes("JWT expired") ||
          errorMessage.includes("Invalid JWT token") ||
          errorMessage.includes("JWT") ||
          statusCode === "UNAUTHORIZED"
        ) {
          console.log("JWT Token expired, letting AuthContext handle logout");
          return;
        }
      }

      // Handle CORS errors
      if (
        error.message.includes("CORS") ||
        error.message.includes("Network Error")
      ) {
        onShowNotification(
          "CORS Error: Please check your backend CORS configuration",
          "error"
        );
        return;
      }

      const errorMessage =
        error.response?.data?.error ||
        error.message ||
        "Failed to restock sweet. Please try again.";
      onShowNotification(errorMessage, "error");
    } finally {
      setIsLoading(false);
    }
  };

  const closeRestockModal = () => {
    setRestockModal({ isOpen: false, sweet: null });
  };

  // ========================================
  // DEBUG FUNCTIONS - API Testing
  // ========================================

  if (loading) {
    return (
      <div className="dashboard">
        <div className="loading">
          <div className="spinner"></div>
          <p>Loading sweets...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <div className="header-content">
          <h1>Sweet Shop Dashboard</h1>
          <div className="header-actions">
            {isAdmin && (
              <button onClick={handleAddSweet} className="add-sweet-btn">
                ➕ Add Sweet
              </button>
            )}

            <div className="user-info">
              <span>Welcome, {user?.username}</span>
              {isAdmin && <span className="admin-badge">👑 Admin</span>}
              <button onClick={handleLogout} className="logout-btn">
                Logout
              </button>
            </div>
          </div>
        </div>
      </header>

      <div className="dashboard-content">
        <SearchForm
          onSearch={searchSweets}
          onClear={clearSearch}
          loading={isSearching}
        />

        <div className="filter-section">
          <h2>
            {Object.keys(searchParams).length > 0
              ? "Search Results"
              : "Available Sweets"}
            {Object.keys(searchParams).length > 0 && (
              <span className="search-indicator"> (Search Active)</span>
            )}
          </h2>
          <div className="filter-buttons">
            <button
              className={`filter-btn ${filter === "all" ? "active" : ""}`}
              onClick={() => handleFilterChange("all")}
            >
              All Sweets
            </button>
            <button
              className={`filter-btn ${filter === "available" ? "active" : ""}`}
              onClick={() => handleFilterChange("available")}
            >
              Available Only
            </button>
          </div>
        </div>

        {error && (
          <div className="error-message">
            <p>{error}</p>
            <button
              onClick={
                Object.keys(searchParams).length > 0 ? clearSearch : fetchSweets
              }
              className="retry-btn"
            >
              Try Again
            </button>
          </div>
        )}

        {sweets.length === 0 && !error ? (
          <div className="no-sweets">
            <p>
              {Object.keys(searchParams).length > 0
                ? "No sweets found matching your search criteria."
                : "No sweets found."}
            </p>
            {Object.keys(searchParams).length > 0 && (
              <div className="search-params-display">
                <p>Search criteria:</p>
                <ul>
                  {Object.entries(searchParams).map(([key, value]) => (
                    <li key={key}>
                      <strong>{key}:</strong> {value}
                    </li>
                  ))}
                </ul>
              </div>
            )}
          </div>
        ) : (
          <div className="sweets-grid">
            {sweets.map((sweet) => (
              <SweetCard
                key={sweet.id}
                sweet={sweet}
                onPurchase={handlePurchaseClick}
                onEdit={handleEditSweet}
                onDelete={handleDeleteSweet}
                onRestock={handleRestockClick}
                isAdmin={isAdmin}
              />
            ))}
          </div>
        )}
      </div>

      <PurchaseModal
        sweet={purchaseModal.sweet}
        isOpen={purchaseModal.isOpen}
        onClose={closePurchaseModal}
        onPurchase={handlePurchase}
      />

      <SweetForm
        sweet={sweetForm.sweet}
        isOpen={sweetForm.isOpen}
        onClose={closeSweetForm}
        onSubmit={handleSweetSubmit}
        loading={isLoading}
      />

      <RestockModal
        sweet={restockModal.sweet}
        isOpen={restockModal.isOpen}
        onClose={closeRestockModal}
        onRestock={handleRestock}
        loading={isLoading}
      />
    </div>
  );
};

export default Dashboard;
