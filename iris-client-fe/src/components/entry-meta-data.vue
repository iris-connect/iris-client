<template>
  <div class="caption mb-3 text-right" v-if="hasMetaData">
    <template v-for="(row, index) in rows">
      <span v-if="notEmpty(row)" :key="index" class="d-block">
        {{ row[0] }}
        <span v-if="row[1]">
          von <strong>{{ row[1] }}</strong>
        </span>
        {{ getRelativeTime(row[2], ["", "am"], "L LT", "") }}
      </span>
    </template>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";
import { MetaData } from "@/api";
import { getRelativeTime } from "@/utils/date";
import _some from "lodash/some";

const EntryMetaDataProps = Vue.extend({
  props: {
    entry: {
      type: Object as PropType<MetaData | null>,
      default: null,
    },
  },
});

@Component
export default class EntryMetaData extends EntryMetaDataProps {
  getRelativeTime = getRelativeTime;
  get rows() {
    return [
      ["Erstellt", this.entry?.createdBy, this.entry?.createdAt],
      [
        "Zuletzt geändert",
        this.entry?.lastModifiedBy,
        this.entry?.lastModifiedAt,
      ],
    ];
  }
  notEmpty(row: string[]) {
    return row.slice(1).filter((v) => v).length > 0;
  }
  get hasMetaData() {
    return _some(this.rows, this.notEmpty);
  }
}
</script>
