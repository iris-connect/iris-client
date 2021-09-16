import { User, UserRole } from "@/api";
import { Complete, normalizeData } from "@/utils/data";

export const normalizeUser = (source?: User, parse?: boolean): User => {
  return normalizeData(
    source,
    (normalizer) => {
      const normalized: Complete<User> = {
        id: normalizer("id", undefined),
        firstName: normalizer("firstName", undefined),
        lastName: normalizer("lastName", undefined),
        userName: normalizer("userName", ""),
        role: normalizer("role", UserRole.User),
      };
      return normalized;
    },
    parse,
    "User"
  );
};
