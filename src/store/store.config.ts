import eventTrackingForm from "../views/event-tracking-form/event-tracking-form.store";
import eventTrackingList from "../views/event-tracking-list/event-tracking-list.store";
import eventTrackingDetails from "@/views/event-tracking-details/event-tracking-details.store";
import userLogin from "@/views/user-login/user-login.store";

import { StoreOptions } from "vuex";
import { RootState } from "@/store/types";
import home from "@/views/home/home.store";

import createPersistedState from "vuex-persistedstate";

export const storeOptions: StoreOptions<RootState> = {
  state: {} as RootState,
  mutations: {},
  actions: {},
  modules: {
    home: home,
    eventTrackingForm,
    eventTrackingList,
    eventTrackingDetails,
    userLogin,
  },
  plugins: [
    // @todo: maybe encrypt session information before saving them in the session storage
    createPersistedState({
      key: "iris-client-frontend",
      paths: ["userLogin.session"],
      storage: window.sessionStorage,
    }),
  ],
};
