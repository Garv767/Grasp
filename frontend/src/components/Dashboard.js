import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useTheme } from '@mui/material/styles';
import Grid from '@mui/material/Grid';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardActions from '@mui/material/CardActions';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import Avatar from '@mui/material/Avatar';
import Chip from '@mui/material/Chip';
import LinearProgress from '@mui/material/LinearProgress';
import Fade from '@mui/material/Fade';
import Slide from '@mui/material/Slide';
import { 
  School, 
  PlayArrow, 
  TrendingUp, 
  Schedule,
  CheckCircle,
  Star,
  Timer,
  EmojiEvents
} from '@mui/icons-material';

export default function Dashboard() {
  const { user, logout } = useAuth();
  const theme = useTheme();
  const [stats, setStats] = useState({
    totalPlans: 3,
    completedSessions: 12,
    totalStudyTime: 18.5,
    averageScore: 8.2
  });

  const [recentActivity] = useState([
    { id: 1, subject: 'Java Programming', duration: 90, score: 9, date: '2 hours ago' },
    { id: 2, subject: 'Data Structures', duration: 60, score: 8, date: '1 day ago' },
    { id: 3, subject: 'React Components', duration: 45, score: 7, date: '2 days ago' }
  ]);

  const quickActions = [
    {
      title: 'Create Study Plan',
      description: 'Define goals and subjects for your learning journey',
      icon: <School sx={{ fontSize: 40 }} />,
      color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
      link: '/plans/new'
    },
    {
      title: 'Log Study Session',
      description: 'Track your progress and performance',
      icon: <PlayArrow sx={{ fontSize: 40 }} />,
      color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
      link: '/sessions/log'
    },
    {
      title: 'View Recommendations',
      description: 'Get AI-powered study suggestions',
      icon: <TrendingUp sx={{ fontSize: 40 }} />,
      color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
      link: '/recommendations'
    }
  ];

  return (
    <Box sx={{ minHeight: '100vh', background: theme.palette.background.default, p: 3 }}>
      <Fade in timeout={800}>
        <Box>
          {/* Welcome Header */}
          <Box sx={{ mb: 4, textAlign: 'center' }}>
            <Slide direction="down" in timeout={600}>
              <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', mb: 2 }}>
                <Avatar 
                  sx={{ 
                    width: 80, 
                    height: 80, 
                    background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                    fontSize: '2rem',
                    mr: 2
                  }}
                >
                  {user?.email?.charAt(0).toUpperCase()}
                </Avatar>
                <Box>
                  <Typography variant="h3" component="h1">
                    Welcome back!
                  </Typography>
                  <Typography variant="h6" color="text.secondary">
                    Ready to continue your learning journey?
                  </Typography>
                </Box>
              </Box>
            </Slide>
          </Box>

          {/* Stats Cards */}
          <Grid container spacing={3} sx={{ mb: 4 }}>
            {[
              { label: 'Study Plans', value: stats.totalPlans, icon: <Schedule />, color: '#667eea' },
              { label: 'Sessions Completed', value: stats.completedSessions, icon: <CheckCircle />, color: '#10b981' },
              { label: 'Hours Studied', value: `${stats.totalStudyTime}h`, icon: <Timer />, color: '#f59e0b' },
              { label: 'Avg Score', value: stats.averageScore, icon: <Star />, color: '#ec4899' }
            ].map((stat, index) => (
              <Grid item xs={12} sm={6} md={3} key={stat.label}>
                <Slide direction="up" in timeout={800 + index * 200}>
                  <Card sx={{ 
                    background: theme.palette.mode === 'light' 
                      ? 'rgba(255, 255, 255, 0.1)'
                      : 'rgba(30, 30, 30, 0.9)',
                    backdropFilter: 'blur(10px)',
                    border: theme.palette.mode === 'light' 
                      ? '1px solid rgba(255, 255, 255, 0.2)'
                      : '1px solid rgba(255, 255, 255, 0.15)',
                    color: theme.palette.mode === 'light' ? 'white' : '#ffffff'
                  }}>
                    <CardContent sx={{ textAlign: 'center', p: 3 }}>
                      <Box sx={{ 
                        display: 'inline-flex', 
                        p: 2, 
                        borderRadius: '50%', 
                        background: `${stat.color}20`,
                        mb: 2
                      }}>
                        {stat.icon}
                      </Box>
                      <Typography variant="h4" fontWeight="bold">
                        {stat.value}
                      </Typography>
                      <Typography variant="body2" sx={{ opacity: 0.8 }}>
                        {stat.label}
                      </Typography>
                    </CardContent>
                  </Card>
                </Slide>
              </Grid>
            ))}
          </Grid>

          {/* Quick Actions */}
          <Typography variant="h4" sx={{ mb: 3, fontWeight: 600 }}>
            Quick Actions
          </Typography>
          <Grid container spacing={3} sx={{ mb: 4 }}>
            {quickActions.map((action, index) => (
              <Grid item xs={12} md={4} key={action.title}>
                <Slide direction="up" in timeout={1000 + index * 200}>
                  <Card sx={{ 
                    height: '100%',
                    cursor: 'pointer',
                    position: 'relative',
                    overflow: 'hidden',
                    '&:hover .action-overlay': {
                      opacity: 1
                    }
                  }}>
                    <Box 
                      className="action-overlay"
                      sx={{
                        position: 'absolute',
                        top: 0,
                        left: 0,
                        right: 0,
                        bottom: 0,
                        background: action.color,
                        opacity: 0,
                        transition: 'opacity 0.3s ease',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        color: 'white'
                      }}
                    >
                      <Typography variant="h6" fontWeight="bold">
                        Click to {action.title.toLowerCase()}
                      </Typography>
                    </Box>
                    <CardContent sx={{ p: 4, textAlign: 'center' }}>
                      <Box sx={{ 
                        display: 'inline-flex', 
                        p: 3, 
                        borderRadius: '50%', 
                        background: action.color,
                        color: 'white',
                        mb: 2
                      }}>
                        {action.icon}
                      </Box>
                      <Typography variant="h5" gutterBottom fontWeight="bold">
                        {action.title}
                      </Typography>
                      <Typography color="text.secondary" sx={{ mb: 3 }}>
                        {action.description}
                      </Typography>
                    </CardContent>
                    <CardActions sx={{ justifyContent: 'center', pb: 3 }}>
                      <Button 
                        component={Link} 
                        to={action.link}
                        variant="contained"
                        sx={{
                          background: action.color,
                          px: 4,
                          py: 1.5,
                          borderRadius: 3
                        }}
                      >
                        Get Started
                      </Button>
                    </CardActions>
                  </Card>
                </Slide>
              </Grid>
            ))}
          </Grid>

          {/* Recent Activity */}
          <Typography variant="h4" sx={{ mb: 3, fontWeight: 600 }}>
            Recent Activity
          </Typography>
          <Grid container spacing={2}>
            {recentActivity.map((activity, index) => (
              <Grid item xs={12} key={activity.id}>
                <Slide direction="right" in timeout={1200 + index * 200}>
                  <Card sx={{ 
                    background: theme.palette.mode === 'light' 
                      ? 'rgba(255, 255, 255, 0.05)'
                      : 'rgba(40, 40, 40, 0.8)',
                    backdropFilter: 'blur(10px)',
                    border: theme.palette.mode === 'light' 
                      ? '1px solid rgba(255, 255, 255, 0.1)'
                      : '1px solid rgba(255, 255, 255, 0.1)',
                    color: theme.palette.mode === 'light' ? 'white' : '#ffffff'
                  }}>
                    <CardContent sx={{ p: 3 }}>
                      <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                        <Box sx={{ display: 'flex', alignItems: 'center' }}>
                          <Avatar sx={{ 
                            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                            mr: 2
                          }}>
                            <EmojiEvents />
                          </Avatar>
                          <Box>
                            <Typography variant="h6" fontWeight="bold">
                              {activity.subject}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                              {activity.duration} minutes • {activity.date}
                            </Typography>
                          </Box>
                        </Box>
                        <Box sx={{ textAlign: 'right' }}>
                          <Chip 
                            label={`${activity.score}/10`}
                            color="primary"
                            sx={{ fontWeight: 'bold' }}
                          />
                          <LinearProgress 
                            variant="determinate" 
                            value={activity.score * 10} 
                            sx={{ 
                              mt: 1, 
                              width: 100,
                              '& .MuiLinearProgress-bar': {
                                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
                              }
                            }}
                          />
                        </Box>
                      </Box>
                    </CardContent>
                  </Card>
                </Slide>
              </Grid>
            ))}
          </Grid>

          {/* Logout Button */}
          <Box sx={{ textAlign: 'center', mt: 4 }}>
            <Button 
              onClick={logout} 
              variant="outlined"
              sx={{
                borderColor: theme.palette.mode === 'light' 
                  ? 'rgba(255, 255, 255, 0.3)'
                  : 'rgba(255, 255, 255, 0.4)',
                color: theme.palette.mode === 'light' ? 'white' : '#ffffff',
                px: 4,
                py: 1.5,
                borderRadius: 3,
                fontWeight: 600,
                '&:hover': {
                  borderColor: theme.palette.mode === 'light' ? 'white' : '#ffffff',
                  background: theme.palette.mode === 'light' 
                    ? 'rgba(255, 255, 255, 0.1)'
                    : 'rgba(255, 255, 255, 0.15)',
                  transform: 'translateY(-2px)'
                }
              }}
            >
              Sign Out
            </Button>
          </Box>
        </Box>
      </Fade>
    </Box>
  );
}


