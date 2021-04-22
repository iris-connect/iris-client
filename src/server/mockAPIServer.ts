import { DataRequestDetails } from "@/api";
import { dummyLocations } from "@/server/data/dummy-locations";
import {
  dummyDataRequests,
  getDummyDetailsWithStatus,
} from "@/server/data/data-requests";
import { createServer, Request, Response } from "miragejs";
import router from "@/router";

// @todo: find better solution for data type
const authResponse = (
  request?: Request,
  // eslint-disable-next-line @typescript-eslint/ban-types
  data?: string | {} | undefined
): Response => {
  if (request) {
    const authHeader = request?.requestHeaders?.Authorization;
    if (!authHeader) {
      return new Response(401, { error: "not authorized" });
    }
  }
  return new Response(
    200,
    {
      "Authentication-Info": "Bearer TOKEN123",
    },
    data
  );
};

// eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
export function makeMockAPIServer() {
  const server = createServer({
    routes() {
      this.namespace = "";

      this.post("/login", () => {
        return authResponse();
      });

      this.post("/data-requests-client/locations", () => {
        const created: Partial<DataRequestDetails> = {
          code: "NEWREQUEST123",
        };
        return created;
      });

      this.get("/data-requests-client/locations", (schema, request) => {
        return authResponse(request, dummyDataRequests);
      });

      this.get("/data-requests-client/locations/:id", (schema, request) => {
        const data = getDummyDetailsWithStatus(router.currentRoute.params.id);
        return authResponse(request, data);
      });

      this.get("/search/mio", (schema, request) => {
        const data = {
          locations: [dummyLocations[0]],
        };
        return authResponse(request, data);
      });

      this.get("/search/august", (schema, request) => {
        const data = {
          locations: [dummyLocations[1]],
        };
        return authResponse(request, data);
      });
    },
  });

  return server;
}
