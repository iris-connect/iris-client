import eventTrackingForm from "../views/event-tracking-form/event-tracking-form.store";
import eventTrackingList from "../views/event-tracking-list/event-tracking-list.store";
import eventTrackingDetails from "@/views/event-tracking-details/event-tracking-details.store";
import userLogin from "@/views/user-login/user-login.store";
import userManagementList from "../views/user-management-list/user-management-list.store";

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
    userManagementList,
  },
  plugins: [
    createPersistedState({
      key: "iris-client-frontend",
      paths: ["userLogin.session"],
      getState(key: string, storage) {
        try {
          const item: string | null = storage.getItem(key);
          return item ? JSON.parse(window.atob(item)) : null;
        } catch (error) {
          return null;
        }
      },
      setState(key: string, state, storage): void {
        try {
          storage.setItem(key, window.btoa(JSON.stringify(state)));
        } catch (error) {
          // ignored
        }
      },
    }),
  ],
};
