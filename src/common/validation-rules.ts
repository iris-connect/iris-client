const rules = {
  defined: (v: unknown): string | boolean => !!v || "Pflichtfeld",
  minLength: (min: number | string) => (v: string): string | boolean =>
    v.length <= 0 || v.length >= min || `Mindestens ${min} Zeichen`,
};

export default rules;
