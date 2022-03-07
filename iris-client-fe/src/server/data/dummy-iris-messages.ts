import { daysAgo } from "@/server/utils/date";
import {
  IrisMessageContext,
  IrisMessageDataAttachment,
  IrisMessageDataDiscriminator,
  IrisMessageDataViewData,
  IrisMessageDetails,
  IrisMessageFolder,
  IrisMessageHdContact,
  IrisMessageInsert,
} from "@/api";
import { Request } from "miragejs";
import { getDummyDetailsWithStatus } from "@/server/data/data-requests";
import { EventTrackingMessageDataImportSelection } from "@/modules/event-tracking/modules/message-data/services/normalizer";

export const dummyIrisMessageFolders: IrisMessageFolder[] = [
  {
    id: "inbox",
    name: "Posteingang",
    context: IrisMessageContext.Inbox,
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

export const dummyIrisMessageHdContacts: IrisMessageHdContact[] = [
  {
    id: "1",
    name: "Eigenes GA",
    own: true,
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

export const getDummyIrisMessageEventViewData = (
  messageDataId: string
): IrisMessageDataViewData => {
  const requestDetails = getDummyDetailsWithStatus("");
  return {
    discriminator: IrisMessageDataDiscriminator.EventTracking,
    id: messageDataId,
    payload: requestDetails,
  };
};

export const getDummyIrisMessageEventImportSelection = (
  messageDataId: string
): IrisMessageDataViewData => {
  const sourceEvent = getDummyDetailsWithStatus("");
  const guests = sourceEvent.submissionData?.guests || [];
  const payload: EventTrackingMessageDataImportSelection = {
    selectables: {
      guests: guests,
    },
    duplicates: {
      guests: [guests?.[0]?.messageDataSelectId || ""].filter((v) => v),
    },
  };
  return {
    discriminator: IrisMessageDataDiscriminator.EventTracking,
    id: messageDataId,
    payload,
  };
};

export const dummyIrisMessageData: IrisMessageDataAttachment = {
  id: "m1md1",
  discriminator: IrisMessageDataDiscriminator.EventTracking,
  isImported: false,
  description: "event tracking data attachment",
};

export const dummyIrisMessageList: IrisMessageDetails[] = [
  {
    hdAuthor: dummyIrisMessageHdContacts[1],
    hdRecipient: dummyIrisMessageHdContacts[0],
    folder: "inbox",
    context: IrisMessageContext.Inbox,
    id: "m1",
    subject: "Indexfall-Anfrage consetetur sadipscing elitr",
    body: "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
    createdAt: daysAgo(3),
    isRead: false,
    dataAttachments: [dummyIrisMessageData],
    attachmentCount: {
      total: 1,
      imported: 0,
    },
  },
  {
    hdAuthor: dummyIrisMessageHdContacts[0],
    hdRecipient: dummyIrisMessageHdContacts[4],
    folder: "outbox",
    context: IrisMessageContext.Outbox,
    id: "2",
    subject: "Austausch",
    body: "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
    createdAt: daysAgo(1),
    isRead: true,
  },
  {
    hdAuthor: dummyIrisMessageHdContacts[2],
    hdRecipient: dummyIrisMessageHdContacts[0],
    folder: "inbox_2_1",
    context: IrisMessageContext.Inbox,
    id: "5",
    subject: "Anfrage",
    body: "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
    createdAt: daysAgo(5),
    isRead: false,
  },
  {
    hdAuthor: dummyIrisMessageHdContacts[0],
    hdRecipient: dummyIrisMessageHdContacts[2],
    context: IrisMessageContext.Outbox,
    folder: "outbox",
    id: "asdf",
    subject: "Lorem ipsum gubergren, no sea takimata ",
    body: "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
    createdAt: daysAgo(2),
    isRead: true,
  },
  {
    hdAuthor: dummyIrisMessageHdContacts[3],
    hdRecipient: dummyIrisMessageHdContacts[0],
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
  const form: IrisMessageInsert = JSON.parse(request.requestBody);
  const subject = form.subject;
  const body = form.body;
  const recipient = form.hdRecipient;
  return {
    id: id || new Date().getTime() + "",
    subject,
    body,
    folder: "outbox",
    isRead: true,
    context: IrisMessageContext.Outbox,
    hdAuthor: dummyIrisMessageHdContacts[0],
    hdRecipient:
      dummyIrisMessageHdContacts.find((c) => c.id === recipient) ||
      dummyIrisMessageHdContacts[1],
    createdAt: new Date().getTime() + "",
  };
};
