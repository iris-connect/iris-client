<template>
  <div>
    <sortable-data-table
      v-if="context"
      class="mt-5"
      v-bind="{ ...dataTable, ...$attrs }"
      v-on="$listeners"
      :item-class="itemClass"
    >
      <template v-slot:header.hasAttachments>
        <v-icon dense>mdi-paperclip</v-icon>
      </template>
      <template v-slot:item.hasAttachments="{ item }">
        <v-icon dense v-if="item.hasAttachments">mdi-paperclip</v-icon>
      </template>
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
  },
});

@Component({
  components: {
    SortableDataTable,
  },
})
export default class IrisMessageDataTable extends IrisMessageDataTableProps {
  get tableHeaders(): DataTableHeader[] {
    if (this.context === IrisMessageContext.Inbox) {
      return [
        { text: "", value: "hasAttachments", sortable: false, width: 0 },
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
        { text: "", value: "hasAttachments", sortable: false, width: 0 },
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
        hasAttachments: message.hasAttachments,
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
