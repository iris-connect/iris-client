const messages = {
  error: {
    sessionExpired: "Ihre Sitzung ist abgelaufen",
    userNotFound: (id: string): string => `No User found for id: "${id}"`,
  },
};

export default messages;
