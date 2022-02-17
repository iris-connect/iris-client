import _isArrayBuffer from "lodash/isArrayBuffer";
import _isTypedArray from "lodash/isTypedArray";

type LegacyNavigator = Navigator & {
  msSaveBlob?: (blob: Blob, defaultName?: string) => boolean;
};

const fileToBase64 = (file: File): Promise<string> =>
  new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => {
      if (typeof reader.result === "string") {
        const dataUrl = reader.result;
        resolve(dataUrl.split(",")[1] || "");
      } else {
        reject("invalid result");
      }
    };
    reader.onerror = (error) => reject(error);
  });

const base64ToFile = (base64: string, fileName: string) => {
  const bStr = atob(base64);
  let n = bStr.length;
  const u8arr = new Uint8Array(n);
  while (n--) {
    u8arr[n] = bStr.charCodeAt(n);
  }
  return new File([u8arr], fileName);
};

const validateData = (data: BlobPart): boolean => {
  if (typeof data === "string") return true;
  if (data instanceof Blob) return true;
  if (_isArrayBuffer(data)) return true;
  if (_isTypedArray(data)) return true;
  return data instanceof DataView;
};

const download = (data: BlobPart, fileName: string) => {
  if (!fileUtil.validateData(data)) {
    throw new Error("invalid file data");
  }
  const legacyNavigator = navigator as LegacyNavigator;
  if (legacyNavigator.msSaveBlob) {
    // IE10
    const blob =
      data instanceof Blob
        ? data
        : new Blob([data], { type: "application/octet-stream" });
    legacyNavigator.msSaveBlob(blob, fileName);
  } else {
    const blob = data instanceof Blob ? data : new Blob([data]);
    const downloadUrl = window.URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = downloadUrl;
    link.setAttribute("download", fileName);
    link.target = "_blank";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
};

const fileUtil = {
  fileToBase64,
  base64ToFile,
  validateData,
  download,
};

export default fileUtil;
