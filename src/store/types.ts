import { EventTrackingListState } from "@/views/event-tracking-list/event-tracking-list.store";
import { EventTrackingFormState } from "../views/event-tracking-form/event-tracking-form.store";
import { EventTrackingDetailsState } from "@/views/event-tracking-details/event-tracking-details.store";
import { HomeState } from "@/views/home/home.store";
import { UserLoginState } from "@/views/user-login/user-login.store";
import { AdminUserListState } from "@/views/admin-user-list/admin-user-list.store";
import { AdminUserCreateState } from "@/views/admin-user-create/admin-user-create.store";
import { AdminUserEditState } from "@/views/admin-user-edit/admin-user-edit.store";

export type RootState = {
  home: HomeState;
  eventTrackingForm: EventTrackingFormState;
  eventTrackingList: EventTrackingListState;
  eventTrackingDetails: EventTrackingDetailsState;
  userLogin: UserLoginState;
  adminUserList: AdminUserListState;
  adminUserCreate: AdminUserCreateState;
  adminUserEdit: AdminUserEditState;
};
