/**
 * @type import('webpack').Configuration
 */
const HtmlWebpackPlugin = require("html-webpack-plugin");
const { EnvironmentPlugin } = require("webpack");
const { CleanWebpackPlugin } = require("clean-webpack-plugin");
const { resolve } = require("path");

module.exports = (env, argv) => {
  const { mode } = argv;
  const isProduction = mode === "production";

  return {
    mode,
    entry: "./src/App.tsx",
    output: {
      path: `${__dirname}/dist`,
      filename: "[name].[fullhash].js",
      publicPath: "/",
    },
    module: {
      rules: [
        {
          test: /\.tsx?$/,
          use: "ts-loader",
        },
        {
          test: /\.css/,
          use: [
            // linkタグに出力する機能
            "style-loader",
            // CSSをバンドルするための機能
            {
              loader: "css-loader",
              options: {
                // オプションでCSS内のurl()メソッドの取り込みを禁止する
                url: false,
                // ソースマップを有効にする
                sourceMap: !isProduction,
              }
            }
          ]
        }
      ],
    },
    resolve: {
      extensions: [".ts", ".tsx", ".js", ".json"],
      alias: {
        "@": resolve(__dirname, "src"),
      },
    },
    plugins: [
      new HtmlWebpackPlugin({
        template: "./index.html",
      }),
      new CleanWebpackPlugin({
        cleanAfterEveryBuildPatterns: ["dist"],
      }),
      new EnvironmentPlugin([
        "FIREBASE_API_KEY",
        "FIREBASE_AUTH_DOMAIN",
        "FIREBASE_PROJECT_ID",
        "FIREBASE_STORAGE_BUCKET",
        "FIREBASE_MESSAGING_SENDER_ID",
        "FIREBASE_APP_ID",
        "FIREBASE_MEASUREMENT_ID",
      ]),
    ],
    devServer: {
      open: true,
      historyApiFallback: true,
    },
    devtool: isProduction ? false : "source-map",
  };
};
