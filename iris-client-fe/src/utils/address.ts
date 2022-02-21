import { join } from "@/utils/misc";
type AddressLike = {
  street?: string;
  houseNumber?: string;
  zipCode?: string;
  city?: string;
};

const sanitiseField = (text = ""): string => {
  return text.replace(/\s+/g, " ");
};

export const getFormattedAddress = (address?: AddressLike | null): string => {
  if (address) {
    return join(
      [
        join(
          [sanitiseField(address.street), sanitiseField(address.houseNumber)],
          " "
        ),
        join(
          [sanitiseField(address.zipCode), sanitiseField(address.city)],
          " "
        ),
      ],
      "\n"
    );
  }
  return "-";
};
