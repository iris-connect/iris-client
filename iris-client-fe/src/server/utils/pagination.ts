import { Page } from "@/api";
import { DataQuery } from "@/api/common";
import { DEFAULT_PAGE_SIZE } from "@/utils/pagination";
import _orderBy from "lodash/orderBy";
import _get from "lodash/get";
import _castArray from "lodash/castArray";

export enum TableSortDirection {
  ASC = "asc",
  DESC = "desc",
}
export type TableSort = {
  col: string;
  dir: TableSortDirection;
};

type Named = {
  name?: string;
};

export function paginated<T extends Named>(
  items: T[],
  page?: number | string
): Page<T> {
  const pageId = Number(page || 0);
  return {
    totalElements: items.length,
    content: items.map((r) => {
      return {
        ...r,
        name: `p${pageId}-${r.name}`,
      };
    }),
  };
}

export const queriedPage = <T, Q extends Partial<DataQuery>>(
  items: T[],
  query: Q
): Page<T> => {
  // @todo: add search functionality if possible
  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  const { page, size, sort, search, ...filters } = query;
  const qPage = Number(page || 0);
  const qSize = Number(size || DEFAULT_PAGE_SIZE);
  let sortedItems = items;
  if (sort) {
    const sortBy: string[] = [];
    const sortOrder: string[] = [];
    _castArray(sort).forEach((s) => {
      const qSort = s.split(",");
      sortBy.push(qSort[0]);
      sortOrder.push(qSort[1]);
    });
    sortedItems = _orderBy(items, sortBy, sortOrder as TableSortDirection[]);
  }
  let filteredItems = sortedItems;
  if (Object.keys(filters).length > 0) {
    filteredItems = sortedItems.filter((item) => {
      return Object.keys(filters).find((fKey) => {
        const iValue = _get(item, fKey);
        const fValue = _get(filters, fKey);
        if (fValue && iValue) {
          return _get(item, fKey) === _get(filters, fKey);
        }
        return true;
      });
    });
  }
  return {
    totalElements: filteredItems.length,
    totalPages: Math.ceil(filteredItems.length / qSize),
    size: qSize,
    number: qPage,
    content: filteredItems.slice(qPage * qSize, qPage * qSize + qSize),
  };
};
