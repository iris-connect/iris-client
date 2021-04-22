import Vue from "vue";
import VueRouter, { Location, RouteConfig } from "vue-router";
import Home from "../views/home/Home.vue";
import store from "@/store";

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
    },
    component: () =>
      import(
        /* webpackChunkName: "admin-user-create" */ "../views/admin-user-create/admin-user-create.view.vue"
      ),
  },
  {
    path: "/admin/user/edit/:id",
    name: "admin-user-edit",
    meta: {
      menu: false,
    },
    component: () =>
      import(
        /* webpackChunkName: "admin-user-edit" */ "../views/admin-user-edit/admin-user-edit.view.vue"
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
        /* webpackChunkName: "about" */ "../views/event-tracking-form/event-tracking-form.view.vue"
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
        /* webpackChunkName: "about" */ "../views/event-tracking-list/event-tracking-list.view.vue"
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
        /* webpackChunkName: "about" */ "../views/index-tracking-list/index-tracking-list.view.vue"
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
        /* webpackChunkName: "about" */ "../views/event-tracking-details/event-tracking-details.view.vue"
      ),
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.meta.auth !== false && !store.getters["userLogin/isAuthenticated"]) {
    const { name, path, hash, query, params } = to;
    const toLocation: Location = {
      ...(name ? { name } : {}),
      path,
      hash,
      query,
      params,
    };
    store.commit("userLogin/setInterceptedRoute", toLocation);
    return next("/user/login");
  }
  if (to.name === "user-login" && store.getters["userLogin/isAuthenticated"]) {
    return next("/");
  }
  next();
});

export default router;
