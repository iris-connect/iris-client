import config from "@/config";

const minLength = (min: number) => (v?: string): string | boolean => {
  if (typeof v !== "string") return true; // we only check the length for strings
  return v.length <= 0 || v.length >= min || `Mindestens ${min} Zeichen`;
};

const defined = (v: unknown): string | boolean => !!v || "Pflichtfeld";

const rules = {
  defined,
  minLength,
  password: minLength(config.passwordMinLength),
};

export default rules;
