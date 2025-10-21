import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import AppLayout from './components/AppLayout';
import { AuthProvider } from './context/AuthContext';
import { ThemeModeProvider, useThemeMode } from './context/ThemeModeContext';
import Login from './components/Login';
import Register from './components/Register';
import Dashboard from './components/Dashboard';
import StudyPlanForm from './components/StudyPlanForm';
import SessionLogger from './components/SessionLogger';
import Recommendations from './components/Recommendations';
import ProtectedRoute from './components/ProtectedRoute';

function ThemedApp({ children }) {
  const { mode } = useThemeMode();
  const theme = createTheme({
    palette: {
      mode,
      primary: { 
        main: mode === 'light' ? '#6366f1' : '#818cf8',
        light: mode === 'light' ? '#a5b4fc' : '#c4b5fd',
        dark: mode === 'light' ? '#4f46e5' : '#6366f1'
      },
      secondary: { 
        main: mode === 'light' ? '#ec4899' : '#f472b6',
        light: mode === 'light' ? '#f9a8d4' : '#fbbf24',
        dark: mode === 'light' ? '#db2777' : '#ec4899'
      },
      background: mode === 'light'
        ? { 
            default: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
            paper: '#ffffff',
            gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
          }
        : { 
            default: 'linear-gradient(135deg, #0a0a0a 0%, #1a1a1a 100%)',
            paper: '#1a1a1a',
            gradient: 'linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%)',
            card: 'rgba(30, 30, 30, 0.8)',
            surface: 'rgba(40, 40, 40, 0.6)'
          },
      success: { main: '#10b981' },
      warning: { main: '#f59e0b' },
      error: { main: '#ef4444' },
      info: { main: '#3b82f6' }
    },
    shape: { borderRadius: 16 },
    typography: {
      fontFamily: '"Inter", "SF Pro Display", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif',
      h1: { 
        fontWeight: 800, 
        fontSize: '2.5rem',
        color: mode === 'light' ? '#1a1a1a' : '#ffffff',
        background: mode === 'light' 
          ? 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
          : 'linear-gradient(135deg, #818cf8 0%, #c4b5fd 100%)',
        WebkitBackgroundClip: 'text',
        WebkitTextFillColor: 'transparent',
        backgroundClip: 'text'
      },
      h2: { 
        fontWeight: 700,
        fontSize: '2rem',
        color: mode === 'light' ? '#1a1a1a' : '#ffffff',
        background: mode === 'light' 
          ? 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
          : 'linear-gradient(135deg, #818cf8 0%, #c4b5fd 100%)',
        WebkitBackgroundClip: 'text',
        WebkitTextFillColor: 'transparent',
        backgroundClip: 'text'
      },
      h3: { 
        fontWeight: 600, 
        fontSize: '1.5rem',
        color: mode === 'light' ? '#2a2a2a' : '#f0f0f0'
      },
      h4: { 
        fontWeight: 600, 
        fontSize: '1.25rem',
        color: mode === 'light' ? '#2a2a2a' : '#f0f0f0'
      },
      h5: { 
        fontWeight: 600, 
        fontSize: '1.125rem',
        color: mode === 'light' ? '#2a2a2a' : '#f0f0f0'
      },
      h6: { 
        fontWeight: 600, 
        fontSize: '1rem',
        color: mode === 'light' ? '#2a2a2a' : '#f0f0f0'
      },
      body1: { 
        fontSize: '1rem', 
        lineHeight: 1.6,
        color: mode === 'light' ? '#3a3a3a' : '#e0e0e0'
      },
      body2: { 
        fontSize: '0.875rem', 
        lineHeight: 1.5,
        color: mode === 'light' ? '#4a4a4a' : '#d0d0d0'
      }
    },
    components: {
      MuiButton: { 
        defaultProps: { 
          variant: 'contained',
          disableElevation: true
        },
        styleOverrides: {
          root: {
            borderRadius: 12,
            textTransform: 'none',
            fontWeight: 600,
            padding: '12px 24px',
            transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
            '&:hover': {
              transform: 'translateY(-2px)',
              boxShadow: '0 10px 25px rgba(0,0,0,0.15)'
            }
          },
          contained: {
            background: mode === 'light' 
              ? 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
              : 'linear-gradient(135deg, #818cf8 0%, #c4b5fd 100%)',
            '&:hover': {
              background: mode === 'light' 
                ? 'linear-gradient(135deg, #5a67d8 0%, #6b46c1 100%)'
                : 'linear-gradient(135deg, #7c3aed 0%, #a855f7 100%)'
            }
          }
        }
      },
      MuiTextField: { 
        defaultProps: { 
          fullWidth: true, 
          size: 'medium'
        },
        styleOverrides: {
          root: {
            '& .MuiOutlinedInput-root': {
              borderRadius: 12,
              transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
              '&:hover': {
                transform: 'translateY(-1px)'
              },
              '&.Mui-focused': {
                transform: 'translateY(-1px)',
                boxShadow: '0 8px 25px rgba(0,0,0,0.1)'
              }
            }
          }
        }
      },
      MuiCard: {
        styleOverrides: {
          root: {
            borderRadius: 20,
            transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
            background: mode === 'light' 
              ? '#ffffff' 
              : 'rgba(30, 30, 30, 0.8)',
            border: mode === 'light' 
              ? '1px solid rgba(0,0,0,0.1)' 
              : '1px solid rgba(255,255,255,0.1)',
            '&:hover': {
              transform: 'translateY(-8px)',
              boxShadow: mode === 'light' 
                ? '0 20px 40px rgba(0,0,0,0.1)'
                : '0 20px 40px rgba(0,0,0,0.3)'
            }
          }
        }
      },
      MuiContainer: { 
        defaultProps: { maxWidth: 'lg' }
      },
      MuiPaper: {
        styleOverrides: {
          root: {
            borderRadius: 20,
            backgroundImage: 'none'
          }
        }
      }
    }
  });

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      {children}
    </ThemeProvider>
  );
}

function App() {
  return (
    <ThemeModeProvider>
      <ThemedApp>
        <AuthProvider>
          <Router>
            <div className="App">
              <Routes>
              {/* Public routes */}
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
              
              {/* Protected routes */}
                <Route path="/dashboard" element={
                  <ProtectedRoute>
                    <AppLayout>
                      <Dashboard />
                    </AppLayout>
                  </ProtectedRoute>
                } />
                <Route path="/plans/new" element={
                  <ProtectedRoute>
                    <AppLayout>
                      <StudyPlanForm />
                    </AppLayout>
                  </ProtectedRoute>
                } />
                <Route path="/sessions/log" element={
                  <ProtectedRoute>
                    <AppLayout>
                      <SessionLogger />
                    </AppLayout>
                  </ProtectedRoute>
                } />
                <Route path="/recommendations" element={
                  <ProtectedRoute>
                    <AppLayout>
                      <Recommendations />
                    </AppLayout>
                  </ProtectedRoute>
                } />
              
              {/* Default redirect */}
                <Route path="/" element={<Navigate to="/dashboard" replace />} />
              </Routes>
            </div>
          </Router>
        </AuthProvider>
      </ThemedApp>
    </ThemeModeProvider>
  );
}

export default App;