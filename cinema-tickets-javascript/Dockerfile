FROM node:20-alpine@sha256:998f2775d03a8141e45520686ecbdd0c57eb7c2bfb6407e6080483686b4566ec as builder

WORKDIR /usr/app
COPY ./src .
COPY server.js .
COPY package.json .

RUN npm install --omit=dev --no-progress

FROM node:20-alpine@sha256:998f2775d03a8141e45520686ecbdd0c57eb7c2bfb6407e6080483686b4566ec as runner
ENV PORT=${PORT}

COPY --from=builder /usr/app ./app
WORKDIR ./app

ENTRYPOINT npm start
