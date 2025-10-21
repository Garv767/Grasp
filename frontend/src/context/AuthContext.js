import React, { createContext, useContext, useEffect, useMemo, useState } from 'react';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const stored = localStorage.getItem('grasp_auth_user');
    if (stored) {
      try {
        setUser(JSON.parse(stored));
      } catch {
        localStorage.removeItem('grasp_auth_user');
      }
    }
    setLoading(false);
  }, []);

  const login = async (credentials) => {
    // Placeholder login: accept any credentials
    const fakeUser = { id: 'demo', email: credentials.email };
    setUser(fakeUser);
    localStorage.setItem('grasp_auth_user', JSON.stringify(fakeUser));
    return fakeUser;
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('grasp_auth_user');
  };

  const value = useMemo(() => ({ user, loading, login, logout }), [user, loading]);

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return ctx;
}



