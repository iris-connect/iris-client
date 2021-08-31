declare global {
  interface Window {
    irisAppContext?: IRISAppContext;
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
