import Vue from "vue";
import Vuetify from "vuetify/lib/framework";
import de from "vuetify/src/locale/de";

Vue.use(Vuetify);

export default new Vuetify({
  icons: {
    iconfont: "mdiSvg",
  },
  lang: {
    locales: { de },
    current: "de",
  },
  theme: {
    themes: {
      light: {
        primary: "#46ff9f", // #E53935
      },
    },
  },
});
