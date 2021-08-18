import { User } from "@/api";
import store from "@/store";
import _noop from "lodash/noop";

/**
 * There are some routes / route guards that require the currently authenticated user to work.
 * E.g. Before entering a route like "admin-user-*" we need to know if the user has admin rights or not.
 * As no user credentials are persisted locally we have to fetch them on page load and / or as soon as there is an access token.
 * The corresponding fetch request is triggered by a watcher function (App.vue).
 * We have to delay the navigation guard functionality while waiting for the user data to be ready.
 */
export const getAuthenticatedUser = async (
  cancelTimeout = 3000
): Promise<User | null> => {
  const authenticated = store.getters["userLogin/isAuthenticated"];
  if (!authenticated) return null; // nothing more to do, if there is no authenticated user
  let user = store.state.userLogin.user; // get stored user (always "null" right after page load)
  // If there is no stored user, yet...
  if (!user) {
    let unsubscribe = _noop; // fallback for vuex unsubscribe function
    // ... wait until the user is fetched and saved to the vuex store.
    // The "userLogin/setUser" mutation is triggered after each "userProfileGet" request (even if it fails).
    const pStore = new Promise<User | null>((resolve) => {
      unsubscribe = store.subscribe((mutation) => {
        if (mutation.type === "userLogin/setUser") {
          resolve(mutation.payload);
        }
      });
    });
    // If "userLogin/setUser" isn't invoked within {{ timout }} milliseconds, do not wait any longer.
    const pCancel = new Promise<null>((resolve) => {
      setTimeout(() => resolve(null), cancelTimeout);
    });
    user = await Promise.race([pStore, pCancel]);
    // Independently of which promise makes the race: unsubscribe.
    unsubscribe();
  }
  return user;
};
