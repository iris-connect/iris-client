import { flattenDeep } from "lodash";

export const join = (arr: Array<unknown>, separator: string): string => {
  return flattenDeep(arr)
    .filter((n) => n)
    .join(separator);
};

export const getValidPhoneNumber = (
  ...numbers: Array<string | undefined>
): string | undefined => {
  const validNumber = numbers.find(isPossiblePhoneNumber);
  return validNumber ? validNumber : numbers[0];
};

const isPossiblePhoneNumber = (phoneNumber?: string): boolean => {
  if (!phoneNumber) return false;
  const number = phoneNumber.replace(/[\s\-_+#*.,:;()/|]/g, "");
  return /^\d+$/.test(number);
};
