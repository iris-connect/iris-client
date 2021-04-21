import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import Home from "../views/home/Home.vue";

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
    path: "/cases/new",
    name: "index-new",
    meta: {
      menu: false,
    },
    component: () =>
      import(
        /* webpackChunkName: "about" */ "../views/index-tracking-form/index-tracking-form.view.vue"
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
  {
    path: "/cases/details/:caseId",
    name: "index-details" /* Caution: This acts as an identifier! */,
    meta: {
      menu: false,
    },
    component: () =>
      import(
        /* webpackChunkName: "about" */ "../views/index-tracking-details/index-tracking-details.view.vue"
      ),
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
