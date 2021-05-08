import eventTrackingForm from "../views/event-tracking-form/event-tracking-form.store";
import eventTrackingList from "../views/event-tracking-list/event-tracking-list.store";
import eventTrackingDetails from "@/views/event-tracking-details/event-tracking-details.store";
import userLogin from "@/views/user-login/user-login.store";
import adminUserList from "@/views/admin-user-list/admin-user-list.store";
import adminUserCreate from "@/views/admin-user-create/admin-user-create.store";
import adminUserEdit from "@/views/admin-user-edit/admin-user-edit.store";

import indexTrackingForm from "../views/index-tracking-form/index-tracking-form.store";
import indexTrackingList from "../views/index-tracking-list/index-tracking-list.store";
import indexTrackingDetails from "@/views/index-tracking-details/index-tracking-details.store";

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
    adminUserList,
    adminUserCreate,
    adminUserEdit,
    indexTrackingForm,
    indexTrackingList,
    indexTrackingDetails,
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
