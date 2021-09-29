import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import initAppContext from "@/utils/appContext";

import "@fontsource/roboto/latin.css";
import "@mdi/font/css/materialdesignicons.css";

Vue.config.productionTip = false;

// in case: config.ts file is loaded before main.ts
initAppContext();

const app = new Vue({
  router,
  store,
  vuetify,
  render: (h) => h(App),
}).$mount("#app");

// only available during E2E tests
if (window.Cypress) {
  window.irisApp = app;
}
