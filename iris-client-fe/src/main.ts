import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
import initAppContext from "@/utils/appContext";

Vue.config.productionTip = false;

// in case: config.ts file is loaded before main.ts
initAppContext();

new Vue({
  router,
  store,
  vuetify,
  render: (h) => h(App),
}).$mount("#app");
