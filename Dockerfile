FROM node:16.14-alpine3.15@sha256:38bc06c682ae1f89f4c06a5f40f7a07ae438ca437a2a04cf773e66960b2d75bc

WORKDIR /usr/src/app

ENV PORT 3000

COPY package*.json ./
RUN npm ci
COPY src src

EXPOSE $PORT

USER node

CMD [ "npm", "start" ]