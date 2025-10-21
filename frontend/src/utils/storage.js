export function save(key, value) {
  localStorage.setItem(key, JSON.stringify(value));
}

export function load(key, defaultValue = null) {
  const raw = localStorage.getItem(key);
  if (raw == null) return defaultValue;
  try {
    return JSON.parse(raw);
  } catch {
    return defaultValue;
  }
}

export function remove(key) {
  localStorage.removeItem(key);
}



