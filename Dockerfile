FROM node:16
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 5020
CMD [ "node", "src/api/index.js" ]