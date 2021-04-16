import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";

import { makeMockAPIServer } from "@/server/mockAPIServer";
import { Configuration } from "@/api";
import config from "@/config";

if (process.env.VUE_APP_ENABLE_MOCK_SERVER === "true") {
  // TODO its not so nice that this is done here
  // Not sure whether imported mockAPIServer ends up in bundle for deployment.
  makeMockAPIServer();
}

const { apiBaseURL } = config;
export const clientConfig = new Configuration({
  basePath: apiBaseURL,
});

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  vuetify,
  render: (h) => h(App),
}).$mount("#app");
