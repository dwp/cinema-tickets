export default {
  base: {
    path: '/'
  },
  healthcheck: {
    path: '/healthcheck',
    responseString: '\n🧑‍⚕️ Application is healthy 🎉\n'
  },
  tickets: {
    path: '/tickets'
  }
}
