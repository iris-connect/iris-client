import { apiBundleProvider } from "@/utils/api";
import { DataQuery } from "@/api/common";
import authClient from "@/api-client";
import asyncAction from "@/utils/asyncAction";

//@todo: add data normalizer
const fetchPageVaccinationRecord = () => {
  const action = async (query: DataQuery) => {
    return (await authClient.pageVaccinationRecordGet({ query })).data;
  };
  return asyncAction(action);
};

//@todo: add data normalizer
const fetchVaccinationRecord = () => {
  const action = async (recordId: string) => {
    return (await authClient.vaccinationRecordGet(recordId)).data;
  };
  return asyncAction(action);
};

export const vaccinationRecordApi = {
  fetchPageVaccinationRecord,
  fetchVaccinationRecord,
};

export const bundleVaccinationRecordApi =
  apiBundleProvider(vaccinationRecordApi);
