import { UserList } from "@/api";
import { entryNormalizer, finalizeData } from "@/utils/data";
import { normalizeUser } from "@/views/user-login/user-login.data";

export const normalizeUserList = (
  source?: UserList,
  parse?: boolean
): UserList => {
  const normalizer = entryNormalizer(source);
  const users = normalizer("users", undefined, "array");
  const normalized = {
    users: users ? users.map((user) => normalizeUser(user)) : undefined,
  };
  return finalizeData(normalized, source, parse);
};
