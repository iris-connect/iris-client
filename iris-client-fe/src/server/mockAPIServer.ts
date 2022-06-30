import {
  AuthenticationStatus,
  Credentials,
  DataRequestCaseClient,
  DataRequestCaseData,
  DataRequestDetails,
  DataRequestStatus,
  ExistingDataRequestClientWithLocation,
  IrisMessage,
  User,
  UserRole,
  VaccinationReport,
} from "@/api";
import { dummyLocations } from "@/server/data/dummy-locations";
import {
  dummyDataRequests,
  getDummyDetailsWithStatus,
} from "@/server/data/data-requests";
import { createServer, Request, Response } from "miragejs";
import {
  dummyDataRequestsCases,
  dummySubmissionUrl,
  getDummyDetailsCases,
} from "@/server/data/data-requests-cases";
import router from "@/router";
import {
  dummyUserList,
  getDummyUserFromRequest,
} from "@/server/data/dummy-userlist";
import { findIndex, remove, some } from "lodash";
import { paginated, queriedPage } from "@/server/utils/pagination";
import dayjs from "@/utils/date";
import _defaults from "lodash/defaults";
import {
  dummyCheckinApps,
  getDummyCheckinAppStatus,
} from "@/server/data/status-checkin-apps";
import {
  dummyIrisMessageFolders,
  dummyIrisMessageList,
  dummyIrisMessageHdContacts,
  getDummyMessageFromRequest,
  getDummyIrisMessageImportSelection,
  getDummyIrisMessageViewData,
  getDummyIrisMessageData,
} from "@/server/data/dummy-iris-messages";
import { DataQuery } from "@/api/common";
import { vaccinationReportList } from "@/server/data/vaccination-reports";
import store from "@/store";

const authResponse = <T>(
  request?: Request,
  data?: T,
  headers?: Record<string, string>
): Response => {
  if (!validateAuthentication()) {
    return new Response(401, { error: "not authorized", ...headers });
  }
  return new Response(200, headers, data);
};

const validateAuthentication = (): boolean => {
  return store.state.userLogin.session?.authenticated ?? false;
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
        if (
          credentials.userName === "admin" ||
          credentials.userName === "e2e_test_invalid_userName"
        ) {
          if (
            credentials.password === "auth" ||
            credentials.password === "e2e_test_invalid_password"
          ) {
            return new Response(401, {}, { message: "Unauthorized" });
          }
          if (credentials.password === "block") {
            const blockedUntil = dayjs().add(10, "seconds").toISOString();
            return new Response(
              401,
              {},
              {
                message: `User blocked! (${blockedUntil})`,
              }
            );
          }
        }
        store.commit(
          "mockApi/setAuthenticatedUserRole",
          credentials.userName === "user" ? UserRole.User : UserRole.Admin
        );
        return new Response(200, undefined, {
          authenticationStatus: AuthenticationStatus.AUTHENTICATED,
        });
      });

      this.post("/user/logout", () => {
        store.commit("mockApi/setAuthenticatedUserRole", null);
        return new Response(200);
      });

      this.get("/user-profile", (schema, request) => {
        const role =
          store.state.mockApi.authenticatedUserRole || UserRole.Admin;
        const user = dummyUserList.users?.find((usr) => {
          return usr.role === role;
        });
        return authResponse(request, user);
      });

      this.get("/users", (schema, request) => {
        return authResponse(request, dummyUserList);
      });

      this.post("/users", (schema, request) => {
        try {
          if (validateAuthentication()) {
            dummyUserList.users?.push(getDummyUserFromRequest(request));
          }
        } catch (e) {
          // ignored
        }
        return authResponse(request);
      });

      this.patch("/users/:id", (schema, request) => {
        try {
          if (validateAuthentication()) {
            const id = request.params.id;
            const users: Array<User> = dummyUserList.users || [];
            const index = findIndex(users, (user) => user.id === id);
            users[index] = _defaults(
              {},
              getDummyUserFromRequest(request, id),
              users[index]
            );
          }
        } catch (e) {
          return new Response(400, undefined, (e as Error).message);
        }
        return authResponse(request);
      });

      this.delete("/users/:id", (schema, request) => {
        if (validateAuthentication()) {
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

      this.post("/data-requests-client/events", (schema, request) => {
        const { locationId, ...data } = JSON.parse(request.requestBody);
        const created: Partial<DataRequestDetails> = {
          code: "NEWREQUEST_" + dayjs().valueOf(),
          ...data,
          locationInformation: dummyLocations.find(
            (location) => location.id === locationId
          ),
        };
        dummyDataRequests.push(created);
        return created;
      });

      this.get("/data-requests-client/events", (schema, request) => {
        const { page, status } = request.queryParams || {};
        return authResponse(
          request,
          paginated(
            dummyDataRequests.filter((item) =>
              status ? item.status === status : true
            ),
            page
          )
        );
      });

      this.get("/data-requests-client/events/:id", (schema, request) => {
        const data = getDummyDetailsWithStatus(router.currentRoute.params.id);
        return authResponse(request, data);
      });

      this.patch("/data-requests-client/events/:id", (schema, request) => {
        try {
          if (validateAuthentication()) {
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
        const data: DataRequestCaseClient = JSON.parse(request.requestBody);
        const created: DataRequestCaseData = {
          caseId: data.externalCaseId,
          status: DataRequestStatus.DataRequested,
          submissionUri: dummySubmissionUrl,
          ...data,
        };
        dummyDataRequestsCases.push(created);
        return authResponse(request, created);
      });

      this.get("/data-requests-client/cases", (schema, request) => {
        const { page, status } = request.queryParams || {};
        return authResponse(
          request,
          paginated(
            dummyDataRequestsCases.filter((item) =>
              status ? item.status === status : true
            ),
            page
          )
        );
      });

      this.get("/data-requests-client/cases/:caseId", (schema, request) => {
        const data = getDummyDetailsCases(router.currentRoute.params.caseId);
        return authResponse(request, data);
      });

      this.get("/search", (schema, request) => {
        let data;

        const searchQuery = request.queryParams?.search.toLowerCase();

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

        if (searchMatches(searchQuery, ["iris"])) {
          data = {
            locations: [dummyLocations[3]],
          };
        }

        return authResponse(request, {
          ...data,
          totalElements: dummyLocations.length,
          page: 1,
          size: 20,
        });
      });

      this.get("/status/checkin-apps", (schema, request) => {
        return authResponse(request, dummyCheckinApps);
      });

      this.get("/status/checkin-apps/:name", (schema, request) => {
        return authResponse(
          request,
          getDummyCheckinAppStatus(request.params.name)
        );
      });

      this.get("/iris-messages", (schema, request) => {
        const query: Partial<DataQuery> = request.queryParams || {};
        return authResponse(
          request,
          queriedPage(dummyIrisMessageList as IrisMessage[], query)
        );
      });

      this.get("/iris-messages/:messageId", (schema, request) => {
        const message = dummyIrisMessageList.find(
          (msg) => msg.id === request.params.messageId
        );
        return authResponse(request, message);
      });

      this.patch("/iris-messages/:messageId", (schema, request) => {
        const message = dummyIrisMessageList.find((msg) => {
          if (msg.id === request.params.messageId) {
            msg.isRead = true;
            return true;
          }
          return false;
        });
        return authResponse(request, message);
      });

      this.post("/iris-messages", (schema, request) => {
        try {
          if (validateAuthentication()) {
            dummyIrisMessageList.push(getDummyMessageFromRequest(request));
          }
        } catch (e) {
          // ignored
        }
        return authResponse(request);
      });

      this.get("/iris-messages/folders", (schema, request) => {
        return authResponse(request, dummyIrisMessageFolders);
      });

      this.get("/iris-messages/hd-contacts", (schema, request) => {
        return authResponse(request, dummyIrisMessageHdContacts);
      });

      this.get("/iris-messages/count/unread", (schema, request) => {
        return authResponse(
          request,
          dummyIrisMessageList.filter((item) => !item.isRead).length
        );
      });

      this.post("/iris-messages/data/:id/import", (schema, request) => {
        const messageData = getDummyIrisMessageData(request.params.id);
        messageData.isImported = true;
        return authResponse(request);
      });

      this.get(
        "/iris-messages/data/:id/import-selection-view",
        (schema, request) => {
          const id = request.params.id;
          return authResponse(request, getDummyIrisMessageImportSelection(id));
        }
      );

      this.get("/iris-messages/data/:id/view", (schema, request) => {
        const id = request.params.id;
        return authResponse(request, getDummyIrisMessageViewData(id));
      });

      this.get("/vaccination-reports", (schema, request) => {
        const query: Partial<DataQuery> = request.queryParams || {};
        return authResponse(
          request,
          queriedPage(vaccinationReportList as VaccinationReport[], query)
        );
      });

      this.get("/vaccination-reports/:id", (schema, request) => {
        const item = vaccinationReportList.find(
          (it) => it.id === request.params.id
        );
        return authResponse(request, item);
      });
    },
  });

  return server;
}

const searchMatches = (query: string, match: string[]): boolean => {
  return some(match, (item) => query.includes(item));
};
