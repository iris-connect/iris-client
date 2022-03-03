<template>
  <div v-bind="$attrs">
    <v-row :key="rowIndex" v-for="(row, rowIndex) in content">
      <v-col
        cols="12"
        :sm="12 / row.length"
        v-for="([title, info], index) in row"
        :key="index"
      >
        <p class="mb-0">
          <strong> {{ title }}: </strong>
        </p>
        <template v-if="info.length > 0">
          <template v-if="Array.isArray(info)">
            <span
              class="d-block"
              v-for="(item, itemIndex) in info"
              :key="itemIndex"
            >
              {{ item }}
            </span>
          </template>
          <template v-else>
            {{ info }}
          </template>
        </template>
        <span v-else class="d-block"> - </span>
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { PropType } from "vue";

export type InfoGridContent = Array<string | string[] | InfoGridContent>;

const InfoGridProps = Vue.extend({
  inheritAttrs: false,
  props: {
    content: {
      type: Array as PropType<InfoGridContent | null>,
      default: null,
    },
  },
});

@Component
export default class InfoGrid extends InfoGridProps {}
</script>
