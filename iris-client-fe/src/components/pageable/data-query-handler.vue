<template>
  <div>
    <slot v-bind="{ query }" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { DataQuery, mapSortAttributes } from "@/api/common";
import { DEFAULT_PAGE_SIZE, getParamFromRoute } from "@/utils/pagination";
import _mapValues from "lodash/mapValues";
import { Location } from "vue-router";

const DataQueryHandlerProps = Vue.extend({
  inheritAttrs: false,
  props: {
    routeControl: {
      type: Boolean,
      default: true,
    },
  },
});

@Component
export default class DataQueryHandler extends DataQueryHandlerProps {
  initialized = false;

  query: DataQuery = {
    size: DEFAULT_PAGE_SIZE,
    page: 0,
    sort: undefined,
    search: undefined,
    status: undefined,
    folder: undefined,
  };

  mounted() {
    if (this.routeControl) {
      this.query = {
        size: getParamFromRoute("size", this.$route) || DEFAULT_PAGE_SIZE,
        page: Math.max(0, (getParamFromRoute("page", this.$route) || 1) - 1),
        status: getParamFromRoute("status", this.$route),
        sort: getParamFromRoute("sort", this.$route),
        search: getParamFromRoute("search", this.$route),
        folder: getParamFromRoute("folder", this.$route),
      };
    }
    this.initialized = true;
  }

  @Watch("query", { immediate: true, deep: true })
  onQueryChange(newValue: DataQuery) {
    if (this.routeControl && !this.initialized) return;
    const query = {
      ...newValue,
      sort: mapSortAttributes(newValue.sort),
    };
    if (this.routeControl) {
      this.updateLocation(query);
    }
    this.$emit("query:update", query);
  }

  updateLocation(query: DataQuery): void {
    const locationQuery: Location["query"] = _mapValues(
      {
        ...this.$route.query,
        ...query,
        page: `${(query?.page || 0) + 1}`,
      },
      (val) => {
        if (Array.isArray(val)) return val;
        return val ? `${val}` : undefined;
      }
    );
    this.$router
      .replace({
        name: this.$route.name || undefined,
        query: locationQuery,
      })
      .catch(() => {
        // ignored
      });
  }
}
</script>
