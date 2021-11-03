import { RootState } from "@/store/types";

import { Module } from "vuex";

export type ChunkLoaderState = {
  reloadedAt: number | null;
};

export interface ChunkLoaderModule extends Module<ChunkLoaderState, RootState> {
  mutations: {
    setReloadedAt(state: ChunkLoaderState, reloadedAt: number | null): void;
    reset(state: ChunkLoaderState, payload: null): void;
  };
}

const defaultState: ChunkLoaderState = {
  reloadedAt: null,
};

const chunkLoader: ChunkLoaderModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setReloadedAt(state: ChunkLoaderState, reloadedAt: number | null) {
      state.reloadedAt = reloadedAt;
    },
    reset(state) {
      Object.assign(state, { ...defaultState });
    },
  },
};

export default chunkLoader;
