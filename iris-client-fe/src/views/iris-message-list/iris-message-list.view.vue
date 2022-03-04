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
        <data-query-handler
          ref="queryHandler"
          @query:update="messageQuery = $event"
          #default="{ query }"
        >
          <iris-message-folders-data-tree
            :folders="messageApi.fetchMessageFolders.state.result"
            :loading="messageApi.fetchMessageFolders.state.loading"
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
                :message-list="messageApi.fetchMessages.state.result"
                :loading="messageApi.fetchMessages.state.loading"
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
        </data-query-handler>
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import SearchField from "@/components/pageable/search-field.vue";
import DataTree from "@/components/data-tree/data-tree.vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import IrisMessageFoldersDataTree from "@/views/iris-message-list/components/iris-message-folders-data-tree.vue";
import IrisMessageDataTable from "@/views/iris-message-list/components/iris-message-data-table.vue";
import { DataQuery } from "@/api/common";
import DataQueryHandler from "@/components/pageable/data-query-handler.vue";
import { getApiErrorMessages } from "@/utils/api";
import {
  bundleIrisMessageApi,
  fetchUnreadMessageCountApi,
} from "@/modules/iris-message/services/api";
import { IrisMessageContext, IrisMessageFolder } from "@/api";

@Component({
  components: {
    DataQueryHandler,
    IrisMessageDataTable,
    IrisMessageFoldersDataTree,
    ErrorMessageAlert,
    DataTree,
    SearchField,
  },
})
export default class IrisMessageListView extends Vue {
  messageApi = bundleIrisMessageApi(["fetchMessages", "fetchMessageFolders"]);

  mounted() {
    this.messageApi.fetchMessageFolders.execute();
    fetchUnreadMessageCountApi.execute();
  }

  get errors() {
    return getApiErrorMessages(this.messageApi);
  }

  messageQuery: DataQuery | null = null;

  get unreadMessageCountLoading() {
    return fetchUnreadMessageCountApi.state.loading;
  }
  @Watch("unreadMessageCountLoading")
  onUnreadMessageCountLoadingChange(newValue: boolean) {
    if (!newValue && !this.messageApi.fetchMessages.state.loading) {
      if (this.currentMessageContext === IrisMessageContext.Inbox) {
        this.fetchMessages(this.messageQuery);
      }
    }
  }

  @Watch("messageQuery", { immediate: true, deep: true })
  onQueryChange(newValue?: DataQuery) {
    if (newValue) {
      this.fetchMessages(newValue);
    }
  }

  get currentMessageContext(): IrisMessageContext {
    const folders: IrisMessageFolder[] | null =
      this.messageApi.fetchMessageFolders.state.result;
    const currentFolder = folders?.find(
      (folder) => folder.id === this.messageQuery?.folder
    );
    return currentFolder?.context || IrisMessageContext.Unknown;
  }

  fetchMessages(query: DataQuery | null) {
    if (query?.folder) {
      this.messageApi.fetchMessages.execute(query);
    } else {
      this.messageApi.fetchMessages.reset(["result"]);
    }
  }

  handleRowClick(row: { id: string }) {
    this.$router.push({
      name: "iris-message-details",
      params: { messageId: row.id },
    });
  }
}
</script>
