import React, { useState } from 'react';
import { useNavigate, useLocation, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useTheme } from '@mui/material/styles';
import Paper from '@mui/material/Paper';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import Fade from '@mui/material/Fade';
import Slide from '@mui/material/Slide';
import Avatar from '@mui/material/Avatar';
import InputAdornment from '@mui/material/InputAdornment';
import IconButton from '@mui/material/IconButton';
import { 
  Email, 
  Lock, 
  Visibility, 
  VisibilityOff,
  School,
  Login as LoginIcon
} from '@mui/icons-material';

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const theme = useTheme();
  const from = location.state?.from?.pathname || '/dashboard';

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await login({ email, password });
      navigate(from, { replace: true });
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Box sx={{ 
      minHeight: '100vh', 
      background: theme.palette.background.default,
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      position: 'relative',
      overflow: 'hidden'
    }}>
      {/* Background Elements */}
      <Box sx={{
        position: 'absolute',
        top: '-50%',
        left: '-50%',
        width: '200%',
        height: '200%',
        background: 'radial-gradient(circle, rgba(99,102,241,0.1) 0%, transparent 70%)',
        animation: 'float 6s ease-in-out infinite'
      }} />
      <Box sx={{
        position: 'absolute',
        bottom: '-50%',
        right: '-50%',
        width: '200%',
        height: '200%',
        background: 'radial-gradient(circle, rgba(236,72,153,0.1) 0%, transparent 70%)',
        animation: 'float 8s ease-in-out infinite reverse'
      }} />

      <Container maxWidth="sm">
        <Fade in timeout={1000}>
          <Box>
            {/* Logo and Branding */}
            <Slide direction="down" in timeout={800}>
              <Box sx={{ textAlign: 'center', mb: 4 }}>
                <Avatar sx={{ 
                  width: 80, 
                  height: 80, 
                  background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                  fontSize: '2.5rem',
                  mx: 'auto',
                  mb: 2,
                  boxShadow: '0 10px 30px rgba(102, 126, 234, 0.3)'
                }}>
                  <School />
                </Avatar>
                <Typography variant="h3" component="h1" sx={{ 
                  fontWeight: 800,
                  mb: 1,
                  background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                  WebkitBackgroundClip: 'text',
                  WebkitTextFillColor: 'transparent',
                  backgroundClip: 'text'
                }}>
                  Grasp
                </Typography>
                <Typography variant="h6" sx={{ 
                  color: 'rgba(255, 255, 255, 0.8)',
                  fontWeight: 400
                }}>
                  AI-Powered Study Planner
                </Typography>
              </Box>
            </Slide>

            {/* Login Form */}
            <Slide direction="up" in timeout={1200}>
              <Paper elevation={0} sx={{ 
                p: 5,
                background: theme.palette.mode === 'light' 
                  ? 'rgba(255, 255, 255, 0.1)'
                  : 'rgba(30, 30, 30, 0.9)',
                backdropFilter: 'blur(20px)',
                border: theme.palette.mode === 'light' 
                  ? '1px solid rgba(255, 255, 255, 0.2)'
                  : '1px solid rgba(255, 255, 255, 0.15)',
                borderRadius: 4
              }}>
                <Typography variant="h4" sx={{ 
                  textAlign: 'center', 
                  mb: 3,
                  fontWeight: 600,
                  color: theme.palette.mode === 'light' ? 'white' : '#ffffff'
                }}>
                  Welcome Back
                </Typography>
                
                <Box component="form" onSubmit={handleSubmit}>
                  <TextField
                    label="Email Address"
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                    margin="normal"
                    fullWidth
                    InputProps={{
                      startAdornment: (
                        <InputAdornment position="start">
                          <Email sx={{ color: theme.palette.mode === 'light' ? 'rgba(255, 255, 255, 0.7)' : 'rgba(255, 255, 255, 0.8)' }} />
                        </InputAdornment>
                      ),
                    }}
                    sx={{
                      '& .MuiOutlinedInput-root': {
                        color: theme.palette.mode === 'light' ? 'white' : '#ffffff',
                        '& fieldset': {
                          borderColor: theme.palette.mode === 'light' 
                            ? 'rgba(255, 255, 255, 0.3)'
                            : 'rgba(255, 255, 255, 0.4)',
                        },
                        '&:hover fieldset': {
                          borderColor: theme.palette.mode === 'light' 
                            ? 'rgba(255, 255, 255, 0.5)'
                            : 'rgba(255, 255, 255, 0.6)',
                        },
                        '&.Mui-focused fieldset': {
                          borderColor: '#667eea',
                        },
                      },
                      '& .MuiInputLabel-root': {
                        color: theme.palette.mode === 'light' 
                          ? 'rgba(255, 255, 255, 0.7)'
                          : 'rgba(255, 255, 255, 0.8)',
                        '&.Mui-focused': {
                          color: '#667eea',
                        },
                      },
                    }}
                  />
                  
                  <TextField
                    label="Password"
                    type={showPassword ? 'text' : 'password'}
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                    margin="normal"
                    fullWidth
                    InputProps={{
                      startAdornment: (
                        <InputAdornment position="start">
                          <Lock sx={{ color: theme.palette.mode === 'light' ? 'rgba(255, 255, 255, 0.7)' : 'rgba(255, 255, 255, 0.8)' }} />
                        </InputAdornment>
                      ),
                      endAdornment: (
                        <InputAdornment position="end">
                          <IconButton
                            onClick={() => setShowPassword(!showPassword)}
                            edge="end"
                            sx={{ color: theme.palette.mode === 'light' ? 'rgba(255, 255, 255, 0.7)' : 'rgba(255, 255, 255, 0.8)' }}
                          >
                            {showPassword ? <VisibilityOff /> : <Visibility />}
                          </IconButton>
                        </InputAdornment>
                      ),
                    }}
                    sx={{
                      '& .MuiOutlinedInput-root': {
                        color: theme.palette.mode === 'light' ? 'white' : '#ffffff',
                        '& fieldset': {
                          borderColor: theme.palette.mode === 'light' 
                            ? 'rgba(255, 255, 255, 0.3)'
                            : 'rgba(255, 255, 255, 0.4)',
                        },
                        '&:hover fieldset': {
                          borderColor: theme.palette.mode === 'light' 
                            ? 'rgba(255, 255, 255, 0.5)'
                            : 'rgba(255, 255, 255, 0.6)',
                        },
                        '&.Mui-focused fieldset': {
                          borderColor: '#667eea',
                        },
                      },
                      '& .MuiInputLabel-root': {
                        color: theme.palette.mode === 'light' 
                          ? 'rgba(255, 255, 255, 0.7)'
                          : 'rgba(255, 255, 255, 0.8)',
                        '&.Mui-focused': {
                          color: '#667eea',
                        },
                      },
                    }}
                  />
                  
                  <Button
                    type="submit"
                    fullWidth
                    disabled={submitting}
                    sx={{
                      mt: 3,
                      mb: 2,
                      py: 1.5,
                      background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                      borderRadius: 3,
                      fontSize: '1.1rem',
                      fontWeight: 600,
                      textTransform: 'none',
                      '&:hover': {
                        background: 'linear-gradient(135deg, #5a67d8 0%, #6b46c1 100%)',
                        transform: 'translateY(-2px)',
                        boxShadow: '0 10px 25px rgba(102, 126, 234, 0.3)'
                      },
                      '&:disabled': {
                        background: 'rgba(255, 255, 255, 0.1)',
                        color: 'rgba(255, 255, 255, 0.5)'
                      }
                    }}
                    startIcon={<LoginIcon />}
                  >
                    {submitting ? 'Signing in...' : 'Sign In'}
          </Button>
        </Box>
                
                <Box sx={{ textAlign: 'center', mt: 3 }}>
                  <Typography variant="body2" sx={{ 
                    color: theme.palette.mode === 'light' 
                      ? 'rgba(255, 255, 255, 0.7)' 
                      : 'rgba(255, 255, 255, 0.8)'
                  }}>
                    Don't have an account?{' '}
                    <Link 
                      to="/register" 
                      style={{ 
                        color: '#667eea', 
                        textDecoration: 'none',
                        fontWeight: 600
                      }}
                    >
                      Create one here
                    </Link>
        </Typography>
                </Box>
      </Paper>
            </Slide>
          </Box>
        </Fade>
      </Container>

      {/* CSS Animation */}
      <style jsx>{`
        @keyframes float {
          0%, 100% { transform: translateY(0px) rotate(0deg); }
          50% { transform: translateY(-20px) rotate(5deg); }
        }
      `}</style>
    </Box>
  );
}


