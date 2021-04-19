module.exports = {
  transpileDependencies: ["vuetify"],
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
