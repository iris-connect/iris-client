import asyncAction from "@/utils/asyncAction";
import { apiBundleProvider } from "@/utils/api";
import authClient from "@/api-client";
import { normalizeMfaConfig } from "@/modules/mfa/services/normalizer";

const fetchMfaConfig = () => {
  const action = async () => {
    return normalizeMfaConfig((await authClient.mfaConfigGet()).data, true);
  };
  return asyncAction(action);
};

const resetUsersMfaSecret = () => {
  const action = async (userId: string) => {
    return (await authClient.usersMfaSecretDelete(userId)).data;
  };
  return asyncAction(action);
};

export const mfaApi = {
  fetchMfaConfig,
  resetUsersMfaSecret,
};

export const bundleMfaApi = apiBundleProvider(mfaApi);
