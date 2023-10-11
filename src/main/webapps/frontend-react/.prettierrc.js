module.exports = {
  printWidth: 80,
  tabWidth: 2,
  bracketSpacing: true,
  singleQuote: true,
  trailingComma: 'none',
  arrowParens: 'avoid',
  semi: false,
  endOfLine: 'auto',
  importOrder: ['^react', '^@', '^.'],
  importOrderSeparation: true,
  importOrderSortSpecifiers: true,
  plugins: [require.resolve("@trivago/prettier-plugin-sort-imports")]
}