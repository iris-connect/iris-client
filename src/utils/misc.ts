export const flattenDeep = (arr: Array<unknown>): Array<unknown> => {
  return arr.reduce<unknown[]>((acc, val) => {
    const value = Array.isArray(val) ? flattenDeep(val) : val;
    return acc.concat(value);
  }, []);
};

export const join = (arr: Array<unknown>, separator: string): string => {
  return flattenDeep(arr)
    .filter((n) => n)
    .join(separator);
};
