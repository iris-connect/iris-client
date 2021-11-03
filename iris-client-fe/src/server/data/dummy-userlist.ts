import { User, UserList, UserRole } from "@/api";
import { Request } from "miragejs";

export const dummyUserList: UserList = {
  users: [
    {
      id: "12345",
      lastName: "Mustermann",
      firstName: "Max",
      userName: "MaxMuster",
      role: UserRole.Admin,
    },
    {
      id: "abcdef",
      lastName: "Musterfrau",
      firstName: "Lisa",
      userName: "LisaMuster",
      role: UserRole.User,
    },
    {
      id: "67890",
      lastName: "Person",
      firstName: "Test",
      userName: "TestUser",
      role: UserRole.User,
    },
    {
      id: "321654",
      lastName: "E2EUser",
      firstName: "E2ETest",
      userName: "E2ETestUser",
      role: UserRole.User,
    },
  ],
};

export const getDummyUserFromRequest = (
  request: Request,
  id?: string
): User => {
  const { firstName, lastName, userName, role } = JSON.parse(
    request.requestBody
  );
  return {
    id: id || new Date().getTime() + "",
    firstName,
    lastName,
    userName,
    role,
  };
};
