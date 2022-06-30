import { MfaConfig, MfaOption } from "@/api";
import { normalizeData } from "@/utils/data";

export const normalizeMfaConfig = (source?: MfaConfig, parse?: boolean) => {
  return normalizeData(
    source,
    (normalizer) => {
      return {
        mfaOption: normalizer("mfaOption", MfaOption.DISABLED),
      };
    },
    parse,
    "MfaConfig"
  );
};
