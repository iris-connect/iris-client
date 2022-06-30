import _isArrayBuffer from "lodash/isArrayBuffer";
import _isTypedArray from "lodash/isTypedArray";

type LegacyNavigator = Navigator & {
  msSaveBlob?: (blob: Blob, defaultName?: string) => boolean;
};

const validateData = (data: BlobPart): boolean => {
  if (typeof data === "string") return true;
  if (data instanceof Blob) return true;
  if (_isArrayBuffer(data)) return true;
  if (_isTypedArray(data)) return true;
  return data instanceof DataView;
};

const download = (data: BlobPart, fileName: string) => {
  if (!fileDownload.validateData(data)) {
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

const fileDownload = {
  validateData,
  download,
};

export default fileDownload;
