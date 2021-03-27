import eventTrackingForm from "../views/event-tracking-form/event-tracking-form.store";

import { StoreOptions } from "vuex";
import { RootState } from "@/store/types";

export const storeOptions: StoreOptions<RootState> = {
  state: {} as RootState,
  mutations: {},
  actions: {},
  modules: {
    eventTrackingForm,
  },
};
