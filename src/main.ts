import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";

import { makeMockAPIServer } from "@/server/mockAPIServer";

if (process.env.NODE_ENV === "development") {
  makeMockAPIServer();
}

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  vuetify,
  render: (h) => h(App),
}).$mount("#app");
