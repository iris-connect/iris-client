const rules = {
  defined: (v: unknown): string | boolean => !!v || "Pflichtfeld",
  minLength: (min: number | string) => (
    v: string | undefined
  ): string | boolean => {
    if (typeof v !== "string") return true; // we only check the length for strings
    return v.length <= 0 || v.length >= min || `Mindestens ${min} Zeichen`;
  },
};

export default rules;
