import { DataRequestDetails } from "@/api";
import { dummyLocations } from "@/server/data/dummy-locations";
import { createServer } from "miragejs";

// eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
export function makeMockAPIServer() {
  const server = createServer({
    routes() {
      this.namespace = "";

      this.post("/data-requests-client/locations", () => {
        const created: Partial<DataRequestDetails> = {
          code: "NEWREQUEST123",
        };
        return created;
      });

      this.get("/data-requests-client/locations", () => {
        return {
          locations: dummyLocations,
        };
      });

      this.get("/search", () => {
        return {
          locations: dummyLocations,
        };
      });
    },
  });

  return server;
}
