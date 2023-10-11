module.exports = {
  root: true,
  env: { es6: true, node: true, jest: true },
  parser: '@typescript-eslint/parser',
  extends: [
    'eslint:recommended',
    'plugin:@typescript-eslint/eslint-recommended',
    'plugin:@typescript-eslint/recommended',
    'prettier'
  ],
  settings: {
    react: {
      version: 'detect'
    }
  },
  plugins: [
    '@typescript-eslint',
    'react',
    'react-hooks',
    'prettier'
  ],
  rules: {
    semi: ['error', 'never'],
    'comma-dangle': ['error', 'never'],
    'no-unused-vars': 'off',
    '@typescript-eslint/no-unused-vars': 'error',
    'no-use-before-define': 'off',
    '@typescript-eslint/no-use-before-define': 'off',
    '@typescript-eslint/explicit-module-boundary-types': 'off',
    '@typescript-eslint/no-empty-function': 'off',
    '@typescript-eslint/no-non-null-assertion': 'off',
    '@typescript-eslint/no-namespace': 'off',
    'no-case-declarations': 'off',
    'prettier/prettier': 'error',
    'react/prop-types': 'off',
    'react-hooks/rules-of-hooks': 'error',
    'react-hooks/exhaustive-deps': 'off',
    '@typescript-eslint/naming-convention': [
      'error',
      {
        selector: 'variable',
        format: ['strictCamelCase', 'UPPER_CASE', 'StrictPascalCase']
      },
      {
        selector: ['function', 'method', 'property', 'memberLike'],
        format: ['strictCamelCase']
      },
      { selector: 'enumMember', format: ['UPPER_CASE'] },
      {
        selector: ['typeLike'],
        format: ['StrictPascalCase']
      }
    ]
  },
  ignorePatterns: [
    'config-overrides.js',
    'node_modules',
    'build',
    '*.css',
    '.eslintrc.js'
  ]
}