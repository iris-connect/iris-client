import { LocationInformation } from "@/api";

export const dummyLocations: LocationInformation[] = [
  {
    id: "location-id-1",
    name: "location-one",
    providerId: "provider-1",
    contact: {
      address: {
        city: "c1",
        street: "s1",
        zip: "z1",
      },
      email: "mail1@example.com",
      phone: "123123",
      officialName: "one-name",
      ownerEmail: "owner-email@exmaple.com",
      representative: "rep1",
    },
  },
  {
    id: "location-id-2",
    name: "location-two",
    providerId: "provider-2",
    contact: {
      address: {
        city: "c2",
        street: "s2",
        zip: "z2",
      },
      email: "mail2@example.com",
      phone: "123123",
      officialName: "another-name",
      ownerEmail: "owner-email@exmaple.com",
      representative: "rep2",
    },
  },
];
