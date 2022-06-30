import eventTrackingForm from "../views/event-tracking-form/event-tracking-form.store";
import userLogin from "@/views/user-login/user-login.store";
import adminUserList from "@/views/admin-user-list/admin-user-list.store";
import adminUserCreate from "@/views/admin-user-create/admin-user-create.store";
import adminUserEdit from "@/views/admin-user-edit/admin-user-edit.store";

import indexTrackingForm from "../views/index-tracking-form/index-tracking-form.store";
import indexTrackingList from "../views/index-tracking-list/index-tracking-list.store";
import indexTrackingDetails from "@/views/index-tracking-details/index-tracking-details.store";
// @todo - indexTracking: optional remove next line once index cases are permanently activated again
import indexTrackingSettings from "@/views/app-settings/index-tracking-settings.store";
import normalizeSettings from "@/views/app-settings/normalize-settings.store";
import chunkLoader from "@/views/app-settings/chunk-loader.store";
import checkinAppStatusList from "@/views/checkin-app-status-list/checkin-app-status-list.store";
import irisMessageCreate from "@/views/iris-message-create/iris-message-create.store";
import e2eTests from "@/modules/e2e-tests/e2e-tests.store";
import mockApi from "@/modules/mock-api/mock-api.store";

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
    userLogin,
    adminUserList,
    adminUserCreate,
    adminUserEdit,
    indexTrackingForm,
    indexTrackingList,
    indexTrackingDetails,
    // @todo - indexTracking: optional remove next line once index cases are permanently activated again
    indexTrackingSettings,
    normalizeSettings,
    chunkLoader,
    checkinAppStatusList,
    irisMessageCreate,
    e2eTests,
    mockApi,
  },
  plugins: [
    createPersistedState({
      key: "iris-client-frontend",
      paths: [
        "userLogin.session",
        "mockApi",
        "normalizeSettings.logEnabled",
        // @todo - indexTracking: optional remove next line once index cases are permanently activated again
        "indexTrackingSettings.indexTrackingEnabled",
        "chunkLoader.reloadedAt",
      ],
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
