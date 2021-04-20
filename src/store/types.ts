import { EventTrackingListState } from "@/views/event-tracking-list/event-tracking-list.store";
import { EventTrackingFormState } from "../views/event-tracking-form/event-tracking-form.store";
import { EventTrackingDetailsState } from "@/views/event-tracking-details/event-tracking-details.store";
import { IndexTrackingListState } from "@/views/index-tracking-list/index-tracking-list.store";
import { IndexTrackingFormState } from "../views/index-tracking-form/index-tracking-form.store";
import { IndexTrackingDetailsState } from "@/views/index-tracking-details/index-tracking-details.store";
import { HomeState } from "@/views/home/home.store";

export type RootState = {
  home: HomeState;
  eventTrackingForm: EventTrackingFormState;
  eventTrackingList: EventTrackingListState;
  eventTrackingDetails: EventTrackingDetailsState;
  indexTrackingForm: IndexTrackingFormState;
  indexTrackingList: IndexTrackingListState;
  indexTrackingDetails: IndexTrackingDetailsState;
};
