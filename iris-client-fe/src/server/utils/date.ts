import dayjs from "./../../utils/date";
import { ManipulateType } from "dayjs";

export const timeAgo = (
  value = 0,
  unit: ManipulateType,
  date = new Date().toISOString()
): string => {
  return dayjs(date).subtract(value, unit).toISOString();
};

export function daysAgo(days = 0, date = new Date().toISOString()): string {
  return timeAgo(days, "days", date);
}

export function hoursAgo(hours = 0, date = new Date().toISOString()): string {
  return timeAgo(hours, "hours", date);
}
