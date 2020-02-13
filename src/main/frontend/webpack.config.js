const path = require('path');
const HtmlWebPackPlugin = require("html-webpack-plugin");

module.exports = {
  entry: {
    dashboard: path.join(__dirname, 'dashboard'),
    //app: path.join(__dirname, 'app'),
  },
  output: {
    pathinfo: false,
    path: path.join(__dirname, '../../../target/classes/static'),
    //publicPath: '/assets/',
    filename: 'bundle-[name].js'
  },
  plugins: [
    new HtmlWebPackPlugin({
      template: path.join(__dirname, 'public/index.html'),
      //chunks: ['dashboard']
    })
  ],
  resolve: {
    extensions: ['*', '.js', '.jsx']
  },
  module: {
    rules: [
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: {
          loader: "babel-loader"
        }
      },
      {
        test: /\.css$/i,
        use: ['style-loader', 'css-loader'],
      },
      {
        test: /\.s[ac]ss$/i,
        use: [
          // Creates `style` nodes from JS strings
          'style-loader',
          // Translates CSS into CommonJS
          'css-loader',
          // Compiles Sass to CSS
          'sass-loader',
        ],
      },
      {
        test: /\.(png|jpe?g|gif)$/i,
        use: [
            {
              loader: 'file-loader',
            },
        ],
      },
      {
        test: /\.svg$/,
        use: [
            {
              loader: 'svg-url-loader',
              options: {
                limit: 10000,
              },
            },
        ],
      },
    ]
  }
};