import { IrisMessageDataDiscriminator } from "@/api";

const getLabel = (discriminator: IrisMessageDataDiscriminator): string => {
  switch (discriminator) {
    case IrisMessageDataDiscriminator.EventTracking:
      return "Ereignis";
    default:
      return "Ungültiger Datentyp";
  }
};

const Discriminators = {
  getLabel,
};

export default Discriminators;
