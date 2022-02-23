import { apiBundleProvider } from "@/utils/api";
import { DataQuery } from "@/api/common";
import authClient from "@/api-client";
import asyncAction from "@/utils/asyncAction";
import {
  normalizePageVaccinationReport,
  normalizeVaccinationReportDetails,
} from "@/modules/vaccination-report/normalizer";

const fetchPageVaccinationReport = () => {
  const action = async (query: DataQuery) => {
    return normalizePageVaccinationReport(
      (await authClient.pageVaccinationReportGet({ query })).data,
      true
    );
  };
  return asyncAction(action);
};

const fetchVaccinationReportDetails = () => {
  const action = async (id: string) => {
    return normalizeVaccinationReportDetails(
      (await authClient.vaccinationReportDetailsGet(id)).data,
      true
    );
  };
  return asyncAction(action);
};

export const vaccinationReportApi = {
  fetchPageVaccinationReport,
  fetchVaccinationReportDetails,
};

export const bundleVaccinationReportApi =
  apiBundleProvider(vaccinationReportApi);
