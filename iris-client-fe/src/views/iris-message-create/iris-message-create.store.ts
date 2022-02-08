import { RootState } from "@/store/types";

import { Module } from "vuex";
import { IrisMessageDataInsert } from "@/api";
import { parseData } from "@/utils/data";

export type IrisMessageCreateState = {
  dataAttachments: IrisMessageDataInsert[];
};

export interface IrisMessageDetailsModule
  extends Module<IrisMessageCreateState, RootState> {
  mutations: {
    setDataAttachments(
      state: IrisMessageCreateState,
      payload: IrisMessageDataInsert[]
    ): void;
    reset(state: IrisMessageCreateState, payload: null): void;
  };
}

const defaultState: IrisMessageCreateState = {
  dataAttachments: [],
};

const irisMessageCreate: IrisMessageDetailsModule = {
  namespaced: true,
  state() {
    return parseData(defaultState);
  },
  mutations: {
    setDataAttachments(state, payload) {
      state.dataAttachments = payload;
    },
    reset(state) {
      Object.assign(state, parseData(defaultState));
    },
  },
};

export default irisMessageCreate;
