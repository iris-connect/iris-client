import { dummyLocations } from "@/server/data/dummy-locations";
import { createServer } from "miragejs";

// eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
export function makeMockAPIServer() {
  const server = createServer({
    routes() {
      this.namespace = "api";

      this.post("/data-requests", () => {
        return {
          locations: dummyLocations,
        };
      });

      this.get("/data-requests", () => {
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
