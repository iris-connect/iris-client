<template>
  <event-tracking-details-component
    :table-rows="guests"
    :event-data="eventData"
    :form-data="formData"
    :loading="loading"
    :errors="errorMessages"
    @field-edit="handleEditableField"
    @status-update="updateRequestStatus"
    @data-export="handleExport"
  />
</template>
<style></style>
<script lang="ts">
import {
  Address,
  DataRequestDetails,
  DataRequestStatus,
  DataRequestStatusUpdateByUser,
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
};

export type ExportData = {
  headers: Array<{
    text: string;
    value: string;
  }>;
  rows: string[][];
};

function getFormattedDate(date?: string | Date): string {
  if (date && dayjs(date).isValid()) {
    return dayjs(date).format("LLL");
  }
  return "-";
}

function getFormattedAddress(address?: Address | null): string {
  if (address) {
    return `${address.street} ${address.houseNumber}, ${address.zipCode} ${address.city}`;
  }
  return "-";
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
      lastChange: getFormattedDate(dataRequest?.lastUpdatedAt),
      location: dataRequest?.locationInformation,
      additionalInformation:
        dataRequest?.submissionData?.additionalInformation || "-",
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
        lastName: guest.lastName || "-",
        firstName: guest.firstName || "-",
        checkInTime,
        checkOutTime,
        maxDuration: maxDuration,
        comment: guest.attendanceInformation.additionalInformation || "-", // TODO: Line Breaks
        sex: guest.sex ? Genders.getName(guest.sex) : "-",
        email: guest.email || "-",
        phone: guest.phone || "-",
        mobilePhone: guest.mobilePhone || "-",
        address: getFormattedAddress(guest.address),
      };
    });
  }

  handleExport(payload: ExportData): void {
    dataExport.exportCsv(
      payload.headers,
      payload.rows,
      [this.eventTrackingDetails?.externalRequestId, Date.now()].join("_")
    );
  }
}
</script>
