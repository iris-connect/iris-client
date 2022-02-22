import { apiBundleProvider } from "@/utils/api";
import { DataQuery } from "@/api/common";
import authClient from "@/api-client";
import asyncAction from "@/utils/asyncAction";
import {
  normalizePageVaccinationRecord,
  normalizeVaccinationRecordDetails,
} from "@/modules/vaccination-record/normalizer";

const fetchPageVaccinationRecord = () => {
  const action = async (query: DataQuery) => {
    return normalizePageVaccinationRecord(
      (await authClient.pageVaccinationRecordGet({ query })).data,
      true
    );
  };
  return asyncAction(action);
};

const fetchVaccinationRecordDetails = () => {
  const action = async (recordId: string) => {
    return normalizeVaccinationRecordDetails(
      (await authClient.vaccinationRecordDetailsGet(recordId)).data,
      true
    );
  };
  return asyncAction(action);
};

export const vaccinationRecordApi = {
  fetchPageVaccinationRecord,
  fetchVaccinationRecordDetails,
};

export const bundleVaccinationRecordApi =
  apiBundleProvider(vaccinationRecordApi);
