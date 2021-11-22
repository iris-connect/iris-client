<template>
  <div>
    <iris-message-folders-data-tree
      :folders="folders"
      :loading="foldersLoading"
      v-model="query.folder"
    >
      <template #data-table="{ context }">
        <iris-message-list
          :message-list="$store.state.irisMessageList.messageList"
          :loading="$store.state.irisMessageList.messageListLoading"
          :context="context"
          :search.sync="query.search"
          :sort.sync="query.sort"
          :page.sync="query.page"
          :size.sync="query.size"
        />
      </template>
    </iris-message-folders-data-tree>
    <error-message-alert :errors="errors" />
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
import IrisMessageList from "@/views/iris-message-list/components/iris-message-list.vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import IrisMessageFoldersDataTree from "@/views/iris-message-list/components/iris-message-folders-data-tree.vue";

@Component({
  components: {
    IrisMessageFoldersDataTree,
    ErrorMessageAlert,
    IrisMessageList,
    DataTree,
    SearchField,
  },
  beforeRouteEnter(to, from, next) {
    store.dispatch("irisMessageList/fetchMessageFolders");
    next();
  },
})
export default class IrisMessageListView extends Vue {
  get errors() {
    return [
      this.$store.state.irisMessageList.messageListError,
      this.$store.state.irisMessageList.messageFoldersError,
    ];
  }

  query: IrisMessageQuery = {
    size: getPageSizeFromRouteWithDefault(this.$route),
    page: getPageFromRouteWithDefault(this.$route),
    sort: getStringParamFromRouteWithOptionalFallback("sort", this.$route),
    search: getStringParamFromRouteWithOptionalFallback("search", this.$route),
    folder: getStringParamFromRouteWithOptionalFallback("folder", this.$route),
  };

  @Watch("query", { deep: true, immediate: true })
  onQueryChange(newValue: IrisMessageQuery) {
    this.updateRoute(newValue);
    this.$store.dispatch("irisMessageList/fetchMessages", newValue);
  }

  @Watch("query.folder", { immediate: true })
  onFolderChange() {
    this.$store.commit("irisMessageList/setMessageList", null);
    this.query = {
      ...this.query,
      page: 1,
      sort: undefined,
      search: undefined,
    };
  }

  get foldersLoading(): boolean {
    return this.$store.state.irisMessageList.messageFoldersLoading;
  }
  get folders(): IrisMessageFolder[] | null {
    return this.$store.state.irisMessageList.messageFolders;
  }

  updateRoute(query: Record<string, unknown>): void {
    this.$router
      .replace({
        name: this.$route.name as string | undefined,
        query: {
          ...this.$route.query,
          page: "1",
          ...query,
        },
      })
      .catch(() => {
        // ignored
      });
  }
}
</script>
