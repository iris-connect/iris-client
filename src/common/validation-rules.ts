const rules = {
  defined: [(v: unknown): string | boolean => !!v || "Pflichtfeld"],
};

export default rules;
