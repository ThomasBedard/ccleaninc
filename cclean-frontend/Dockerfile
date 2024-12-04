# Use Node.js for building and serving
FROM node:18-alpine as builder
WORKDIR /app
COPY ./cclean-frontend .
RUN npm install

COPY . .

EXPOSE 5173

CMD ["npm", "run", "dev"]
