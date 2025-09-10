# Stage 1: Build Angular app
FROM node:20 AS build
WORKDIR /app
COPY frontend/*.json .
RUN npm install
COPY frontend/src ./src
RUN npm run build -- --configuration production

# Stage 2: Serve with Nginx
FROM nginx:alpine
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html
EXPOSE 80
