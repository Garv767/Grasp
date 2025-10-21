import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Paper from '@mui/material/Paper';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';

export default function StudyPlanForm() {
  const navigate = useNavigate();
  const [title, setTitle] = useState('');
  const [goal, setGoal] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    // Placeholder only
    navigate('/dashboard');
  };

  return (
    <Box sx={{ maxWidth: 640, mx: 'auto' }}>
      <Paper elevation={0} sx={{ p: 4 }}>
        <Typography variant="h5" gutterBottom>Create Study Plan</Typography>
        <Box component="form" onSubmit={handleSubmit}>
          <TextField label="Title" value={title} onChange={(e) => setTitle(e.target.value)} required margin="normal" />
          <TextField label="Goal" value={goal} onChange={(e) => setGoal(e.target.value)} margin="normal" multiline rows={4} />
          <Button type="submit" sx={{ mt: 2 }}>Save</Button>
        </Box>
      </Paper>
    </Box>
  );
}


