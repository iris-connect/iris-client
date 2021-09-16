import Vue from "vue";
import Vuetify from "vuetify/lib/framework";
import de from "vuetify/src/locale/de";

/**
 * Vuetify generates theme styles at run-time. The generated styles will be placed in a <style> tag with an id of vuetify-theme-stylesheet.
 * https://vuetifyjs.com/en/features/theme/#options
 * This causes issues with the style-src Content Security Policy rules which could be resolved by using a Nonce.
 * https://vuetifyjs.com/en/features/theme/#csp-nonce
 * Passing a per request differing cryptographically-strong Nonce in a secure way to a CSR application / vuetify is rather impossible, because we would have to replace a Nonce substitution value with the Nonce value using Nginx.
 * If an attacker finds out this substitution value they can use it to inject into their own script tag and Nginx would rewrite a valid Nonce into it for them.
 * If we use a more static nonce (like a git commit hash) the Nonce is easy to bypass by an attacker.
 * The easiest and most secure solution is to not use the run-time generated inline-style stylesheet at all by disabling the vuetify theme.
 * The theme styles can easily be replicated with SCSS und provided at compile-time.
 */
import "./../styles/theme.scss";

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
    disable: true, // This will prevent the creation of the Vuetify inline-style stylesheet.
    // The theme configuration is handled in a dedicated stylesheet ./../styles/theme.scss
    // themes: {
    //   light: {
    //     primary: "#46ff9f", // #E53935
    //   },
    // },
  },
});
