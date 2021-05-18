import {DataRequestCaseDetails, PageIndexCase} from "@/api";
import client from "@/api-client";
import { RootState } from "@/store/types";

import { Commit, Module } from "vuex";

export type IndexTrackingListState = {
  indexTrackingList: Array<DataRequestCaseDetails> | null;
  indexTrackingListLoading: boolean;
  tableData: any;
};

export interface IndexTrackingListModule
  extends Module<IndexTrackingListState, RootState> {
  mutations: {
    setIndexTrackingList(
      state: IndexTrackingListState,
      indexTrackingList: Array<DataRequestCaseDetails> | null
    ): void;
    setIndexTrackingListLoading(
      state: IndexTrackingListState,
      payload: boolean
    ): void;
    setTableData(
        state: IndexTrackingListState,
        tableData: any
    ): void;
    reset(state: IndexTrackingListState, payload: null): void;
  };
  actions: {
    fetchIndexTrackingList({ commit }: { commit: Commit }, payload: null): Promise<void>;
  };
}

const defaultState: IndexTrackingListState = {
  indexTrackingList: null,
  indexTrackingListLoading: false,
  tableData: {
    search: "",
    page: 1,
    itemsPerPage: 5,
    numberOfPages: 1,
    totalElements: 0,
    headers: [
      {
        text: "Ext.ID",
        align: "start",
        sortable: true,
        value: "extID",
      },
      { text: "Index-Bezeichner", value: "name" },
      { text: "Zeit (Start)", value: "startTime" },
      { text: "Zeit (Ende)", value: "endTime" },
      { text: "Status", value: "status" },
      { text: "", value: "actions" },
    ],
  },
};

const indexTrackingList: IndexTrackingListModule = {
  namespaced: true,
  state() {
    return { ...defaultState };
  },
  mutations: {
    setIndexTrackingList(state, indexTrackingList) {
      state.indexTrackingList = indexTrackingList;
    },
    setIndexTrackingListLoading(state, loading) {
      state.indexTrackingListLoading = loading;
    },
    setTableData(state, tableData) {
      state.tableData = tableData;
    },
    reset(state) {
      // we can keep the data, no need to reset it
      // Object.assign(state, { ...defaultState });
      state.indexTrackingListLoading = false;
    }
  },
  actions: {
    async fetchIndexTrackingList({ commit }, page: any) {
      let indexTrackingList: PageIndexCase | null = null;
      const query = page ? generateQuery(page) : null;
      commit("setIndexTrackingListLoading", true);
      try {
        indexTrackingList = (await client.dataRequestClientCasesGet(query)).data;
        page.numberOfPages = indexTrackingList.totalPages;
        page.totalElements = indexTrackingList.totalElements;
      } finally {
        commit("setTableData", page);
        commit("setIndexTrackingList", indexTrackingList?.content);
        commit("setIndexTrackingListLoading", false);
      }
    },
  },
};

export type IndexDataQuery = {
  size: number,
  page: number,
  sort?: string,
  status?: string
  search?: string
}

function generateQuery(page: any) {
  const sortAttributes : { [key: string]: string; } = {
    extID: 'refId',
    name: 'name',
    startTime: 'requestStart',
    endTime: 'requestEnd',
    status: 'status'
  }
  const query : IndexDataQuery = {
    size: page.itemsPerPage,
    page: page.page - 1,
  };

  if ((page.sortBy && page.sortBy.length > 0)) query.sort = sortAttributes[page.sortBy[0]];
  if (query.sort && page.sortOrder && page.sortOrder.length > 0) page.sortOrder[0] ? query.sort = query.sort + ',desc' : query.sort = query.sort + ',asc'

  if (page.statusFilter) query.status = page.statusFilter;

  if(page.search && page.search !== '') query.search = page.search;

  return { query: query };
}

export default indexTrackingList;
