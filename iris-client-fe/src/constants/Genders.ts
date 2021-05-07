import { Sex } from "@/api";

const getName = function (sex: string): string {
  switch (sex) {
    case Sex.Male:
      return "männlich";
    case Sex.Female:
      return "weiblich";
    case Sex.Other:
      return "divers";
    default:
      return "unbekannt";
  }
};

const Genders = {
  getName,
};

export default Genders;
