# use node/npm image at 16.11.x
FROM node:16.11.1

# directory within image where our code lives
WORKDIR /usr/src/app

# copy dependencies (and lockfile) from source code into image
COPY package*.json ./

# install dependencies, uncomment npm ci when building prod images
RUN npm install
# RUN npm ci --only=production

# copy source code into image
COPY . .

# run on port 8085
EXPOSE 8085

# TODO: implement a build tool so we can run from dist/ rather than src/
# run app
CMD ["node", "src/app.js"]