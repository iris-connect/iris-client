import { DataRequestCaseDetails, DataRequestDetails, User } from "@/api";
import { dummyLocations } from "@/server/data/dummy-locations";
import {
  dummyDataRequests,
  getDummyDetailsWithStatus,
} from "@/server/data/data-requests";
import { createServer, Request, Response } from "miragejs";
import {
  dummyDataRequestsCases,
  getDummyDetailsCases,
} from "@/server/data/data-requests-cases";
import router from "@/router";
import {
  dummyUserList,
  getDummyUserFromRequest,
} from "@/server/data/dummy-userlist";
import { remove, findIndex } from "lodash";

// @todo: find better solution for data type
const authResponse = (
  request?: Request,
  // eslint-disable-next-line @typescript-eslint/ban-types
  data?: string | {} | undefined
): Response => {
  if (request) {
    if (!validateAuthHeader(request)) {
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

const validateAuthHeader = (request: Request): boolean => {
  const authHeader = request?.requestHeaders?.Authorization;
  return !!authHeader;
};

// eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
export function makeMockAPIServer() {
  const server = createServer({
    routes() {
      this.namespace = "";

      this.post("/login", () => {
        return authResponse();
      });

      this.get("/user-profile", (schema, request) => {
        const user = dummyUserList.users?.[0];
        return authResponse(request, user);
      });

      this.get("/users", (schema, request) => {
        return authResponse(request, dummyUserList);
      });

      this.post("/users", (schema, request) => {
        try {
          if (validateAuthHeader(request)) {
            dummyUserList.users?.push(getDummyUserFromRequest(request));
          }
        } catch (e) {
          // ignored
        }
        return authResponse(request);
      });

      this.patch("/users/:id", (schema, request) => {
        try {
          if (validateAuthHeader(request)) {
            const id = request.params.id;
            const users: Array<User> = dummyUserList.users || [];
            const index = findIndex(users, (user) => user.id === id);
            users[index] = getDummyUserFromRequest(request, id);
          }
        } catch (e) {
          // ignored
        }
        return authResponse(request);
      });

      this.delete("/users/:id", (schema, request) => {
        if (validateAuthHeader(request)) {
          try {
            const id = request.params.id;
            const users: Array<User> = dummyUserList.users || [];
            remove(users, (item: User) => item.id === id);
          } catch (e) {
            // ignored
          }
        }
        return authResponse(request);
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

      this.post("/data-request-client/cases", () => {
        const created: Partial<DataRequestCaseDetails> = {
          caseId: "NEWCASE123",
        };
        return created;
      });

      this.get("/data-request-client/cases", () => {
        return dummyDataRequestsCases;
      });

      this.get("/data-request-client/cases/:caseId", () => {
        return getDummyDetailsCases(router.currentRoute.params.caseId);
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
