import { apiBundleProvider } from "@/utils/api";
import { DataQuery } from "@/api/common";
import authClient from "@/api-client";
import asyncAction from "@/utils/asyncAction";

const fetchPageVaccinationRecord = () => {
  const action = async (query: DataQuery) => {
    return (await authClient.pageVaccinationRecordGet({ query })).data;
  };
  return asyncAction(action);
};

export const vaccinationRecordApi = {
  fetchPageVaccinationRecord,
};

export const bundleVaccinationRecordApi =
  apiBundleProvider(vaccinationRecordApi);
