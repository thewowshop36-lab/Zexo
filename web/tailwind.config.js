/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        zexo: {
          blue: '#1E40AF',
          gold: '#F59E0B',
          dark: '#0F172A',
          card: '#1E293B',
          neon: '#06B6D4'
        }
      }
    },
  },
  plugins: [],
}
