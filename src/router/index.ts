import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import Home from "../views/home/Home.vue";

Vue.use(VueRouter);

// TODO create router config which exports these constants
const ROUTE_NAME_EVENT_TRACKING = "ereignisnachverfolgung";
export const ROUTE_NAME_EVENT_TRACKING_FORM = `/${ROUTE_NAME_EVENT_TRACKING}/starten`;
export const ROUTE_NAME_EVENT_TRACKING_LIST = `/${ROUTE_NAME_EVENT_TRACKING}/liste`;

export const ROUTE_NAME_HOME = "";

export const routes: Array<RouteConfig> = [
  {
    path: `/${ROUTE_NAME_HOME}`,
    name: "Dashboard",
    meta: {
      menu:true
    },
    component: Home,
  },
  {
    path: ROUTE_NAME_EVENT_TRACKING_FORM,
    name: `${ROUTE_NAME_EVENT_TRACKING}-start`,
    meta: {
      menu:false
    },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(
        /* webpackChunkName: "about" */ "../views/event-tracking-form/event-tracking-form.view.vue"
      ),
  },
  {
    path: ROUTE_NAME_EVENT_TRACKING_LIST,
    name: `${ROUTE_NAME_EVENT_TRACKING}`,
    meta: {
      menu:true
    },
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(
        /* webpackChunkName: "about" */ "../views/event-tracking-list/event-tracking-list.view.vue"
      ),
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
