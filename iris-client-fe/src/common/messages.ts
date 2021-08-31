const messages = {
  error: {
    sessionExpired: "Ihre Sitzung ist abgelaufen",
    accessDenied:
      "Sie haben nicht die erforderliche Berechtigung, um diese Aktion auszuführen",
    userNotFound: (id: string): string => `No User found for id: "${id}"`,
  },
};

export default messages;
