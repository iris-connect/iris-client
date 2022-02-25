<template>
  <div>
    <sortable-data-table
      v-if="context"
      class="mt-5"
      v-bind="{ ...dataTable, ...$attrs }"
      v-on="listeners"
      :page.sync="tablePage"
      :footer-props="{ 'items-per-page-options': [10, 20, 30, 50] }"
      :item-class="itemClass"
    >
      <template v-for="(_, slot) of $scopedSlots" v-slot:[slot]="scope">
        <slot :name="slot" v-bind="scope" />
      </template>
    </sortable-data-table>
    <div v-else>Bitte wählen Sie einen Ordner aus</div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { IrisMessageContext, PageIrisMessages } from "@/api";
import { DataTableHeader } from "vuetify";
import { getFormattedDate } from "@/utils/date";
import { PropType } from "vue";
import SortableDataTable from "@/components/sortable-data-table.vue";
import _omit from "lodash/omit";

const IrisMessageDataTableProps = Vue.extend({
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
    page: {
      type: Number,
      default: 0,
    },
  },
});

@Component({
  components: {
    SortableDataTable,
  },
})
export default class IrisMessageDataTable extends IrisMessageDataTableProps {
  get listeners(): Record<string, unknown> {
    return _omit(this.$listeners, ["update:page"]);
  }
  get tablePage(): number {
    return this.page + 1;
  }
  set tablePage(value: number) {
    this.$emit("update:page", Math.max(0, value - 1));
  }
  get tableHeaders(): DataTableHeader[] {
    if (this.context === IrisMessageContext.Inbox) {
      return [
        { text: "Von", value: "hdAuthor.name", sortable: true },
        {
          text: "Betreff",
          value: "subject",
          sortable: true,
        },
        { text: "Datum", value: "metadata.created", sortable: true },
      ];
    }
    if (this.context === IrisMessageContext.Outbox) {
      return [
        { text: "An", value: "hdRecipient.name", sortable: true },
        {
          text: "Betreff",
          value: "subject",
          sortable: true,
        },
        { text: "Datum", value: "metadata.created", sortable: true },
      ];
    }
    return [];
  }
  get dataTable() {
    const items = (this.messageList?.content || []).map((message) => {
      return {
        id: message.id,
        hdAuthor: message.hdAuthor || "-",
        hdRecipient: message.hdRecipient || "-",
        subject: message.subject || "-",
        metadata: {
          created: getFormattedDate(message.createdAt, "L LT"),
        },
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
  itemClass(item: { isRead: boolean }) {
    return item.isRead ? "cursor-pointer" : "cursor-pointer font-weight-bold";
  }
}
</script>
