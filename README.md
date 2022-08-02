# cinema-tickets

This repository is a code test to satisfy the problem defined in [task.md](./TASK.md).

## 🏃 Running the application 🏃

There are two options for running this application locally.

Prior to both, clone the repo to your machine and open a terminal window in the root of the project.

### 💻 NodeJS 💻

Run the application using `node`.

#### Prerequisites

- `node` version `16.11.1` or higher (though this has only been tested on major version `16`)

#### Commands

```sh
npm i
npm start
```

This will run the following commands in order:

```sh
npm run test:lint && npm run test:unit && node src/index.js
```

If either of the test commands fail, the application will not start.

If you wish to make any changes to the application, hot reloading is enabled by running:

```sh
npm run dev
```

This bypasses the `npm test` commands.

### 🐳 Docker 🐳

#### Prerequisites

- Docker

#### Commands

```sh
npm run docker:start
```

### Verifying

You can check the application is running by opening another terminal window and hitting:

```sh
curl localhost:3000/healthcheck
```