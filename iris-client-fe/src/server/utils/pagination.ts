import { DataPage } from "@/api/common";

type Named = {
  name?: string;
};

export function paginated<T extends Named>(
  items: T[],
  page?: number | string
): DataPage<T> {
  const pageId = Number(page || 0);
  return {
    itemsPerPage: 15,
    totalElements: 43,
    content: items.map((r) => {
      return {
        ...r,
        externalCaseId: `p${pageId}-${r.name}`,
      };
    }),
  };
}
