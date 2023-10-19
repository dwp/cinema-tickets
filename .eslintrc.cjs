module.exports = {
  extends: [
    '@dwp/eslint-config-base',
    '@dwp/eslint-config-mocha',
  ],
  parserOptions: {
    ecmaVersion: 2022,
    sourceType: 'module',
  },
  env: {
    es2021: true,
  },
  rules: {
    'import/extensions': ['error', 'ignorePackages'],
  },
};
