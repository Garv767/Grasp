import React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import Tooltip from '@mui/material/Tooltip';
import Avatar from '@mui/material/Avatar';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import Divider from '@mui/material/Divider';
import Chip from '@mui/material/Chip';
import { useTheme } from '@mui/material/styles';
import LightModeIcon from '@mui/icons-material/LightMode';
import DarkModeIcon from '@mui/icons-material/DarkMode';
import School from '@mui/icons-material/School';
import Dashboard from '@mui/icons-material/Dashboard';
import Add from '@mui/icons-material/Add';
import PlayArrow from '@mui/icons-material/PlayArrow';
import TrendingUp from '@mui/icons-material/TrendingUp';
import Logout from '@mui/icons-material/Logout';
import Person from '@mui/icons-material/Person';
import { Link as RouterLink, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useThemeMode } from '../context/ThemeModeContext';

export default function AppLayout({ children }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const theme = useTheme();
  const { mode, toggleMode } = useThemeMode();
  const [anchorEl, setAnchorEl] = React.useState(null);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const handleProfileMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleProfileMenuClose = () => {
    setAnchorEl(null);
  };

  const navigationItems = [
    { label: 'Dashboard', path: '/dashboard', icon: <Dashboard /> },
    { label: 'New Plan', path: '/plans/new', icon: <Add /> },
    { label: 'Log Session', path: '/sessions/log', icon: <PlayArrow /> },
    { label: 'Recommendations', path: '/recommendations', icon: <TrendingUp /> }
  ];

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh', flexDirection: 'column' }}>
      <AppBar 
        position="static" 
        elevation={0}
        sx={{
          background: theme.palette.mode === 'light' 
            ? 'rgba(255, 255, 255, 0.1)'
            : 'rgba(20, 20, 20, 0.9)',
          backdropFilter: 'blur(20px)',
          borderBottom: theme.palette.mode === 'light' 
            ? '1px solid rgba(255, 255, 255, 0.1)'
            : '1px solid rgba(255, 255, 255, 0.15)',
          color: theme.palette.mode === 'light' ? 'white' : '#ffffff'
        }}
      >
        <Container maxWidth="lg">
          <Toolbar sx={{ py: 1 }}>
            {/* Logo */}
            <Box sx={{ display: 'flex', alignItems: 'center', mr: 4 }}>
              <Avatar sx={{ 
                width: 40, 
                height: 40, 
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                mr: 1
              }}>
                <School />
              </Avatar>
              <Typography variant="h5" sx={{ 
                fontWeight: 800,
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                WebkitBackgroundClip: 'text',
                WebkitTextFillColor: 'transparent',
                backgroundClip: 'text'
              }}>
                Grasp
              </Typography>
            </Box>

            {/* Navigation */}
            <Box sx={{ flexGrow: 1, display: 'flex', gap: 1 }}>
              {navigationItems.map((item) => (
                <Button
                  key={item.path}
                  component={RouterLink}
                  to={item.path}
                  startIcon={item.icon}
                  sx={{
                    color: location.pathname === item.path 
                      ? '#667eea' 
                      : theme.palette.mode === 'light' 
                        ? 'rgba(255, 255, 255, 0.8)'
                        : 'rgba(255, 255, 255, 0.9)',
                    fontWeight: location.pathname === item.path ? 600 : 400,
                    borderRadius: 2,
                    px: 2,
                    py: 1,
                    transition: 'all 0.3s ease',
                    '&:hover': {
                      background: theme.palette.mode === 'light' 
                        ? 'rgba(255, 255, 255, 0.1)'
                        : 'rgba(255, 255, 255, 0.15)',
                      color: theme.palette.mode === 'light' ? 'white' : '#ffffff',
                      transform: 'translateY(-1px)'
                    }
                  }}
                >
                  {item.label}
                </Button>
              ))}
            </Box>

            {/* Right Side Actions */}
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
              {/* Theme Toggle */}
              <Tooltip title={mode === 'light' ? 'Switch to dark mode' : 'Switch to light mode'}>
                <IconButton 
                  onClick={toggleMode}
                  sx={{
                    color: theme.palette.mode === 'light' 
                      ? 'rgba(255, 255, 255, 0.8)'
                      : 'rgba(255, 255, 255, 0.9)',
                    transition: 'all 0.3s ease',
                    '&:hover': {
                      background: theme.palette.mode === 'light' 
                        ? 'rgba(255, 255, 255, 0.1)'
                        : 'rgba(255, 255, 255, 0.15)',
                      color: theme.palette.mode === 'light' ? 'white' : '#ffffff',
                      transform: 'rotate(180deg)'
                    }
                  }}
                >
                  {mode === 'light' ? <DarkModeIcon /> : <LightModeIcon />}
                </IconButton>
              </Tooltip>

              {/* User Profile */}
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                <Chip
                  label={`${user?.email?.split('@')[0] || 'User'}`}
                  avatar={<Avatar sx={{ width: 24, height: 24, background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' }}>
                    {user?.email?.charAt(0).toUpperCase()}
                  </Avatar>}
                  sx={{
                    background: theme.palette.mode === 'light' 
                      ? 'rgba(255, 255, 255, 0.1)'
                      : 'rgba(40, 40, 40, 0.8)',
                    color: theme.palette.mode === 'light' ? 'white' : '#ffffff',
                    border: theme.palette.mode === 'light' 
                      ? '1px solid rgba(255, 255, 255, 0.2)'
                      : '1px solid rgba(255, 255, 255, 0.3)',
                    '&:hover': {
                      background: theme.palette.mode === 'light' 
                        ? 'rgba(255, 255, 255, 0.2)'
                        : 'rgba(60, 60, 60, 0.9)'
                    }
                  }}
                  onClick={handleProfileMenuOpen}
                />
              </Box>
            </Box>
          </Toolbar>
        </Container>
      </AppBar>

      {/* Profile Menu */}
      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={handleProfileMenuClose}
        PaperProps={{
          sx: {
            mt: 1,
            borderRadius: 3,
            background: 'rgba(255, 255, 255, 0.95)',
            backdropFilter: 'blur(20px)',
            border: '1px solid rgba(255, 255, 255, 0.2)',
            minWidth: 200
          }
        }}
        transformOrigin={{ horizontal: 'right', vertical: 'top' }}
        anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
      >
        <MenuItem onClick={handleProfileMenuClose} sx={{ py: 2 }}>
          <Avatar sx={{ 
            width: 32, 
            height: 32, 
            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
            mr: 2
          }}>
            <Person />
          </Avatar>
          <Box>
            <Typography variant="subtitle2" fontWeight="bold">
              {user?.email}
            </Typography>
            <Typography variant="caption" color="text.secondary">
              Study Planner
            </Typography>
          </Box>
        </MenuItem>
        <Divider />
        <MenuItem 
          onClick={() => {
            handleProfileMenuClose();
            handleLogout();
          }}
          sx={{ 
            color: 'error.main',
            '&:hover': {
              background: 'rgba(239, 68, 68, 0.1)'
            }
          }}
        >
          <Logout sx={{ mr: 2 }} />
          Sign Out
        </MenuItem>
      </Menu>

      {/* Main Content */}
      <Box sx={{ flexGrow: 1 }}>
        {children}
      </Box>
    </Box>
  );
}


