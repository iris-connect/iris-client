module.exports = {
  transpileDependencies: ["vuetify"],
  // pluginOptions: {
  //   configureWebpack: {
  //     optimization: {
  //       minimize: true,
  //       minimizer: buildForProduction
  //         ? [
  //             new TerserPlugin({
  //               terserOptions: {
  //                 ecma: 6,
  //                 // eslint-disable-next-line @typescript-eslint/camelcase
  //                 compress: { drop_console: true },
  //                 output: { comments: false, beautify: false },
  //               },
  //             }),
  //           ]
  //         : [],
  //     },
  //   },
  // },
  chainWebpack: (config) => {
    config.plugin("html").tap((args) => {
      const htmlConfig = args[0];
      htmlConfig.irisAppContext = {
        apiBaseURL: process.env.VUE_APP_API_BASE_URL,
      };
      return args;
    });
  },
};
