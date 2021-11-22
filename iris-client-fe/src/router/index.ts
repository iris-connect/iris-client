import store from "@/store";
import Vue from "vue";
import VueRouter, {
  Location,
  NavigationGuardNext,
  Route,
  RouteConfig,
} from "vue-router";
import Home from "../views/home/Home.vue";
import { getAuthenticatedUser } from "@/views/user-login/utils/store";
import { UserRole } from "@/api";
import dayjs from "@/utils/date";

Vue.use(VueRouter);

// TODO create router config which exports these constants

export const routes: Array<RouteConfig> = [
  {
    path: "/",
    name: "dashboard" /* Caution: This acts as an identifier! */,
    meta: {
      menu: true,
      menuName: "Dashboard",
      menuExact: true,
    },
    component: Home,
  },
  {
    path: "/user/login",
    name: "user-login",
    meta: {
      menu: false,
      auth: false,
    },
    component: () =>
      import(
        /* webpackChunkName: "user-login" */ "../views/user-login/user-login.view.vue"
      ),
  },
  {
    path: "/admin/user/list",
    name: "admin-user-list",
    meta: {
      menu: false,
      admin: true,
    },
    component: () =>
      import(
        /* webpackChunkName: "admin-user-list" */ "../views/admin-user-list/admin-user-list.view.vue"
      ),
  },
  {
    path: "/admin/user/create",
    name: "admin-user-create",
    meta: {
      menu: false,
      admin: true,
    },
    component: () =>
      import(
        /* webpackChunkName: "admin-user-create" */ "../views/admin-user-create/admin-user-create.view.vue"
      ),
  },
  {
    path: "/admin/user/edit/:id",
    name: "admin-user-edit",
    beforeEnter: async (
      to: Route,
      from: Route,
      next: NavigationGuardNext
    ): Promise<void> => {
      const user = await getAuthenticatedUser();
      const accessGranted =
        user?.role === UserRole.Admin ||
        (user?.role === UserRole.User && user.id === to.params.id);
      if (accessGranted) {
        next();
      } else {
        next("/");
      }
    },
    meta: {
      menu: false,
    },
    component: () =>
      import(
        /* webpackChunkName: "admin-user-edit" */ "../views/admin-user-edit/admin-user-edit.view.vue"
      ),
  },
  {
    path: "/checkin-app-status/list",
    name: "checkin-app-status-list",
    meta: {
      menu: false,
    },
    component: () =>
      import(
        /* webpackChunkName: "checkin-app-status-list" */ "../views/checkin-app-status-list/checkin-app-status-list.view.vue"
      ),
  },
  {
    path: "/events/new",
    name: "event-new",
    meta: {
      menu: false,
    },
    component: () =>
      import(
        /* webpackChunkName: "event-tracking-form" */ "../views/event-tracking-form/event-tracking-form.view.vue"
      ),
  },
  {
    path: "/cases/new",
    name: "index-new" /* Caution: This acts as an identifier! */,
    meta: {
      menu: false,
    },
    component: () =>
      import(
        /* webpackChunkName: "index-tracking-form" */ "../views/index-tracking-form/index-tracking-form.view.vue"
      ),
  },
  {
    path: "/events/list",
    name: "event-list" /* Caution: This acts as an identifier! */,
    meta: {
      menu: true,
      menuName: "Ereignisse",
    },
    component: () =>
      import(
        /* webpackChunkName: "event-tracking-list" */ "../views/event-tracking-list/event-tracking-list.view.vue"
      ),
  },
  {
    path: "/cases/list",
    name: "index-list" /* Caution: This acts as an identifier! */,
    meta: {
      menu: true,
      menuName: "Indexfälle",
    },
    component: () =>
      import(
        /* webpackChunkName: "index-tracking-list" */ "../views/index-tracking-list/index-tracking-list.view.vue"
      ),
  },
  {
    path: "/events/details/:id",
    name: "event-details" /* Caution: This acts as an identifier! */,
    meta: {
      menu: false,
    },
    component: () =>
      import(
        /* webpackChunkName: "event-tracking-details" */ "../views/event-tracking-details/event-tracking-details.view.vue"
      ),
  },
  {
    path: "/cases/details/:caseId",
    name: "index-details" /* Caution: This acts as an identifier! */,
    meta: {
      menu: false,
    },
    component: () =>
      import(
        /* webpackChunkName: "index-tracking-details" */ "../views/index-tracking-details/index-tracking-details.view.vue"
      ),
  },
  {
    path: "/iris-messages/list",
    name: "iris-message-list" /* Caution: This acts as an identifier! */,
    meta: {
      menu: true,
      menuName: "Nachrichten",
      menuComponent: () =>
        import(
          /* webpackChunkName: "iris-message-list-nav-link" */ "../views/iris-message-list/components/iris-message-list-nav-link.vue"
        ),
    },
    component: () =>
      import(
        /* webpackChunkName: "iris-message-list" */ "../views/iris-message-list/iris-message-list.view.vue"
      ),
  },
  {
    path: "/iris-messages/details/:messageId",
    name: "iris-message-details" /* Caution: This acts as an identifier! */,
    meta: {
      menu: false,
    },
    component: () =>
      import(
        /* webpackChunkName: "iris-message-details" */ "../views/iris-message-details/iris-message-details.view.vue"
      ),
  },
  {
    path: "/about",
    name: "about" /* Caution: This acts as an identifier! */,
    meta: {
      menu: true,
      menuName: "Über IRIS",
    },
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/about/about.view.vue"),
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export const locationFromRoute = (route: Route): Location => {
  const { name, path, hash, query, params } = route;
  return {
    ...(name ? { name } : {}),
    path,
    hash,
    query,
    params,
  };
};

export const setInterceptRoute = (route: Route): void => {
  store.commit("userLogin/setInterceptedRoute", locationFromRoute(route));
};

router.beforeEach(async (to, from, next) => {
  if (to.query.normalizeLog) {
    const normalizeLogEnabled = to.query.normalizeLog;
    if (normalizeLogEnabled === "enabled") {
      store.commit("normalizeSettings/setLogEnabled", true);
    } else if (normalizeLogEnabled === "disabled") {
      store.commit("normalizeSettings/setLogEnabled", false);
    }
    delete to.query.normalizeLog;
    return next(locationFromRoute(to));
  }
  // @todo: remove indexTracking enabled / disabled query functionality once index cases are permanently activated again
  if (to.query.indexTracking) {
    const indexTrackingQuery = to.query.indexTracking;
    if (indexTrackingQuery === "enabled") {
      store.commit("indexTrackingSettings/setIndexTrackingEnabled", true);
    } else if (indexTrackingQuery === "disabled") {
      store.commit("indexTrackingSettings/setIndexTrackingEnabled", false);
    }
    // remove query String "indexTracking" & reload page
    delete to.query.indexTracking;
    return next(locationFromRoute(to));
  }
  // @todo - indexTracking: remove redirect once index cases are permanently activated again
  if (
    to.name === "index-new" ||
    to.name === "index-list" ||
    to.name === "index-details"
  ) {
    if (!store.state.indexTrackingSettings.indexTrackingEnabled) {
      return next("/");
    }
  }
  if (to.meta?.auth !== false && !store.getters["userLogin/isAuthenticated"]) {
    // this is triggered if a user is not logged in and tries to deep link into the application
    setInterceptRoute(to);
    return next("/user/login");
  }
  if (to.meta?.admin === true) {
    const user = await getAuthenticatedUser();
    if (user?.role !== UserRole.Admin) {
      return next("/");
    }
  }
  if (to.name === "user-login" && store.getters["userLogin/isAuthenticated"]) {
    return next("/");
  }
  next();
});

/**
 * Webpack splits files (assets, code, etc.) into hashed chunks to improve loading performance and to handle outdated / cached files.
 * Some of the hashed chunks are not loaded initially with the index.html file but only if they are used / required.
 * If new code (-> new chunks with new file-hashes) is deployed to the server the old files are deleted and the references to the old chunk file hashes are no longer valid.
 * The router views are imported as hashed chunks which means that the router wont be able to navigate to the view-chunk after a deployment.
 * There are many solutions for that problem (not deleting old files from the server, removing the hash from the output files, etc.)
 * To keep it simple - the following approach is used:
 * - if a router error is thrown, check if it is caused by a missing chunk
 * - reload the page which should update the file paths to the chunks
 * - to avoid infinite reload loops: check the last time the page was reloaded due to a missing chunk.
 * - trigger the reload only after 30 minutes have passed since the last reload (or if it is the first reload).
 */
router.onError((error) => {
  const pattern = /Loading chunk.*failed/g;
  if (pattern.test(error.message)) {
    const reloadedAt = store.state.chunkLoader.reloadedAt;
    if (!reloadedAt || Math.abs(dayjs().diff(reloadedAt, "minutes")) > 30) {
      store.commit("chunkLoader/setReloadedAt", dayjs().valueOf());
      window.location.reload();
      return;
    }
  }
  throw error;
});

export default router;
