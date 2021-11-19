import { Route } from "vue-router";
import { DataRequestStatus } from "@/api";

export const DEFAULT_PAGE_SIZE = 20;

export function getStringParamFromRouteWithOptionalFallback(
  param: "page" | "sort" | "search" | "status" | "size",
  route: Route,
  fallback?: string
): string | undefined {
  const v = route.query[param] as string | undefined;
  return v || fallback;
}

export function getPageSizeFromRouteWithDefault(route: Route): number {
  const fallback = `${DEFAULT_PAGE_SIZE}`;
  return Number(
    getStringParamFromRouteWithOptionalFallback("size", route, fallback)
  );
}

export function getPageFromRouteWithDefault(route: Route): number {
  const fallback = "1";
  return Number(
    getStringParamFromRouteWithOptionalFallback("page", route, fallback)
  );
}

export function getStatusFilterFromRoute(
  route: Route
): DataRequestStatus | undefined {
  const s = getStringParamFromRouteWithOptionalFallback("status", route);
  if (!s) {
    return;
  }
  return s as DataRequestStatus;
}
