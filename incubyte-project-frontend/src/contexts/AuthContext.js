// ========================================
// IMPORTS - External Dependencies
// ========================================
import  { createContext, useContext, useState, useEffect, useCallback } from 'react';  // React hooks for context and state
import axios from 'axios';                                        // HTTP client for API calls
import NotificationService from '../services/NotificationService'; // Global notification service

// ========================================
// CONTEXT CREATION
// ========================================
const AuthContext = createContext();                              // Create authentication context

/**
 * ========================================
// AUTHENTICATION HOOK
// ========================================
 * 
 * Custom hook to access authentication context
 * Must be used within an AuthProvider component
 * 
 * @returns {Object} Authentication context with user data and functions
 * @throws {Error} If used outside of AuthProvider
 */
export const useAuth = () => {
  const context = useContext(AuthContext);                        // Get context value
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider'); // Error if not in provider
  }
  return context;                                                 // Return context value
};

/**
 * ========================================
// AUTHENTICATION PROVIDER COMPONENT
// ========================================
 * 
 * This component provides authentication state and functions to all child components.
 * It manages user login, logout, token storage, and automatic token expiration handling.
 * 
 * FEATURES:
 * - User authentication state management
 * - JWT token storage and retrieval
 * - Automatic token attachment to API requests
 * - Token expiration detection and automatic logout
 * - Persistent login state across browser sessions
 * 
 * PROPS:
 * - children: Child components that need access to authentication context
 */
export const AuthProvider = ({ children }) => {
  
  // ========================================
  // STATE MANAGEMENT - Authentication States
  // ========================================
  const [user, setUser] = useState(null);                         // Current user data (null if not logged in)
  const [token, setToken] = useState(localStorage.getItem('token')); // JWT token from localStorage
  const [loading, setLoading] = useState(false);                  // Loading state for auth operations

  // ========================================
  // AUTHENTICATION FUNCTIONS
  // ========================================
  
  /**
   * Logs out the current user and clears all authentication data
   * 
   * ACTIONS:
   * - Clears user state
   * - Clears token state
   * - Removes token from localStorage
   * - Removes user data from localStorage
   * - Removes Authorization header from axios
   * - Redirects to login page
   */
  const logout = useCallback(() => {
    // ========================================
    // CLEAR STATE
    // ========================================
    setToken(null);                                                // Clear token state
    setUser(null);                                                 // Clear user state
    
    // ========================================
    // CLEAR LOCAL STORAGE
    // ========================================
    localStorage.removeItem('token');                              // Remove token from localStorage
    localStorage.removeItem('user');                               // Remove user data from localStorage
    
    // ========================================
    // CLEAR AXIOS HEADERS
    // ========================================
    delete axios.defaults.headers.common['Authorization'];         // Remove Authorization header
    
    // ========================================
    // REDIRECT TO LOGIN
    // ========================================
    if (window.location.pathname !== '/login') {                   // Only redirect if not already on login page
      window.location.href = '/login';                             // Force redirect to login page
    }
  }, []);                                                          // No dependencies - function is stable

  // ========================================
  // EFFECTS - Token Management
  // ========================================
  
  /**
   * Effect to automatically attach/remove JWT token from axios headers
   * Runs whenever the token state changes
   */
  useEffect(() => {
    if (token) {
      // ========================================
      // ATTACH TOKEN TO AXIOS HEADERS
      // ========================================
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;  // Add Bearer token to all requests
    } else {
      // ========================================
      // REMOVE TOKEN FROM AXIOS HEADERS
      // ========================================
      delete axios.defaults.headers.common['Authorization'];               // Remove Authorization header
    }
  }, [token]);                                                             // Re-run when token changes

  /**
   * Effect to set up axios response interceptor for automatic token expiration handling
   * This interceptor checks all API responses for 401 errors and JWT expiration
   */
  useEffect(() => {
    // ========================================
    // SETUP RESPONSE INTERCEPTOR
    // ========================================
    const interceptor = axios.interceptors.response.use(
      // ========================================
      // SUCCESS RESPONSE HANDLER
      // ========================================
      (response) => response,                                            // Pass through successful responses
      
      // ========================================
      // ERROR RESPONSE HANDLER
      // ========================================
      (error) => {
        // ========================================
        // CHECK FOR 401 UNAUTHORIZED ERRORS
        // ========================================
        if (error.response?.status === 401) {
          const errorMessage = error.response?.data?.error || '';        // Get error message from response
          const statusCode = error.response?.data?.statusCode || '';     // Get status code from response
          
          // ========================================
          // CHECK FOR JWT-RELATED ERRORS
          // ========================================
          if (errorMessage.includes('JWT expired') ||                    // JWT token has expired
              errorMessage.includes('Invalid JWT token') ||              // JWT token is invalid
              errorMessage.includes('JWT') ||                            // Any JWT-related error
              statusCode === 'UNAUTHORIZED') {                           // Generic unauthorized error
            
            console.log('🔐 JWT Token expired, logging out user:', errorMessage);
            
            // ========================================
            // AUTOMATIC LOGOUT AND NOTIFICATION
            // ========================================
            logout();                                                    // Logout user automatically
            NotificationService.showNotification('Token expired. Please login again.', 'error'); // Show error notification
          }
        }
        return Promise.reject(error);                                    // Reject the promise to propagate error
      }
    );

    // ========================================
    // CLEANUP FUNCTION
    // ========================================
    return () => {
      axios.interceptors.response.eject(interceptor);                    // Remove interceptor on cleanup
    };
  }, [logout]);                                                          // Re-run when logout function changes

  const login = async (username, password) => {
    setLoading(true);
    try {
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        username,
        password
      });
      
      const { jwt, userId, roles } = response.data.data;
      
      setToken(jwt);
      setUser({ userId, username, roles });
      localStorage.setItem('token', jwt);
      localStorage.setItem('user', JSON.stringify({ userId, username, roles }));
      
      return { success: true, message: response.data.message };
    } catch (error) {
      const message = error.response?.data?.error || 'Login failed';
      return { success: false, message };
    } finally {
      setLoading(false);
    }
  };

  const register = async (username, password) => {
    setLoading(true);
    try {
      const response = await axios.post('http://localhost:8080/api/auth/register', {
        username,
        password
      });
      
      return { success: true, message: response.data.message };
    } catch (error) {
      const message = error.response?.data?.message || 'Registration failed';
      return { success: false, message };
    } finally {
      setLoading(false);
    }
  };

  const isAuthenticated = () => {
    return !!token;
  };

  const value = {
    user,
    token,
    loading,
    login,
    register,
    logout,
    isAuthenticated
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};