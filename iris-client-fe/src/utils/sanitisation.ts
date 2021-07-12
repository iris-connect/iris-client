// Utility for sanitisation, input validation and type checks

export const sanitiseAndCheckRecordWithNoRestrictionToInput = function (
  input: Record<string, unknown>
): Record<string, unknown> {
  console.log("sanitiseAndCheckUIINputWithNoRestrictionToInput");
  console.log(input);
  if (input["name"] != null && typeof input["name"] == "string") {
    input["name"] = sanitiseField(String(input["name"]));
  }
  if (input["comment"] != null && typeof input["comment"] == "string") {
    input["comment"] = sanitiseField(String(input["comment"]));
  }
  if (
    input["externalRequestId"] != null &&
    typeof input["externalRequestId"] == "string"
  ) {
    input["externalRequestId"] = sanitiseField(
      String(input["externalRequestId"])
    );
  }
  return input;
};

export const sanitiseAndCheckUIInputWithNoRestrictionToInput = function (
  input: string
): string {
  return sanitiseField(input);
};

export const sanitiseField = function (
  field: string | undefined,
  separator = ""
): string {
  const possibleSeperatorRE = /[,;]/g;
  const whitespaceRE = RegExp(/\s+/, "g");
  const whitelistRE = /([\p{L}\p{N}]@[\p{L}\p{N}])|[\p{L}\p{N}()[\]:./ -]/gu;
  const headWhitelistRE = /^([()[\]]*[\p{L}\p{N}])+/u;

  if (!field) {
    return field || "-";
  }

  field = field.replace(possibleSeperatorRE, "/");
  field = field.replace(whitespaceRE, " ");
  const matches = field.match(whitelistRE);
  field = matches?.join("") || "";

  /**
   * json2csv uses a seperator to split table columns. We currently are using ; and , as those.
   * If such a sign would appear in the content than it would result in a split.
   * To prevent this we change the seperators symbol with another symbol not used as such.
   */
  if (separator != "") {
    let separator_replacement = "/";
    if (separator === "/") separator_replacement = ".";
    while (field.includes(separator))
      field = field.replace(separator, separator_replacement);
  }
  while (!headWhitelistRE.test(field) && field.length > 0) {
    field = field.substring(1);
  }

  if (field.length == 0) {
    return "-";
  }
  return field;
};
