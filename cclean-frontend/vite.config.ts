import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import dotenv from "dotenv";

dotenv.config();
// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: "0.0.0.0", // Ensures the server listens on all network interfaces
    port: 5173, // Matches the exposed port in Docker
    strictPort: true, // Ensures Vite fails if the port is not available
  },
});
