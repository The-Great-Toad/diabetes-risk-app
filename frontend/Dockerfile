FROM node:20-alpine

WORKDIR /usr/src/app
COPY . /usr/src/app

RUN npm install -g @angular/cli
RUN npm install

CMD ["ng", "serve", "--host", "0.0.0.0"]

# # Étape 1 : Build de l'application Angular
# FROM node:20-alpine as build
# WORKDIR /usr/src/app
# COPY package.json package-lock.json ./
# RUN npm install
# COPY . .
# RUN npm run build -- --configuration production

# # Étape 2 : Serveur Nginx pour servir l'application
# FROM nginx:alpine
# # Supprimez le fichier index.html par défaut de Nginx
# RUN rm -rf /usr/share/nginx/html/*
# # Copiez les fichiers de build Angular dans le répertoire de Nginx
# COPY --from=build /usr/src/app/dist/frontend /usr/share/nginx/html
# # Copiez la configuration Nginx personnalisée (optionnel)
# # COPY nginx.conf /etc/nginx/conf.d/default.conf
# # Exposez le port 80
# EXPOSE 80
# # Démarrez Nginx
# CMD ["nginx", "-g", "daemon off;"]