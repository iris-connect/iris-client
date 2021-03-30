import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";

import { makeMockAPIServer } from "@/server/mockAPIServer";
import { Configuration } from "@/api";

// TODO this should be in a config.ts file
let apiBaseURL = "https://api.staging.iris-gateway.de";
if (process.env.NODE_ENV === "development") {
  apiBaseURL = "http://localhost:8080";
  makeMockAPIServer();
}
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
