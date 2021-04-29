import { Sex } from "@/api";

const getName = function (sex: string): string {
  switch (sex) {
    case Sex.Male:
      return "m";
    case Sex.Female:
      return "w";
    case Sex.Other:
      return "d";
    default:
      return "Unbekannt";
  }
};

const Genders = {
  getName,
};

export default Genders;
