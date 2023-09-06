FROM node:20-alpine@sha256:998f2775d03a8141e45520686ecbdd0c57eb7c2bfb6407e6080483686b4566ec as builder

WORKDIR /usr/app
COPY package.json .

RUN npm install --no-progress

COPY ./src ./src
COPY ./dist ./dist
COPY server.js .

RUN npm run build

FROM node:20-alpine@sha256:998f2775d03a8141e45520686ecbdd0c57eb7c2bfb6407e6080483686b4566ec as runner
ENV PORT=${PORT}

COPY --from=builder /usr/app ./app
WORKDIR ./app

ENTRYPOINT npm start
