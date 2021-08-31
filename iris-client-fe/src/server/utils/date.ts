export function daysAgo(days = 0, date = new Date().toISOString()): string {
  const d = new Date(date);
  d.setDate(d.getDate() - days);
  return d.toISOString();
}

export function hoursAgo(hours = 0, date = new Date().toISOString()): string {
  const d = new Date(date);
  d.setHours(d.getHours() - hours);
  return d.toISOString();
}
