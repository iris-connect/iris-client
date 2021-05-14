import { LocationInformation } from "@/api";

export const dummyLocations: LocationInformation[] = [
  {
    id: "id-pizzeria-muster",
    name: "Pizzeria Mio Muster",
    providerId: "recover-app",
    contact: {
      address: {
        city: "Frankfurt am Main",
        street: "Musterstraße. 3",
        zip: "36054",
      },
      email: "pizza-muster@example.com",
      phone: "06973928393",
      officialName: "Pizzeria Mio Muster GmbH",
      ownerEmail: "legal-pizza-muster@exmaple.com",
      representative: "Luigi Calzone",
    },
  },
  {
    id: "id-brauwirt-keller",
    name: "Brauwirt-Keller Pub",
    providerId: "recover-app",
    contact: {
      address: {
        city: "München",
        street: "Brauwirtstraße 12",
        zip: "12345",
      },
      email: "brauwirt@example.com",
      phone: "06973928394",
      officialName: "Brauwirt-Keller",
      ownerEmail: "brauwirt-keller@exmaple.com",
      representative: "Maximilian Brauwirt",
    },
  },
  {
    id: "id-bowling-bahn",
    name: "Bowling World",
    providerId: "recover-app",
    contact: {
      address: {
        city: "Musterstadt",
        street: "Bowlinger Weg 53a",
        zip: "54321",
      },
      email: "bowling@example.com",
      phone: "06973928395",
      officialName: "Bowling World Musterstadt",
      ownerEmail: "bowling@exmaple.com",
      representative: "Martina Superbowl",
    },
  },
];
