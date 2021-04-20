import eventTrackingForm from "../views/event-tracking-form/event-tracking-form.store";
import eventTrackingList from "../views/event-tracking-list/event-tracking-list.store";
import eventTrackingDetails from "@/views/event-tracking-details/event-tracking-details.store";

import indexTrackingForm from "../views/index-tracking-form/index-tracking-form.store";
import indexTrackingList from "../views/index-tracking-list/index-tracking-list.store";
import indexTrackingDetails from "@/views/index-tracking-details/index-tracking-details.store";

import { StoreOptions } from "vuex";
import { RootState } from "@/store/types";
import home from "@/views/home/home.store";

export const storeOptions: StoreOptions<RootState> = {
  state: {} as RootState,
  mutations: {},
  actions: {},
  modules: {
    home: home,
    eventTrackingForm,
    eventTrackingList,
    eventTrackingDetails,
    indexTrackingForm,
    indexTrackingList,
    indexTrackingDetails,
  },
};
