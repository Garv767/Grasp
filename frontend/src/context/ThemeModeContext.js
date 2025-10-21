import React, { createContext, useContext, useEffect, useMemo, useState } from 'react';

const ThemeModeContext = createContext(null);

export function ThemeModeProvider({ children }) {
  const [mode, setMode] = useState('light');

  useEffect(() => {
    const stored = localStorage.getItem('grasp_theme_mode');
    if (stored === 'light' || stored === 'dark') {
      setMode(stored);
    }
  }, []);

  useEffect(() => {
    localStorage.setItem('grasp_theme_mode', mode);
  }, [mode]);

  const toggleMode = () => setMode((m) => (m === 'light' ? 'dark' : 'light'));

  const value = useMemo(() => ({ mode, toggleMode }), [mode]);
  return <ThemeModeContext.Provider value={value}>{children}</ThemeModeContext.Provider>;
}

export function useThemeMode() {
  const ctx = useContext(ThemeModeContext);
  if (!ctx) throw new Error('useThemeMode must be used within ThemeModeProvider');
  return ctx;
}




