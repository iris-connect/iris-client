import { User, UserRole } from "@/api";
import { normalizeData } from "@/utils/data";
import { normalizeMetaData } from "@/common/normalizer";

export const normalizeUser = (source?: User, parse?: boolean): User => {
  return normalizeData(
    source,
    (normalizer) => {
      return {
        id: normalizer("id", undefined),
        firstName: normalizer("firstName", undefined),
        lastName: normalizer("lastName", undefined),
        userName: normalizer("userName", ""),
        role: normalizer("role", UserRole.User),
        locked: normalizer("locked", false, "boolean"),
        ...normalizeMetaData(source),
      };
    },
    parse,
    "User"
  );
};
