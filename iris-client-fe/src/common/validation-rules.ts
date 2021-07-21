import config from "@/config";
import dayjs from "@/utils/date";
import { some } from "lodash";

const minLength = (min: number) => (v?: string): string | boolean => {
  if (typeof v !== "string") return true; // we only check the length for strings
  return v.length <= 0 || v.length >= min || `Mindestens ${min} Zeichen`;
};

const defined = (v: unknown): string | boolean => !!v || "Pflichtfeld";

const location = (v: unknown): string | boolean =>
  !!v || "Bitte wählen Sie einen Ereignisort aus";

const sanitised = (v?: string): string | boolean => {
  if (typeof v !== "string") return true; // we only check the length for strings
  if (v.length <= 0) return true;

  if (v.includes("<script")) {
    return "Aus Sicherheitsgründen ist die Sequenz <script nicht erlaubt. Bitte modifiziere deine Eingabe.";
  }

  if (v.includes("SELECT") && v.includes("FROM")) {
    return "Aus Sicherheitsgründen ist die Sequenz SELECT .... FROM ... nicht erlaubt. Bitte modifiziere deine Eingabe.";
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

const anyMatches = (query: string, match: string[]): boolean => {
  return some(match, (item) => query.includes(item));
};

const nameConventions = (v: string): string | boolean => {
  if (
    anyMatches(v, [
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
      "_",
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
    return "Aus Sicherheitsgründen ist die Verwendung von Spezialcharakter in Namen eingeschränkt.";
  }

  return true;
};

const dateStart = (v: string): string | boolean => {
  return (
    dayjs(v).isSameOrBefore(dayjs(), "minute") ||
    "Bitte geben Sie einen Zeitpunkt in der Vergangenheit an"
  );
};

const dateEnd = (start: string) => (v: string): string | boolean => {
  if (!start) return true;
  return (
    dayjs(v).isSameOrAfter(dayjs(start), "minute") ||
    "Bitte geben Sie einen Zeitpunkt an, der nach dem Beginn liegt"
  );
};

const rules = {
  defined,
  minLength,
  password: minLength(config.passwordMinLength),
  sanitised,
  nameConventions,
  dateStart,
  dateEnd,
  location,
};

export default rules;
