import { EventTrackingListState } from "@/views/event-tracking-list/event-tracking-list.store";
import { EventTrackingFormState } from "../views/event-tracking-form/event-tracking-form.store";

export type RootState = {
  eventTrackingForm: EventTrackingFormState;
  eventTrackingList: EventTrackingListState;
};
