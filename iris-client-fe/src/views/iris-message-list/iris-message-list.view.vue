<template>
  <v-row>
    <v-col cols="3">
      <data-tree :item="{ items: folders }" v-model="query.folder" />
    </v-col>
    <v-col cols="9">
      <div>
        <search-field v-model="query.search" />
        <iris-message-data-table
          class="mt-5"
          v-bind="dataTable"
          :sort.sync="sort"
          :page.sync="query.page"
          :items-per-page.sync="query.size"
          :footer-props="{ 'items-per-page-options': [10, 20, 30, 50] }"
          :item-class="itemClass"
        />
      </div>
    </v-col>
  </v-row>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import store from "@/store";
import {
  getPageFromRouteWithDefault,
  getPageSizeFromRouteWithDefault,
  getStringParamFromRouteWithOptionalFallback,
} from "@/utils/pagination";
import { getFormattedDate } from "@/utils/date";
import SearchField from "@/components/pageable/search-field.vue";
import IrisMessageDataTable, {
  getSortDir,
  TableSort,
} from "@/views/iris-message-list/components/iris-message-data-table.vue";
import {
  IrisMessage,
  IrisMessageContext,
  IrisMessageFolder,
  IrisMessageQuery,
} from "@/api";
import DataTree from "@/components/data-tree/data-tree.vue";

type TableRowInbox = Pick<
  IrisMessage,
  "author" | "subject" | "createdAt" | "isRead"
>;

@Component({
  components: {
    IrisMessageDataTable,
    DataTree,
    SearchField,
  },
  beforeRouteEnter(to, from, next) {
    store.dispatch("irisMessageList/fetchMessageFolders");
    next();
  },
})
export default class IrisMessageListView extends Vue {
  // @todo: split data tables: inbox / outbox or use one data table and reinitialize it if context is switched
  get dataTable() {
    const { messageList, messageListLoading } = store.state.irisMessageList;
    const items: TableRowInbox[] = (messageList?.content || []).map(
      (message) => {
        return {
          author: message.author || "-",
          recipient: message.recipient || "-",
          subject: message.subject || "-",
          createdAt: getFormattedDate(message.createdAt, "L LT"),
          isRead: message.isRead,
        };
      }
    );
    return {
      loading: messageListLoading,
      serverItemsLength: messageList?.totalElements || 0,
      items: items,
      headers: [
        { text: "Von", value: "author", sortable: true },
        {
          text: "Betreff",
          value: "subject",
          sortable: true,
        },
        { text: "Datum", value: "createdAt", sortable: true },
      ],
    };
  }

  get sort(): TableSort | null {
    const sort = this.query.sort;
    if (typeof sort === "string") {
      const sortArgs = sort.split(",");
      const col = sortArgs[0];
      const dir = getSortDir(sortArgs[1]);
      if (col && dir) {
        return { col, dir };
      }
    }
    return null;
  }

  set sort(value: TableSort | null) {
    this.query.sort = value ? [value.col, value.dir].join(",") : undefined;
  }

  query: IrisMessageQuery = {
    size: getPageSizeFromRouteWithDefault(this.$route),
    page: getPageFromRouteWithDefault(this.$route),
    sort: getStringParamFromRouteWithOptionalFallback("sort", this.$route),
    search: getStringParamFromRouteWithOptionalFallback("search", this.$route),
    folder: "inbox",
  };

  @Watch("query", { deep: true, immediate: true })
  onQueryChange(newValue: IrisMessageQuery) {
    this.updateRoute(newValue);
    this.$store.dispatch("irisMessageList/fetchMessages", newValue);
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

  itemClass(item: TableRowInbox) {
    return item.isRead ? "" : "font-weight-bold";
  }

  get foldersLoading(): boolean {
    return this.$store.state.irisMessageList.messageFoldersLoading;
  }
  get folders(): IrisMessageFolder[] | null {
    return this.$store.state.irisMessageList.messageFolders;
  }

  get messageContext(): IrisMessageContext | null {
    const folder = (this.folders || []).find((f) => f.id === this.query.folder);
    return folder?.context || null;
  }
}
</script>
