declare global {
  interface Window {
    irisAppContext?: IRISAppContext;
  }
}

export type IRISAppContext = {
  apiBaseURL: string;
};
