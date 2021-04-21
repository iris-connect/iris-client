import { UserList, UserRole } from "@/api";

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
      lastName: "Nutzer",
      firstName: "Test",
      userName: "TestUser",
      role: UserRole.User,
    },
  ],
};
