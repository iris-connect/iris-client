import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";

// TODO import real location type as defined in swagger client
type EventTrackingFormData = {
  name: string;
};

// TODO import real location type as defined in swagger client
type EventLocation = {
  name: string;
};

// TODO import real query options type as defined in swagger client
type EventLocationQueryOptions = {
  name: string;
  city: string;
  zip: string;
};

// TODO import real location type as defined in swagger client
const api = {
  eventLocations: {
    get: (options: EventLocationQueryOptions) => {
      console.log("EVENT_LOCATIONS", "GET", options);
      return new Promise((resolve) => {
        setTimeout(resolve, 3000);
      });
    },
  },
  eventTrackings: {
    put: (data: EventTrackingFormData) => {
      console.log("EVENT_TRACKINGS", "PUT", data);
      return new Promise((resolve) => {
        setTimeout(resolve, 3000);
      });
    },
  },
};

export type EventTrackingFormState = {
  locations: EventLocation[] | null;
  locationsLoading: boolean;
  eventCreationOngoing: boolean;
};

export interface EventTrackingFormModule
  extends Module<EventTrackingFormState, RootState> {
  mutations: {
    setEventLocations(
      state: EventTrackingFormState,
      locations: EventLocation[] | null
    ): void;
    setEventLocationsLoading(
      state: EventTrackingFormState,
      payload: boolean
    ): void;
    setEventCreationOngoing(
      state: EventTrackingFormState,
      payload: boolean
    ): void;
  };
  actions: {
    fetchEventLocations(
      { commit }: { commit: Commit },
      queryOptions: EventLocationQueryOptions
    ): Promise<void>;
    createEventTracking(
      { commit }: { commit: Commit },
      formData: EventTrackingFormData
    ): Promise<void>;
  };
}

const productDetail: EventTrackingFormModule = {
  namespaced: true,
  state() {
    return {
      locations: null,
      locationsLoading: false,
      eventCreationOngoing: false,
    };
  },
  mutations: {
    setEventLocations(state, locations: EventLocation[] | null) {
      state.locations = locations;
    },
    setEventLocationsLoading(state, loading: boolean) {
      state.locationsLoading = loading;
    },
    setEventCreationOngoing(state, loading: boolean) {
      state.eventCreationOngoing = loading;
    },
  },
  actions: {
    async fetchEventLocations({ commit }, queryOptions) {
      let locations = null;
      commit("setProductLoading", true);
      try {
        locations = await api.eventLocations.get(queryOptions);
      } finally {
        commit("setEventLocations", locations);
        commit("setEventLocationsLoading", false);
      }
    },
    async createEventTracking({ commit }, formData) {
      commit("setEventCreationOngoing", true);
      try {
        await api.eventTrackings.put(formData);
      } finally {
        commit("setEventCreationOngoing", false);
      }
    },
  },
};

export default productDetail;
