import { LocationInformation } from "@/api";

export const dummyLocations: LocationInformation[] = [
  {
    id: "id-pizzeria-mio",
    name: "Pizzeria Mio",
    providerId: "recover-app",
    contact: {
      address: {
        city: "Frankfurt am Main",
        street: "Dreieichstr. 3",
        zip: "360594",
      },
      email: "pizza-mio@example.com",
      phone: "06973928393",
      officialName: "Pizzeria Mio",
      ownerEmail: "legal-pizza-mio@exmaple.com",
      representative: "Luigi Calzone",
    },
  },
  {
    id: "id-augustiner-keller",
    name: "Augustiner-Keller",
    providerId: "recover-app",
    contact: {
      address: {
        city: "München",
        street: "Arnulfstraße 52",
        zip: "80335",
      },
      email: "augustiner@example.com",
      phone: "089594393",
      officialName: "Augustiner-Keller",
      ownerEmail: "legal-augustiner@exmaple.com",
      representative: "Hans Joachim Jakob",
    },
  },
];
