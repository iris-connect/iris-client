export function paginated<T>(items: T[]) {
  return {
    page: 0,
    itemsPerPage: 15,
    numberOfPages: 3,
    totalElements: 43,
    content: items,
  };
}
