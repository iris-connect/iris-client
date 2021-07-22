const initAppContext = (): void => {
  try {
    if (window.irisAppContext) return;
    const appElement = document.querySelector("#app");
    if (appElement) {
      const ctxString = appElement.getAttribute("data-iris-app-context");
      if (ctxString) {
        window.irisAppContext = JSON.parse(ctxString);
      }
    }
  } catch (e) {
    console.error(e);
  }
};

export default initAppContext;
