import { storeOptions } from "@/store/store.config";

import Vue from "vue";
import Vuex, { Store } from "vuex";

Vue.use(Vuex);
export default new Store(storeOptions);
