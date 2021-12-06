type LegacyNavigator = Navigator & {
  msSaveBlob?: (blob: Blob, defaultName?: string) => boolean;
};

const download = (data: BufferSource | Blob | string, fileName: string) => {
  const legacyNavigator = navigator as LegacyNavigator;
  if (legacyNavigator.msSaveBlob) {
    // IE10
    legacyNavigator.msSaveBlob(
      new Blob([data], {
        type: "application/octet-stream",
      }),
      fileName
    );
  } else {
    const downloadUrl = window.URL.createObjectURL(new Blob([data]));
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
  download,
};

export default fileDownload;
