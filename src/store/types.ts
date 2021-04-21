import { EventTrackingListState } from "@/views/event-tracking-list/event-tracking-list.store";
import { EventTrackingFormState } from "../views/event-tracking-form/event-tracking-form.store";
import { EventTrackingDetailsState } from "@/views/event-tracking-details/event-tracking-details.store";
import { HomeState } from "@/views/home/home.store";
import { UserLoginState } from "@/views/user-login/user-login.store";
import { UserManagementListState } from "@/views/user-management-list/user-management-list.store";

export type RootState = {
  home: HomeState;
  eventTrackingForm: EventTrackingFormState;
  eventTrackingList: EventTrackingListState;
  eventTrackingDetails: EventTrackingDetailsState;
  userLogin: UserLoginState;
  userManagementList: UserManagementListState;
};
