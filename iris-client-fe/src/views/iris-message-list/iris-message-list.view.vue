<template>
  <div>
    <div class="pb-6 text-right">
      <v-btn
        color="primary"
        :to="{ name: 'iris-message-create' }"
        data-test="view.link.create"
      >
        Nachricht schreiben
      </v-btn>
    </div>
    <v-card>
      <v-card-title>Nachrichten</v-card-title>
      <v-card-text>
        <iris-message-folders-data-tree
          :folders="folders"
          :loading="foldersLoading"
          v-model="query.folder"
        >
          <template #data-table="{ context }">
            <search-field
              :disabled="!context"
              v-model="query.search"
              data-test="search"
            />
            <iris-message-data-table
              :context="context"
              :message-list="messageList"
              :loading="messageListLoading"
              :search.sync="query.search"
              :sort.sync="query.sort"
              :page.sync="query.page"
              :items-per-page.sync="query.size"
              @click:row="handleRowClick"
              data-test="view.data-table"
            />
          </template>
        </iris-message-folders-data-tree>
        <error-message-alert :errors="errors" />
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import store from "@/store";
import {
  getPageFromRouteWithDefault,
  getPageSizeFromRouteWithDefault,
  getStringParamFromRouteWithOptionalFallback,
} from "@/utils/pagination";
import SearchField from "@/components/pageable/search-field.vue";
import { IrisMessageFolder, IrisMessageQuery } from "@/api";
import DataTree from "@/components/data-tree/data-tree.vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import IrisMessageFoldersDataTree from "@/views/iris-message-list/components/iris-message-folders-data-tree.vue";
import IrisMessageDataTable from "@/views/iris-message-list/components/iris-message-data-table.vue";
import _mapValues from "lodash/mapValues";

@Component({
  components: {
    IrisMessageDataTable,
    IrisMessageFoldersDataTree,
    ErrorMessageAlert,
    DataTree,
    SearchField,
  },
  beforeRouteEnter(to, from, next) {
    store.dispatch("irisMessageList/fetchMessageFolders");
    store.dispatch("irisMessageList/fetchUnreadMessageCount");
    next();
  },
})
export default class IrisMessageListView extends Vue {
  get errors() {
    return [
      this.$store.state.irisMessageList.messageListLoadingError,
      this.$store.state.irisMessageList.messageFoldersLoadingError,
    ];
  }

  query: IrisMessageQuery = {
    size: getPageSizeFromRouteWithDefault(this.$route),
    page: Math.max(0, getPageFromRouteWithDefault(this.$route) - 1),
    sort: getStringParamFromRouteWithOptionalFallback("sort", this.$route),
    search: getStringParamFromRouteWithOptionalFallback("search", this.$route),
    folder: getStringParamFromRouteWithOptionalFallback("folder", this.$route),
  };

  @Watch("query.folder", { immediate: true })
  onFolderChange() {
    this.$store.commit("irisMessageList/setMessageList", null);
    this.query = {
      ...this.query,
      page: 0,
      sort: undefined,
      search: undefined,
    };
  }

  @Watch("query", { immediate: true, deep: true })
  onQueryChange(newValue: IrisMessageQuery) {
    this.updateRoute(newValue);
    if (newValue.folder) {
      this.$store.dispatch("irisMessageList/fetchMessages", newValue);
    }
  }

  get messageList(): IrisMessageFolder[] | null {
    return this.$store.state.irisMessageList.messageList;
  }
  get messageListLoading(): boolean {
    return this.$store.state.irisMessageList.messageListLoading;
  }

  get folders(): IrisMessageFolder[] | null {
    return this.$store.state.irisMessageList.messageFolders;
  }
  get foldersLoading(): boolean {
    return this.$store.state.irisMessageList.messageFoldersLoading;
  }

  handleRowClick(row: { id: string }) {
    this.$router.push({
      name: "iris-message-details",
      params: { messageId: row.id },
    });
  }

  updateRoute(query: IrisMessageQuery): void {
    const routeQuery: Record<string, unknown> = {
      ...this.$route.query,
      ...query,
      page: `${(query?.page || 0) + 1}`,
    };
    this.$router
      .replace({
        name: this.$route.name as string | undefined,
        query: _mapValues(routeQuery, (val) => {
          return val ? `${val}` : undefined;
        }),
      })
      .catch(() => {
        // ignored
      });
  }
}
</script>
