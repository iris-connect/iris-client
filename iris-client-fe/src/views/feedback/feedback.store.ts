import { FeedbackInsert } from "@/api";
import client from "@/api-client";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";
import { ErrorMessage, getErrorMessage } from "@/utils/axios";

export type FeedbackState = {
  feedbackSubmissionOngoing: boolean;
  feedbackSubmissionError: ErrorMessage;
};

export interface FeedbackModule extends Module<FeedbackState, RootState> {
  mutations: {
    setFeedbackSubmissionOngoing(state: FeedbackState, payload: boolean): void;
    setFeedbackSubmissionError(
      state: FeedbackState,
      payload: ErrorMessage
    ): void;
    reset(state: FeedbackState, payload: null): void;
  };
  actions: {
    submitFeedback(
      { commit }: { commit: Commit },
      formData: FeedbackInsert
    ): Promise<void>;
  };
}

const defaultState: FeedbackState = {
  feedbackSubmissionOngoing: false,
  feedbackSubmissionError: null,
};

const feedback: FeedbackModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setFeedbackSubmissionOngoing(state: FeedbackState, payload: boolean) {
      state.feedbackSubmissionOngoing = payload;
    },
    setFeedbackSubmissionError(state: FeedbackState, payload: ErrorMessage) {
      state.feedbackSubmissionError = payload;
    },
    reset(state) {
      Object.assign(state, { ...defaultState });
    },
  },
  actions: {
    async submitFeedback({ commit }, formData): Promise<void> {
      commit("setFeedbackSubmissionError", null);
      commit("setFeedbackSubmissionOngoing", true);
      try {
        // @todo: add & show feedback response: github url to feedback ticket
        return (await client.feedbackPost(formData)).data;
      } catch (e) {
        commit("setFeedbackSubmissionError", getErrorMessage(e));
        return Promise.reject(e);
      } finally {
        commit("setFeedbackSubmissionOngoing", false);
      }
    },
  },
};

export default feedback;
