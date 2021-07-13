import config from "@/config";
import { some } from "lodash";

const minLength = (min: number) => (v?: string): string | boolean => {
  if (typeof v !== "string") return true; // we only check the length for strings
  return v.length <= 0 || v.length >= min || `Mindestens ${min} Zeichen`;
};

const defined = (v: unknown): string | boolean => !!v || "Pflichtfeld";

const sanitised = (v?: string): string | boolean => {
  if (typeof v !== "string") return true; // we only check the length for strings
  if (v.length <= 0) return true;

  if (v.includes("<script")) {
    return "Aus Sicherheitsgründen ist die Sequenz <script nicht erlaubt. Bitte modifiziere deine Eingabe.";
  }

  if (
    startMatches(v, [
      "=",
      "<",
      ">",
      "!",
      '"',
      "§",
      "$",
      "%",
      "&",
      "/",
      "(",
      ")",
      "?",
      "´",
      "`",
      "¿",
      "≠",
      "¯",
      "}",
      "·",
      "{",
      "˜",
      "\\",
      "]",
      "^",
      "ﬁ",
      "[",
      "¢",
      "¶",
      "“",
      "¡",
      "¬",
      "”",
      "#",
      "£",
      "+",
      "*",
      "±",
      "",
      "‘",
      "’",
      "'",
      "-",
      "_",
      ".",
      ":",
      "…",
      "÷",
      "∞",
      ";",
      "˛",
      "æ",
      "Æ",
      "œ",
      "Œ",
      "@",
      "•",
      "°",
      "„",
    ])
  ) {
    return "Aus Sicherheitsgründen ist ein Spezialcharakter nicht als erstes Symbol erlaubt. Bitte modifiziere deine Eingabe.";
  }

  return true;
};

const startMatches = (query: string, match: string[]): boolean => {
  return some(match, (item) => query.startsWith(item));
};

const rules = {
  defined,
  minLength,
  password: minLength(config.passwordMinLength),
  sanitised,
};

export default rules;
