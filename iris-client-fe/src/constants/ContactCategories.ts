import { ContactCategory } from "@/api";

const getCategory = function (
  contactCategory: ContactCategory | null | undefined
): string {
  switch (contactCategory) {
    case ContactCategory.NoRisk:
      return "Kein Risiko";
    case ContactCategory.LowRisk:
      return "Niedriges Risiko";
    case ContactCategory.MediumRiskMed:
      return "Mittleres Risiko";
    case ContactCategory.HighRiskMed:
      return "Erhöhtes Risiko";
    case ContactCategory.HighRisk:
      return "Hohes Risiko";
    default:
      return "Unbekannt"; // TODO find better name
  }
};

const ContactCategories = {
  getCategory,
};

export default ContactCategories;
