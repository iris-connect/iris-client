import { Route } from "vue-router";
import { DataRequestStatus } from "@/api";

export const DEFAULT_PAGE_SIZE = 20;
export const DEFAULT_ITEMS_PER_PAGE_OPTIONS = [10, 20, 30, 50];

type QueryParams = {
  page: number;
  size: number;
  sort: string | string[];
  search: string;
  folder: string;
  status: DataRequestStatus;
};

export const getParamFromRoute = <T extends keyof QueryParams>(
  param: T,
  route: Route
): QueryParams[T] | undefined => {
  let value: unknown = route.query[param];
  if (["page", "size"].indexOf(param) !== -1) {
    value = Number(value);
  }
  return value as QueryParams[T];
};

export function getStringParamFromRouteWithOptionalFallback(
  param: keyof QueryParams,
  route: Route,
  fallback?: string
): string | undefined {
  return `${getParamFromRoute(param, route)}` || fallback;
}

export function getPageSizeFromRouteWithDefault(route: Route): number {
  return getParamFromRoute("size", route) || DEFAULT_PAGE_SIZE;
}

export function getPageFromRouteWithDefault(route: Route): number {
  return getParamFromRoute("page", route) || 1;
}

export function getStatusFilterFromRoute(
  route: Route
): DataRequestStatus | undefined {
  return getParamFromRoute("status", route);
}
