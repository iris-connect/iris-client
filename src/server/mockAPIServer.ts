import { DataRequestDetails } from "@/api";
import { DataRequestCaseDetails } from "@/api";
import { dummyLocations } from "@/server/data/dummy-locations";
import {
  dummyDataRequests,
  getDummyDetailsWithStatus,
} from "@/server/data/data-requests";
import {
  dummyDataRequestsCases,
  getDummyDetailsCases,
} from "@/server/data/data-requests-cases";
import { createServer } from "miragejs";
import router from "@/router";

// eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
export function makeMockAPIServer() {
  //console.log(router);
  const server = createServer({
    routes() {
      this.namespace = "";

      this.post("/data-requests-client/locations", () => {
        const created: Partial<DataRequestDetails> = {
          code: "NEWREQUEST123",
        };
        return created;
      });

      this.post("/data-request-client/cases", () => {
        const created: Partial<DataRequestCaseDetails> = {
          caseId: "NEWCASE123",
        };
        return created;
      });

      this.get("/data-requests-client/locations", () => {
        return dummyDataRequests;
      });

      this.get("/data-request-client/cases", () => {
        return dummyDataRequestsCases;
      });

      this.get("/data-requests-client/locations/:id", () => {
        return getDummyDetailsWithStatus(router.currentRoute.params.id);
      });

      this.get("/data-request-client/cases/:caseId", () => {
        console.log(router.currentRoute.params.caseId);
        return getDummyDetailsCases(router.currentRoute.params.caseId);
      });

      this.get("/search/mio", () => {
        return {
          locations: [dummyLocations[0]],
        };
      });

      this.get("/search/august", () => {
        return {
          locations: [dummyLocations[1]],
        };
      });
    },
  });

  return server;
}
