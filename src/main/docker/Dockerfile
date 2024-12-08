# # Stage 1: Build
# FROM gradle:jdk17 as builder
# WORKDIR /app
# COPY . .
# RUN gradle build -x test

# # Stage 2: Run
# FROM openjdk:17-jdk-slim
# WORKDIR /app
# COPY --from=builder /app/build/libs/*.jar app.jar
# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "app.jar"]

# Stage 1: Build the application
# FROM node:16-alpine as builder

# # Set the working directory
# WORKDIR /app

# # Copy package.json and package-lock.json to install dependencies
# COPY ./cclean-frontend/package*.json ./

# # Install dependencies
# RUN npm install

# # Copy the rest of the application code
# COPY ./cclean-frontend .

# # Expose the port Vite uses for development
# EXPOSE 5173

# # Start the Vite development server
# CMD ["npm", "run", "dev"]

# Use Node.js for building and serving
FROM node:16-alpine as builder
WORKDIR /app
COPY ./cclean-frontend .
RUN npm install

COPY . .

EXPOSE 5173

CMD ["npm", "run", "dev"]