<template>
  <div>
    <slot v-bind="{ query }" />
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";
import { DataQuery, getSortAttribute } from "@/api/common";
import {
  DEFAULT_PAGE_SIZE,
  getPageFromRouteWithDefault,
  getPageSizeFromRouteWithDefault,
  getStatusFilterFromRoute,
  getStringParamFromRouteWithOptionalFallback,
} from "@/utils/pagination";
import _mapValues from "lodash/mapValues";
import { join } from "@/utils/misc";

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
        size: getPageSizeFromRouteWithDefault(this.$route),
        page: Math.max(0, getPageFromRouteWithDefault(this.$route) - 1),
        status: getStatusFilterFromRoute(this.$route),
        sort: getStringParamFromRouteWithOptionalFallback("sort", this.$route),
        search: getStringParamFromRouteWithOptionalFallback(
          "search",
          this.$route
        ),
        folder: getStringParamFromRouteWithOptionalFallback(
          "folder",
          this.$route
        ),
      };
    }
    this.initialized = true;
  }

  @Watch("query", { immediate: true, deep: true })
  onQueryChange(newValue: DataQuery) {
    if (this.routeControl && !this.initialized) return;
    let sort = newValue.sort;
    if (sort) {
      const sortModel = sort.split(",");
      sort = join(
        [getSortAttribute(sortModel[0]) || sortModel[0], sortModel[1]],
        ","
      );
    }
    const query = {
      ...newValue,
      sort,
    };
    if (this.routeControl) {
      this.updateRoute(query);
    }
    this.$emit("query:update", query);
  }

  updateRoute(query: DataQuery): void {
    const routeQuery: Record<string, unknown> = {
      ...this.$route.query,
      ...query,
      page: `${(query?.page || 0) + 1}`,
    };
    this.$router
      .replace({
        name: this.$route.name as string | undefined,
        query: _mapValues(routeQuery, (val) => {
          return val ? `${val}` : undefined;
        }),
      })
      .catch(() => {
        // ignored
      });
  }
}
</script>
