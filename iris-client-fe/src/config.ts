// https://cli.vuejs.org/guide/mode-and-env.html
// During build, process.env.VUE_APP_XYZ will be replaced by the corresponding value.
// In the case of VUE_APP_VERSION_ID=v1.2.3, it will be replaced by "v1.2.3".

import initAppContext from "@/utils/appContext";

// in case: config.ts file is loaded before main.ts
initAppContext();

const config = {
  apiBaseURL: window.irisAppContext?.apiBaseURL,
  localContactPerson: window.irisAppContext?.localContactPerson || {},
  passwordMinLength: 8,
  appVersionId: process.env.VUE_APP_VERSION_ID as string | undefined,
  appBuildId: process.env.VUE_APP_BUILD_ID as string | undefined,
};

export default config;
