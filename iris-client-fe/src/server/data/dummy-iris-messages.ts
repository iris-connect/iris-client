import { daysAgo } from "@/server/utils/date";
import {
  IrisMessageContact,
  IrisMessageContext,
  IrisMessageDetails,
  IrisMessageFolder,
} from "@/api";
import { Request } from "miragejs";

export const dummyIrisMessageFolders: IrisMessageFolder[] = [
  {
    id: "inbox",
    name: "Posteingang",
    context: IrisMessageContext.Inbox,
    isDefault: true,
    items: [
      {
        id: "inbox_1",
        name: "Ordner 1",
        context: IrisMessageContext.Inbox,
      },
      {
        id: "inbox_2",
        name: "Ordner 2",
        context: IrisMessageContext.Inbox,
        items: [
          {
            id: "inbox_2_1",
            name: "Ordner 2 1",
            context: IrisMessageContext.Inbox,
          },
          {
            id: "inbox_2_2",
            name: "Ordner 2 2",
            context: IrisMessageContext.Inbox,
          },
        ],
      },
    ],
  },
  {
    id: "outbox",
    name: "Postausgang",
    context: IrisMessageContext.Outbox,
  },
];

export const dummyIrisMessageContacts: IrisMessageContact[] = [
  {
    id: "1",
    name: "Eigenes GA",
  },
  {
    id: "2",
    name: "Kontakt 2",
  },
  {
    id: "3",
    name: "Kontakt 3",
  },
  {
    id: "4",
    name: "Kontakt 4",
  },
  {
    id: "5",
    name: "Kontakt 5",
  },
];

export const dummyIrisMessageList: IrisMessageDetails[] = [
  {
    authorHd: dummyIrisMessageContacts[1],
    recipientHd: dummyIrisMessageContacts[0],
    folder: "inbox",
    context: IrisMessageContext.Inbox,
    id: "m1",
    subject: "Indexfall-Anfrage consetetur sadipscing elitr",
    body: "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
    createdAt: daysAgo(3),
    isRead: false,
    hasAttachments: true,
    attachments: [
      {
        name: "anhang 1",
        type: "pdf",
        link: "testlink",
      },
      {
        name: "Liste 2",
        type: "csv",
        link: "testlink_2",
      },
    ],
  },
  {
    authorHd: dummyIrisMessageContacts[0],
    recipientHd: dummyIrisMessageContacts[4],
    folder: "outbox",
    context: IrisMessageContext.Outbox,
    id: "2",
    subject: "Austausch",
    body: "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
    createdAt: daysAgo(1),
    isRead: true,
  },
  {
    authorHd: dummyIrisMessageContacts[2],
    recipientHd: dummyIrisMessageContacts[0],
    folder: "inbox_2_1",
    context: IrisMessageContext.Inbox,
    id: "5",
    subject: "Anfrage",
    body: "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
    createdAt: daysAgo(5),
    isRead: false,
  },

  {
    authorHd: dummyIrisMessageContacts[0],
    recipientHd: dummyIrisMessageContacts[2],
    context: IrisMessageContext.Outbox,
    folder: "outbox",
    id: "asdf",
    subject: "Lorem ipsum gubergren, no sea takimata ",
    body: "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
    createdAt: daysAgo(2),
    isRead: true,
  },
  {
    authorHd: dummyIrisMessageContacts[3],
    recipientHd: dummyIrisMessageContacts[0],
    context: IrisMessageContext.Inbox,
    folder: "inbox",
    id: "271",
    subject: "Test",
    body: "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et",
    createdAt: daysAgo(4),
    isRead: true,
  },
];

export const getDummyMessageFromRequest = (
  request: Request,
  id?: string
): IrisMessageDetails => {
  const form = request.requestBody as unknown as FormData;
  const subject = form.get("subject") as string;
  const body = form.get("body") as string;
  const recipient = form.get("recipient") as string;
  const attachments = form.getAll("attachments");
  return {
    id: id || new Date().getTime() + "",
    subject,
    body,
    folder: "outbox",
    context: IrisMessageContext.Outbox,
    authorHd: dummyIrisMessageContacts[0],
    recipientHd:
      dummyIrisMessageContacts.find((c) => c.id === recipient) ||
      dummyIrisMessageContacts[1],
    createdAt: new Date().getTime() + "",
    attachments: attachments.map((attachment) => {
      const a = attachment as File;
      return {
        name: a.name,
        type: a.type,
        link: a.name,
      };
    }),
  };
};
