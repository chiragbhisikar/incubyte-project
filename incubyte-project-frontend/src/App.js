import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import LoginForm from './components/LoginForm';
import RegisterForm from './components/RegisterForm';
import Dashboard from './components/Dashboard';
import Notification from './components/Notification';
import NotificationService from './services/NotificationService';
import './App.css';

const AppContent = () => {
  const [notification, setNotification] = useState({ message: '', type: '', show: false });
  const { isAuthenticated } = useAuth();

  const showNotification = (message, type) => {
    setNotification({ message, type, show: true });
  };

  const hideNotification = () => {
    setNotification({ message: '', type: '', show: false });
  };

  // Register notification callback with the service
  useEffect(() => {
    NotificationService.setNotificationCallback(showNotification);
  }, []);

  return (
    <div className="App">
      <Routes>
        <Route 
          path="/login" 
          element={
            isAuthenticated() ? 
            <Navigate to="/dashboard" replace /> : 
            <LoginForm onShowNotification={showNotification} />
          } 
        />
        <Route 
          path="/register" 
          element={
            isAuthenticated() ? 
            <Navigate to="/dashboard" replace /> : 
            <RegisterForm onShowNotification={showNotification} />
          } 
        />
        <Route 
          path="/dashboard" 
          element={
            isAuthenticated() ? 
            <Dashboard onShowNotification={showNotification} /> : 
            <Navigate to="/login" replace />
          } 
        />
        <Route 
          path="/" 
          element={
            <Navigate to={isAuthenticated() ? "/dashboard" : "/login"} replace />
          } 
        />
      </Routes>
      
      <Notification
        message={notification.message}
        type={notification.type}
        onClose={hideNotification}
      />
    </div>
  );
};

function App() {
  return (
    <AuthProvider>
      <Router>
        <AppContent />
      </Router>
    </AuthProvider>
  );
}

export default App;