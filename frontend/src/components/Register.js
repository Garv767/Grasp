import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Paper from '@mui/material/Paper';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';

export default function Register() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    // Placeholder: treat register same as login for now
    await login({ email, password });
    navigate('/dashboard', { replace: true });
  };

  return (
    <Box sx={{ maxWidth: 420, mx: 'auto', mt: 8 }}>
      <Paper elevation={0} sx={{ p: 4 }}>
        <Typography variant="h5" gutterBottom>Create account</Typography>
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1 }}>
          <TextField label="Email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} required margin="normal" />
          <TextField label="Password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} required margin="normal" />
          <Button type="submit" fullWidth sx={{ mt: 2 }}>Create account</Button>
        </Box>
        <Typography variant="body2" sx={{ mt: 2 }}>
          Have an account? <Link to="/login">Sign in</Link>
        </Typography>
      </Paper>
    </Box>
  );
}


