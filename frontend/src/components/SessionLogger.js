import React, { useState } from 'react';
import Paper from '@mui/material/Paper';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';

export default function SessionLogger() {
  const [subject, setSubject] = useState('');
  const [duration, setDuration] = useState(60);
  const [notes, setNotes] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    setSubject('');
    setDuration(60);
    setNotes('');
    alert('Session logged (placeholder)');
  };

  return (
    <Box sx={{ maxWidth: 640, mx: 'auto' }}>
      <Paper elevation={0} sx={{ p: 4 }}>
        <Typography variant="h5" gutterBottom>Log Study Session</Typography>
        <Box component="form" onSubmit={handleSubmit}>
          <TextField label="Subject" value={subject} onChange={(e) => setSubject(e.target.value)} required margin="normal" />
          <TextField label="Duration (minutes)" type="number" value={duration} onChange={(e) => setDuration(Number(e.target.value))} inputProps={{ min: 1 }} margin="normal" />
          <TextField label="Notes" value={notes} onChange={(e) => setNotes(e.target.value)} multiline rows={3} margin="normal" />
          <Button type="submit" sx={{ mt: 2 }}>Log Session</Button>
        </Box>
      </Paper>
    </Box>
  );
}


