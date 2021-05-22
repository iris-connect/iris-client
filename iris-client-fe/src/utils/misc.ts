import { flattenDeep } from "lodash";
import { Route } from "vue-router";
import { DataRequestStatus } from "@/api";

export const join = (arr: Array<unknown>, separator: string): string => {
  return flattenDeep(arr)
    .filter((n) => n)
    .join(separator);
};

export function getStringParamFromRouteWithOptionalFallback(
  param: "page" | "sort" | "search" | "status" | "size",
  route: Route,
  fallback?: string
): string | undefined {
  const v = route.query[param] as string | undefined;
  return v || fallback;
}

export function getPageSizeFromRouteWithDefault(route: Route) {
  const fallback = "15";
  return Number(
    getStringParamFromRouteWithOptionalFallback("size", route, fallback)
  );
}

export function getPageFromRouteWithDefault(route: Route) {
  const fallback = "1";
  return Number(
    getStringParamFromRouteWithOptionalFallback("page", route, fallback)
  );
}

export function getStatusFilterFromRoute(route: Route) {
  const s = getStringParamFromRouteWithOptionalFallback("status", route);
  if (!s) {
    return null;
  }
  return s as DataRequestStatus;
}
