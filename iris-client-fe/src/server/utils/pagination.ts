import { PageEvent, PageIndexCase } from "@/api";

type Named = {
  name?: string;
};

export function paginated<T extends Named>(
  items: T[],
  page?: number | string
): PageEvent | PageIndexCase {
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
