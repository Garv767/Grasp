import React, { useEffect, useMemo, useState } from 'react';
import Grid from '@mui/material/Grid';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Alert from '@mui/material/Alert';
import CircularProgress from '@mui/material/CircularProgress';
import Box from '@mui/material/Box';
import Chip from '@mui/material/Chip';
import { api } from '../services/api';

export default function Recommendations() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const samplePayload = useMemo(() => ({
    plan: {
      id: 1,
      userId: 123,
      title: 'Demo Plan',
      description: 'Sample subjects',
      subjects: [
        { id: 10, planId: 1, name: 'Algebra', difficultyLevel: 'BEGINNER', timeAllocationMinutes: 45 },
        { id: 11, planId: 1, name: 'Reading', difficultyLevel: 'INTERMEDIATE', timeAllocationMinutes: 30 }
      ]
    },
    sessions: [
      { planId: 1, subjectId: 10, durationMinutes: 25, completionPercent: 60, performanceScore: 5 },
      { planId: 1, subjectId: 10, durationMinutes: 35, completionPercent: 70, performanceScore: 6 },
      { planId: 1, subjectId: 11, durationMinutes: 20, completionPercent: 80, performanceScore: 9 }
    ]
  }), []);

  useEffect(() => {
    let isMounted = true;
    (async () => {
      try {
        setLoading(true);
        const data = await api.post('/api/recommendations/generate', samplePayload);
        if (isMounted) setItems(Array.isArray(data) ? data : []);
      } catch (e) {
        if (isMounted) setError(e.message || 'Failed to load recommendations');
      } finally {
        if (isMounted) setLoading(false);
      }
    })();
    return () => { isMounted = false; };
  }, [samplePayload]);

  return (
    <>
      <Typography variant="h5" gutterBottom>Recommendations</Typography>
      {loading && (
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, py: 2 }}>
          <CircularProgress size={20} />
          <Typography>Loading...</Typography>
        </Box>
      )}
      {!!error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
      <Grid container spacing={3}>
        {items.map((r, idx) => (
          <Grid item xs={12} md={4} key={idx}>
            <Card>
              <CardContent>
                <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                  <Typography variant="h6">{r.type?.replace('_', ' ') || 'Recommendation'}</Typography>
                  {typeof r.confidenceScore === 'number' && (
                    <Chip label={`Conf: ${(r.confidenceScore * 100).toFixed(0)}%`} size="small" />
                  )}
                </Box>
                <Typography sx={{ mt: 1 }} color="text.secondary">
                  {r.reasoning || '—'}
                </Typography>
                {r.value && (
                  <Typography sx={{ mt: 1 }}><b>Suggested:</b> {r.value}</Typography>
                )}
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </>
  );
}


