module.exports = {
  "root": true,
  "env": {
    "browser": true,
    "es2021": true
  },
  "extends": [
    "eslint:recommended",
    "plugin:react/recommended",
    "plugin:@typescript-eslint/recommended",
    "airbnb",
    "plugin:prettier/recommended",
    "plugin:eslint-comments/recommended",
  ],
  "parser": "@typescript-eslint/parser",
  "parserOptions": {
    "ecmaFeatures": {
      "jsx": true
    },
    "ecmaVersion": 12,
    "sourceType": "module"
  },
  "plugins": [
    "react",
    "@typescript-eslint",
    "jsx-a11y",
    "immutable",
    "jest",
    "prettier",
    "react-hooks",
  ],
  "rules": {
    "no-use-before-define": "off",
    "@typescript-eslint/no-use-before-define": ["error"],
    "import/prefer-default-export": "off",
    "react/jsx-filename-extension": ["error", { extensions: [".jsx", ".tsx"] }],
    "react/prop-types": "off",
    "import/no-extraneous-dependencies": "off",
    "import/extensions": "off",
    "react/jsx-props-no-spreading": "off",
    "no-underscore-dangle": [
      2,
      {
        allow: ["__typename", "__schema"],
      },
    ],
  },
  settings: {
    "import/extensions": [".js", ".jsx", ".ts", ".tsx"],
    "import/resolver": {
      node: {},
      webpack: {
        config: "./webpack.config.js",
      },
    },
  },
};
