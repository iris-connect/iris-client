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
          @query:update="handleQueryUpdate"
          #default="{ query }"
        >
          <iris-message-folders-data-tree
            :folders="folders"
            :loading="foldersLoading"
            v-model="query.folder"
            @input="handleFolderChange($event)"
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
        </data-query-handler>
      </v-card-text>
    </v-card>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import store from "@/store";
import SearchField from "@/components/pageable/search-field.vue";
import { IrisMessageFolder } from "@/api";
import DataTree from "@/components/data-tree/data-tree.vue";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import IrisMessageFoldersDataTree from "@/views/iris-message-list/components/iris-message-folders-data-tree.vue";
import IrisMessageDataTable from "@/views/iris-message-list/components/iris-message-data-table.vue";
import { DataQuery } from "@/api/common";
import DataQueryHandler from "@/components/pageable/data-query-handler.vue";

@Component({
  components: {
    DataQueryHandler,
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

  handleFolderChange() {
    this.$store.commit("irisMessageList/setMessageList", null);
  }

  handleQueryUpdate(newValue: DataQuery) {
    if (newValue.folder) {
      this.$store.dispatch("irisMessageList/fetchMessages", newValue);
    } else {
      this.$store.commit("irisMessageList/setMessageList", null);
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
}
</script>
