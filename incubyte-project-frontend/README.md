# Sweet Shop Frontend Application

A modern React application for a sweet shop with user authentication and sweets management.

## Features

### 🔐 Authentication
- **User Registration**: Create new accounts with email validation and strong password requirements
- **User Login**: Secure login with JWT token authentication
- **Password Validation**: Must contain uppercase, lowercase, number, and special character
- **Session Management**: Persistent login with localStorage

### 🍬 Sweets Dashboard
- **View All Sweets**: Display all available sweets from the API
- **Filter Options**: Toggle between all sweets and available sweets only
- **Search & Filter**: Advanced search functionality with multiple criteria
- **Responsive Design**: Beautiful cards showing sweet details
- **Real-time Data**: Fetch fresh data from the backend API

### 🎨 Modern UI/UX
- **Beautiful Design**: Modern gradient-based design with smooth animations
- **Responsive Layout**: Works perfectly on desktop, tablet, and mobile
- **Notification System**: Toast notifications for success/error messages
- **Loading States**: Smooth loading indicators and error handling

## API Integration

### Authentication Endpoints
- **Login**: `POST http://localhost:8080/api/auth/login`
- **Register**: `POST http://localhost:8080/api/auth/register`

### Sweets Endpoints
- **All Sweets**: `GET http://localhost:8080/api/sweets`
- **Available Sweets**: `GET http://localhost:8080/api/sweets/available` (requires JWT)
- **Search Sweets**: `GET http://localhost:8080/api/sweets/search` (requires JWT)
  - Query parameters: `name`, `category`, `minPrice`, `maxPrice`
- **Specific Sweet**: `GET http://localhost:8080/api/sweets/{id}` (requires JWT)

## Technology Stack

- **React 19.1.1**: Modern React with hooks
- **React Router DOM**: Client-side routing
- **Axios**: HTTP client for API calls
- **CSS3**: Modern styling with gradients and animations
- **Context API**: State management for authentication

## Getting Started

### Prerequisites
- Node.js (v14 or higher)
- npm or yarn
- Backend API running on `http://localhost:8080`

### Installation

1. Clone the repository
2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

4. Open [http://localhost:3000](http://localhost:3000) to view it in the browser

## Project Structure

```
src/
├── components/
│   ├── AuthForm.css          # Shared auth form styles
│   ├── Dashboard.css         # Dashboard component styles
│   ├── Dashboard.js          # Main dashboard component
│   ├── LoginForm.js          # Login form component
│   ├── Notification.css      # Notification component styles
│   ├── Notification.js       # Toast notification component
│   ├── RegisterForm.js       # Registration form component
│   ├── SearchForm.css        # Search form component styles
│   ├── SearchForm.js         # Search and filter form component
│   ├── SweetCard.css         # Sweet card component styles
│   └── SweetCard.js          # Individual sweet display component
├── contexts/
│   └── AuthContext.js        # Authentication context provider
├── App.css                   # Global styles
├── App.js                    # Main app component with routing
└── index.js                  # Application entry point
```

## Usage

### Registration
1. Navigate to the registration page
2. Enter a valid email address
3. Create a password meeting the requirements:
   - At least one uppercase letter
   - At least one lowercase letter
   - At least one number
   - At least one special character
4. Confirm your password
5. Submit the form

### Login
1. Enter your registered email and password
2. Click login to authenticate
3. You'll be redirected to the dashboard upon successful login

### Dashboard
1. **Search & Filter**: Use the search form to find sweets by:
   - Name (partial matching)
   - Category
   - Price range (minimum and maximum)
2. View all available sweets in a beautiful card layout
3. Use the filter buttons to toggle between all sweets and available sweets only
4. Each sweet card shows:
   - Name and category
   - Price in Indian Rupees
   - Available quantity
   - Creation and update dates
5. Clear search to return to all sweets view
6. Logout when done

## Error Handling

The application includes comprehensive error handling:
- Form validation with real-time feedback
- API error handling with user-friendly messages
- Network error handling with retry options
- Authentication error handling with proper redirects

## Responsive Design

The application is fully responsive and works on:
- Desktop computers (1200px+)
- Tablets (768px - 1199px)
- Mobile phones (320px - 767px)

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Development

### Available Scripts

- `npm start`: Runs the app in development mode
- `npm test`: Launches the test runner
- `npm run build`: Builds the app for production
- `npm run eject`: Ejects from Create React App (one-way operation)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is licensed under the MIT License.