<template>
  <div>
    <alert-component v-if="alert">
      <template v-slot:message>
        Die Kontaktdaten zu diesem Ereignis wurden angefragt.
      </template>
    </alert-component>
    <event-tracking-details-component
      :table-rows="guests"
      :event-data="eventData"
      :form-data="formData"
      :loading="loading"
      :errors="errorMessages"
      @field-edit="handleEditableField"
      @status-update="updateRequestStatus"
      @data-export="handleStandardCsvExport"
      @handle-standard-csv-export="handleStandardCsvExport"
      @handle-alternative-standard-csv-export="
        handleAlternativeStandardCsvExport
      "
      @handle-sormas-csv-event-participants-export="
        handleSormasCsvEventParticipantsExport
      "
      @handle-sormas-csv-contact-person-export="
        handleSormasCsvContactPersonExport
      "
    />
  </div>
</template>
<style></style>
<script lang="ts">
import {
  Address,
  DataRequestDetails,
  DataRequestStatus,
  DataRequestStatusUpdateByUser,
  Guest,
  LocationInformation,
} from "@/api";
import router from "@/router";
import store from "@/store";
import { Component, Vue } from "vue-property-decorator";
import dataExport from "@/utils/data-export";
import EventTrackingDetailsLocationInfo from "@/views/event-tracking-details/components/event-tracking-details-location-info.vue";
import dayjs from "@/utils/date";
import Genders from "@/constants/Genders";
import ErrorMessageAlert from "@/components/error-message-alert.vue";
import { ErrorMessage } from "@/utils/axios";
import EditableField from "@/components/form/editable-field.vue";
import StatusChangeConfirmDialog from "@/views/event-tracking-details/components/confirm-dialog.vue";
import EventTrackingStatusChange from "@/views/event-tracking-details/components/event-tracking-status-change.vue";
import EventTrackingDetailsComponent from "@/views/event-tracking-details/components/event-tracking-details.component.vue";
import AlertComponent from "@/components/alerts/alert.component.vue";

export type FormData = {
  name?: string;
  externalRequestId?: string;
  comment?: string;
};

export type EventData = {
  startTime: string;
  endTime: string;
  generatedTime: string;
  status?: DataRequestStatus;
  lastChange: string;
  location?: LocationInformation;
  additionalInformation: string;
};

export type TableRow = {
  lastName: string;
  firstName: string;
  checkInTime: string;
  checkOutTime: string;
  maxDuration: string;
  comment: string;
  sex: string;
  email: string;
  phone: string;
  mobilePhone: string;
  address: string;
  raw: Guest;
};

export type EventParticipantData = {
  involvementDescription: string;
  firstName: string;
  lastName: string;
  sex: string;
  phone: string;
  email: string;
  postalCode: string;
  city: string;
  street: string;
  houseNumber: string;
};

export type ContactCaseData = {
  description: string;

  firstName: string;
  lastName: string;
  sex: string;
  phone: string;
  email: string;
  postalCode: string;
  city: string;
  street: string;
  houseNumber: string;
};

function getFormattedDate(date?: string | Date): string {
  if (date && dayjs(date).isValid()) {
    return dayjs(date).format("LLL");
  }
  return "-";
}

function getFormattedAddress(address?: Address | null): string {
  if (address) {
    return `${sanitiseFieldForDisplay(
      address.street
    )} ${sanitiseFieldForDisplay(
      address.houseNumber
    )} \n${sanitiseFieldForDisplay(address.zipCode)} ${sanitiseFieldForDisplay(
      address.city
    )}`;
  }
  return "-";
}
function sanitiseFieldForDisplay(text = ""): string {
  const RE = RegExp(/\s+/, "g");
  return text.replace(RE, " ");
}

@Component({
  components: {
    EventTrackingDetailsComponent,
    EventTrackingStatusChange,
    StatusChangeConfirmDialog,
    EditableField,
    ErrorMessageAlert,
    EventTrackingDetailsLocationInfo,
    EventTrackingDetailsView: EventTrackingDetailsView,
    AlertComponent,
  },
  async beforeRouteEnter(_from, _to, next) {
    next();
    await store.dispatch(
      "eventTrackingDetails/fetchEventTrackingDetails",
      router.currentRoute.params.id
    );
  },
  beforeRouteLeave(to, from, next) {
    store.commit("eventTrackingDetails/reset");
    next();
  },
})
export default class EventTrackingDetailsView extends Vue {
  alert = false;

  get eventTrackingDetails(): DataRequestDetails | null {
    return store.state.eventTrackingDetails.eventTrackingDetails;
  }

  // Editable values => formData. Readonly values => eventData.
  get formData(): FormData {
    return {
      externalRequestId: this.eventTrackingDetails?.externalRequestId || "",
      name: this.eventTrackingDetails?.name || "",
      comment: this.eventTrackingDetails?.comment || "",
    };
  }

  get eventData(): EventData {
    const dataRequest = this.eventTrackingDetails;

    return {
      startTime: getFormattedDate(dataRequest?.start),
      endTime: getFormattedDate(dataRequest?.end),
      generatedTime: getFormattedDate(dataRequest?.requestedAt),
      status: dataRequest?.status,
      lastChange: getFormattedDate(dataRequest?.lastModifiedAt),
      location: dataRequest?.locationInformation,
      additionalInformation: dataRequest?.requestDetails || "-",
    };
  }

  get loading(): boolean {
    return store.state.eventTrackingDetails.eventTrackingDetailsLoading;
  }

  get errorMessages(): ErrorMessage[] {
    return [
      store.state.eventTrackingDetails.eventTrackingDetailsLoadingError,
      store.state.eventTrackingDetails.dataRequestPatchError,
    ];
  }

  created(): void {
    if (this.$route.query.is_created == "true") {
      this.openAlert();
    }

    let query = Object.assign({}, this.$route.query);
    if (query.is_created) {
      delete query.is_created;
      this.$router.replace({ query });
    }
  }

  openAlert(): void {
    this.alert = true;
    setTimeout(() => {
      this.alert = false;
    }, 2000);
  }

  updateRequestStatus(status: DataRequestStatusUpdateByUser): void {
    store.dispatch("eventTrackingDetails/patchDataRequest", {
      id: router.currentRoute.params.id,
      data: {
        status,
      },
    });
  }

  handleEditableField(
    data: Record<string, unknown>,
    resolve: () => void,
    reject: (error: string | undefined) => void
  ): void {
    store
      .dispatch("eventTrackingDetails/patchDataRequest", {
        id: router.currentRoute.params.id,
        data,
      })
      .then(resolve)
      .catch((error) => {
        // reset vuex error as it is handled locally
        store.commit("eventTrackingDetails/setDataRequestPatchError", null);
        reject(error);
      });
  }

  get guests(): TableRow[] {
    // TODO attendanceInformation is optional
    const guests =
      store.state.eventTrackingDetails.eventTrackingDetails?.submissionData
        ?.guests || [];
    const eventDataRequest =
      store.state.eventTrackingDetails.eventTrackingDetails;
    return guests.map((guest, index) => {
      const attendTo = guest.attendanceInformation?.attendTo;
      const checkOut = attendTo ? new Date(attendTo) : null;

      const attendFrom = guest.attendanceInformation?.attendFrom;
      const checkIn = attendFrom ? new Date(attendFrom) : null;

      const startTime = eventDataRequest?.start
        ? new Date(eventDataRequest.start)
        : checkIn;
      const endTime = eventDataRequest?.end
        ? new Date(eventDataRequest.end)
        : checkOut;

      let checkInTime = "-";
      if (checkIn && startTime) {
        checkInTime = getFormattedDate(checkIn);
      }

      let checkOutTime = "-";
      if (checkOut) {
        checkOutTime = getFormattedDate(checkOut);
      }

      // min(iE, cE)-max(iS, cS)
      // |--------------| TIME INFECTED PERSON _i Started and Ended_ AT LOCATION
      //       |----------------| TIME CONTACT PERSON _c Started and Ended_ AT LOCATION
      //       |--------| RELEVANT

      let maxDuration = "keine";
      if (checkOut && checkIn && startTime && endTime) {
        let durationSeconds =
          (Math.min(checkOut.valueOf(), endTime.valueOf()) -
            Math.max(checkIn.valueOf(), startTime.valueOf())) /
          1000;
        let hours = Math.floor(durationSeconds / 3600);
        let minutes = Math.round((durationSeconds - hours * 3600) / 60);
        if (durationSeconds > 0) {
          if (hours > 0)
            maxDuration = hours.toString() + "h " + minutes.toString() + "min";
          else if (minutes > 0) maxDuration = minutes.toString() + "min";
        }
      }
      return {
        id: index,
        lastName: sanitiseFieldForDisplay(guest.lastName) || "-",
        firstName: sanitiseFieldForDisplay(guest.firstName) || "-",
        checkInTime,
        checkOutTime,
        maxDuration: maxDuration,
        comment:
          sanitiseFieldForDisplay(
            guest.attendanceInformation.additionalInformation
          ) || "-",
        sex: guest.sex ? Genders.getName(guest.sex) : "-",
        email: sanitiseFieldForDisplay(guest.email) || "-",
        phone: sanitiseFieldForDisplay(guest.phone) || "-",
        mobilePhone: sanitiseFieldForDisplay(guest.mobilePhone) || "-",
        address: getFormattedAddress(guest.address),
        raw: guest,
      };
    });
  }

  handleStandardCsvExport(payload: Array<Array<string>>): void {
    dataExport.exportStandardCsvForEventTracking(
      payload,
      [
        this.eventTrackingDetails?.externalRequestId || "Export",
        Date.now(),
      ].join("_")
    );
  }

  handleAlternativeStandardCsvExport(payload: Array<Array<string>>): void {
    dataExport.exportAlternativeStandardCsvForEventTracking(
      payload,
      [
        this.eventTrackingDetails?.externalRequestId || "Export",
        Date.now(),
      ].join("_")
    );
  }

  handleSormasCsvEventParticipantsExport(payload: TableRow[]): void {
    dataExport.exportSormasEventParticipantsCsv(
      this.convertTableRowToEventParticipationData(payload),
      [
        this.eventTrackingDetails?.externalRequestId || "Export",
        Date.now(),
      ].join("_")
    );
  }

  handleSormasCsvContactPersonExport(payload: TableRow[]): void {
    dataExport.exportSormasContactPersonCsv(
      this.convertTableRowToContactCaseData(
        payload,
        this.eventTrackingDetails?.externalRequestId || "-"
      ),
      [
        this.eventTrackingDetails?.externalRequestId || "Export",
        Date.now(),
      ].join("_")
    );
  }

  convertTableRowToContactCaseData(
    tableRows: TableRow[],
    externalId: string
  ): ContactCaseData[] {
    const data: ContactCaseData[] = [];

    tableRows.forEach((element) => {
      const description =
        "Aus Ereignis " +
        externalId +
        ": " +
        element.comment +
        " // " +
        element.checkInTime +
        " Uhr bis " +
        element.checkOutTime +
        " Uhr (Maximale Kontaktdauer " +
        element.maxDuration +
        ")";

      const dataInstance: ContactCaseData = {
        description: description,
        firstName: element.firstName,
        lastName: element.lastName,
        sex: element.raw.sex || "",
        phone: element.phone,
        email: element.email,
        postalCode: element.raw.address?.zipCode || "",
        city: element.raw.address?.city || "",
        street: element.raw.address?.street || "",
        houseNumber: element.raw.address?.houseNumber || "",
      };
      data.push(dataInstance);
    });
    return data;
  }

  convertTableRowToEventParticipationData(
    tableRows: TableRow[]
  ): EventParticipantData[] {
    const data: EventParticipantData[] = [];

    const headerInstance: EventParticipantData = {
      involvementDescription: "involvementDescription",
      firstName: "person.firstName",
      lastName: "person.lastName",
      sex: "person.sex",
      phone: "person.phone",
      email: "person.emailAddress",
      postalCode: "person.address.postalCode",
      city: "person.address.city",
      street: "person.address.street",
      houseNumber: "person.address.houseNumber",
    };
    data.push(headerInstance);

    tableRows.forEach((element) => {
      const involvementDescription =
        element.comment +
        " // " +
        element.checkInTime +
        " Uhr bis " +
        element.checkOutTime +
        " Uhr (Maximale Kontaktdauer " +
        element.maxDuration +
        ")";

      const dataInstance: EventParticipantData = {
        involvementDescription: involvementDescription,
        firstName: element.firstName,
        lastName: element.lastName,
        sex: element.raw.sex || "",
        phone: element.phone,
        email: element.email,
        postalCode: element.raw.address?.zipCode || "",
        city: element.raw.address?.city || "",
        street: element.raw.address?.street || "",
        houseNumber: element.raw.address?.houseNumber || "",
      };
      data.push(dataInstance);
    });
    return data;
  }
}
</script>
