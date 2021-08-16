import {
  Credentials,
  DataRequestCaseDetails,
  DataRequestDetails,
  ExistingDataRequestClientWithLocation,
  User,
} from "@/api";
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
import { remove, findIndex, some } from "lodash";
import { paginated } from "@/server/utils/pagination";
import dayjs from "@/utils/date";

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

      /**
       * You can simulate the "username blocked due to too many invalid login attempts" behaviour with the following credentials:
       * username: admin
       * password:
       * - auth: default / non blocking login error: 401 "Unauthorized"
       * - block: blocking login error: 401 "User blocked! (dateTime ISOString)"
       */
      this.post("/login", (schema, request) => {
        const credentials: Credentials = JSON.parse(request.requestBody);
        if (credentials.userName === "admin") {
          if (credentials.password === "auth") {
            return new Response(401, {}, { message: "Unauthorized" });
          }
          if (credentials.password === "block") {
            const blockedUntil = dayjs().add(10, "minutes").toISOString();
            return new Response(
              401,
              {},
              {
                message: `User blocked! (${blockedUntil})`,
              }
            );
          }
        }
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

      this.post("/data-requests-client/events", () => {
        const created: Partial<DataRequestDetails> = {
          code: "NEWREQUEST123",
        };
        return created;
      });

      this.get("/data-requests-client/events", (schema, request) => {
        const { page } = request.queryParams;
        return authResponse(request, paginated(dummyDataRequests, page));
      });

      this.get("/data-requests-client/events/:id", (schema, request) => {
        const data = getDummyDetailsWithStatus(router.currentRoute.params.id);
        return authResponse(request, data);
      });

      this.patch("/data-requests-client/events/:id", (schema, request) => {
        try {
          if (validateAuthHeader(request)) {
            const id = request.params.id;
            const dataRequest = dummyDataRequests.find(
              (entry: ExistingDataRequestClientWithLocation) =>
                entry.code === id
            );
            if (dataRequest) {
              Object.assign(dataRequest, JSON.parse(request.requestBody));
            }
          }
        } catch (e) {
          // ignored
        }
        return authResponse(request);
      });

      this.post("/data-requests-client/cases", (schema, request) => {
        const created: Partial<DataRequestCaseDetails> = {
          caseId: "NEWCASE123",
        };
        return authResponse(request, created);
      });

      this.get("/data-requests-client/cases", (schema, request) => {
        const { page } = request.queryParams;
        return authResponse(request, paginated(dummyDataRequestsCases, page));
      });

      this.get("/data-requests-client/cases/:caseId", (schema, request) => {
        const data = getDummyDetailsCases(router.currentRoute.params.caseId);
        return authResponse(request, data);
      });

      this.get("/search", (schema, request) => {
        let data;

        const searchQuery = request.queryParams.search.toLowerCase();

        if (searchMatches(searchQuery, ["pizza", "musterstraße", "mio"])) {
          data = {
            locations: [dummyLocations[0]],
          };
        }

        if (searchMatches(searchQuery, ["brau", "münchen"])) {
          data = {
            locations: [dummyLocations[1]],
          };
        }

        if (searchMatches(searchQuery, ["muster"])) {
          data = {
            locations: [dummyLocations[0], dummyLocations[2]],
          };
        }

        if (searchMatches(searchQuery, ["bowl", "musterstadt"])) {
          data = {
            locations: [dummyLocations[2]],
          };
        }

        return authResponse(request, data);
      });
    },
  });

  return server;
}

const searchMatches = (query: string, match: string[]): boolean => {
  return some(match, (item) => query.includes(item));
};
