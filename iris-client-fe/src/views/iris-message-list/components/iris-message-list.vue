<template>
  <div>
    <div v-if="context">
      <search-field :value="search" @input="$emit('update:search', $event)" />
      <iris-message-data-table
        class="mt-5"
        v-bind="dataTable"
        :sort.sync="tableSort"
        :page="page"
        @update:page="$emit('update:page', $event)"
        :items-per-page="size"
        @update:items-per-page="$emit('update:size', $event)"
        :footer-props="{ 'items-per-page-options': [10, 20, 30, 50] }"
        :item-class="itemClass"
      />
    </div>
    <div v-else>Bitte wählen Sie einen Ordner aus</div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { TableSort } from "@/server/utils/pagination";
import SearchField from "@/components/pageable/search-field.vue";
import IrisMessageDataTable, {
  getSortDir,
} from "@/views/iris-message-list/components/iris-message-data-table.vue";
import { IrisMessageContext, PageIrisMessages } from "@/api";
import { DataTableHeader } from "vuetify";
import { getFormattedDate } from "@/utils/date";
import { PropType } from "vue";

const IrisMessageListProps = Vue.extend({
  inheritAttrs: false,
  props: {
    messageList: {
      type: Object as PropType<PageIrisMessages | null>,
      default: null,
    },
    loading: {
      type: Boolean,
      default: false,
    },
    context: {
      type: String as PropType<IrisMessageContext | null>,
      default: null,
    },
    search: {
      type: String,
      default: "",
    },
    sort: {
      type: String,
      default: "",
    },
    page: {
      type: Number,
      default: 1,
    },
    size: {
      type: Number,
      default: 0,
    },
  },
});

@Component({
  components: {
    IrisMessageDataTable,
    SearchField,
  },
})
export default class IrisMessageList extends IrisMessageListProps {
  get tableHeaders(): DataTableHeader[] {
    if (this.context === IrisMessageContext.Inbox) {
      return [
        { text: "Von", value: "author", sortable: true },
        {
          text: "Betreff",
          value: "subject",
          sortable: true,
        },
        { text: "Datum", value: "createdAt", sortable: true },
      ];
    }
    if (this.context === IrisMessageContext.Outbox) {
      return [
        { text: "An", value: "recipient", sortable: true },
        {
          text: "Betreff",
          value: "subject",
          sortable: true,
        },
        { text: "Datum", value: "createdAt", sortable: true },
      ];
    }
    return [];
  }
  get dataTable() {
    const items = (this.messageList?.content || []).map((message) => {
      return {
        author: message.author || "-",
        recipient: message.recipient || "-",
        subject: message.subject || "-",
        createdAt: getFormattedDate(message.createdAt, "L LT"),
        isRead: message.isRead,
      };
    });
    return {
      loading: this.loading,
      serverItemsLength: this.messageList?.totalElements || 0,
      items: items,
      headers: this.tableHeaders,
    };
  }

  get tableSort(): TableSort | null {
    const sort = this.sort;
    const sortArgs = sort.split(",");
    const col = sortArgs[0];
    const dir = getSortDir(sortArgs[1]);
    if (col && dir) {
      return { col, dir };
    }
    return null;
  }

  set tableSort(value: TableSort | null) {
    this.$emit(
      "update:sort",
      value ? [value.col, value.dir].join(",") : undefined
    );
  }

  itemClass(item: { isRead: boolean }) {
    return item.isRead ? "" : "font-weight-bold";
  }
}
</script>
