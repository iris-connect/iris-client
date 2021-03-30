import { EventTrackingFormState } from "../views/event-tracking-form/event-tracking-form.store";
import { EventTrackingDetailsState } from "@/views/event-tracking-details/event-tracking-details.store";

export type RootState = {
  eventTrackingForm: EventTrackingFormState;
  eventTrackingDetails: EventTrackingDetailsState;
};
