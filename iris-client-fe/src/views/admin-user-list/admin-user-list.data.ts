import { UserList } from "@/api";
import { Complete, normalizeData } from "@/utils/data";
import { normalizeUser } from "@/views/user-login/user-login.data";

export const normalizeUserList = (
  source?: UserList,
  parse?: boolean
): UserList => {
  return normalizeData(
    source,
    (normalizer) => {
      const users = normalizer("users", undefined, "array");
      const normalized: Complete<UserList> = {
        users: users ? users.map((user) => normalizeUser(user)) : undefined,
      };
      return normalized;
    },
    parse,
    "UserList"
  );
};
