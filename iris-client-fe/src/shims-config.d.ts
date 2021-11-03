declare global {
  interface Window {
    irisAppContext?: IRISAppContext;
    Cypress?: unknown;
    irisApp?: Vue;
  }
}

export type IRISAppContext = {
  apiBaseURL: string;
  csvExportStandardAtomicAddress: string;
  localContactPerson: {
    name?: string;
    phone?: string;
    mail?: string;
  };
};
