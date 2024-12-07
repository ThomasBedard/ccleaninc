/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        primary: '#007bff', // Matches the blue in the screenshot
        secondary: '#f8f9fa', // Background color
        textPrimary: '#212529', // Dark text color
        textSecondary: '#6c757d', // Muted text
      },
      fontFamily: {
        sans: ['Inter', 'sans-serif'], // Use a modern sans-serif font
      },
      boxShadow: {
        card: '0 4px 6px rgba(0, 0, 0, 0.1)', // For cards and similar elements
        button: '0 2px 4px rgba(0, 0, 0, 0.1)', // For buttons
      },
      spacing: {
        '128': '32rem',
        '144': '36rem',
      },
      screens: {
        xs: '480px', // Extra small devices
      },
      backgroundImage: {
        'gradient-to-b': 'linear-gradient(to bottom, #e4edf5, #f7f8fc)',
      },
    },
  },
  plugins: [
    require('@tailwindcss/typography'), // Optional: Enables typography utilities
  ],
};