# Event Management System

A comprehensive Java Swing-based Event Management System with secure authentication, event creation, and registration management.

## Features

### Student Features
- **Secure Registration**: Create account with email validation and password hashing
- **Secure Login**: Authenticate with encrypted password storage
- **Event Discovery**: Browse all available events with detailed information
- **Event Registration**: Register for events with confirmation system
- **Registration Management**: View and cancel existing registrations
- **User Dashboard**: Personalized dashboard showing registered events

### Admin Features
- **Admin Authentication**: Secure admin login system
- **Event Management**: Create, view, and delete events
- **Registration Monitoring**: View all registrations for each event
- **Admin Management**: Add new administrators to the system
- **Comprehensive Dashboard**: Central control panel for all admin functions

## Technical Improvements

### Security
- **Password Hashing**: All passwords stored using SHA-256 with salt
- **Input Validation**: Comprehensive validation for all user inputs
- **SQL Injection Prevention**: Safe file-based data storage
- **Authentication Security**: Secure login mechanisms

### Architecture
- **MVC Pattern**: Clean separation of Model, View, and Controller
- **Service Layer**: Business logic separated from UI components
- **Utility Classes**: Reusable utilities for common operations
- **Thread Safety**: Safe concurrent file operations

### Code Quality
- **Error Handling**: Comprehensive exception handling with user-friendly messages
- **Input Validation**: All inputs validated before processing
- **Constants Management**: Centralized configuration and string literals
- **Documentation**: Extensive JavaDoc comments throughout

### User Experience
- **Modern UI**: Clean, professional Swing interface
- **Responsive Design**: Background processing prevents UI freezing
- **User Feedback**: Clear success/error messages and confirmations
- **Tooltips**: Helpful hints throughout the interface

## Project Structure

```
src/
├── com.eventmanagement/
│   ├── models/           # Data models
│   │   ├── Student.java
│   │   ├── Event.java
│   │   └── Registration.java
│   ├── services/         # Business logic
│   │   ├── StudentService.java
│   │   ├── EventService.java
│   │   └── RegistrationService.java
│   ├── utils/            # Utility classes
│   │   ├── CsvUtils.java
│   │   ├── PasswordUtils.java
│   │   ├── ValidationUtils.java
│   │   └── UIUtils.java
│   ├── ui/               # User interface
│   │   ├── HomePage.java
│   │   ├── StudentLoginPage.java
│   │   ├── StudentSignupPage.java
│   │   ├── StudentDashboard.java
│   │   ├── AdminLoginPage.java
│   │   ├── AdminDashboard.java
│   │   ├── AddAdminDialog.java
│   │   ├── AddEventPage.java
│   │   └── ViewEventsPage.java
│   └── Constants.java    # Application constants
├── students.csv          # Student data storage
├── events.csv           # Events data storage
├── registrations.csv    # Registration data storage
└── admins.csv          # Admin data storage
```

## Installation & Setup

### Prerequisites
- Java 8 or higher
- Java Swing (included in standard JDK)

### Running the Application
1. Compile all Java files:
   ```bash
   javac -d . *.java
   ```

2. Run the main application:
   ```bash
   java com.eventmanagement.ui.HomePage
   ```

### Default Admin Credentials
- Username: `admin`
- Password: `admin123`

## Data Storage

The application uses CSV files for data persistence:
- `students.csv`: Student account information
- `events.csv`: Event details
- `registrations.csv`: Event registration records  
- `admins.csv`: Administrator accounts

## Key Improvements Made

1. **Security**: Implemented password hashing and secure authentication
2. **Architecture**: Adopted MVC pattern with service layers
3. **Thread Safety**: Added synchronized file operations
4. **Input Validation**: Comprehensive validation with user-friendly error messages
5. **Error Handling**: Robust exception handling throughout
6. **UI/UX**: Modern interface with background processing and user feedback
7. **Code Quality**: Extensive documentation and consistent coding standards
8. **Maintainability**: Modular design with reusable components

## Future Enhancements

- Database integration (MySQL/PostgreSQL)
- Email notifications for events
- Event categories and filtering
- User profile management
- Event capacity limits
- Reporting and analytics
- Mobile app integration
- REST API development

## License

This project is created for educational purposes and portfolio demonstration.

## Author

N.Uthkarsh Sai  
BTech AI & Data Science  
Amrita Vishwa Vidyapeetham
