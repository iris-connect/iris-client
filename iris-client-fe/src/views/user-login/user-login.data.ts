import { User, UserRole } from "@/api";
import { entryNormalizer, finalizeData } from "@/utils/data";

export const normalizeUser = (source?: User, parse?: boolean): User => {
  const normalizer = entryNormalizer(source);
  const normalized = {
    id: normalizer("id", undefined),
    firstName: normalizer("firstName", undefined),
    lastName: normalizer("lastName", undefined),
    userName: normalizer("userName", ""),
    role: normalizer("role", UserRole.User),
  };
  return finalizeData(normalized, source, parse);
};
