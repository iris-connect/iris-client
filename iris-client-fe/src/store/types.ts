import { EventTrackingListState } from "@/views/event-tracking-list/event-tracking-list.store";
import { EventTrackingFormState } from "@/views/event-tracking-form/event-tracking-form.store";
import { EventTrackingDetailsState } from "@/views/event-tracking-details/event-tracking-details.store";
import { IndexTrackingListState } from "@/views/index-tracking-list/index-tracking-list.store";
import { IndexTrackingFormState } from "@/views/index-tracking-form/index-tracking-form.store";
import { IndexTrackingDetailsState } from "@/views/index-tracking-details/index-tracking-details.store";
import { HomeState } from "@/views/home/home.store";
import { UserLoginState } from "@/views/user-login/user-login.store";
import { AdminUserListState } from "@/views/admin-user-list/admin-user-list.store";
import { AdminUserCreateState } from "@/views/admin-user-create/admin-user-create.store";
import { AdminUserEditState } from "@/views/admin-user-edit/admin-user-edit.store";
// @todo - indexTracking: optional remove next line once index cases are permanently activated again
import { IndexTrackingSettingsState } from "@/views/app-settings/index-tracking-settings.store";
import { NormalizeSettingsState } from "@/views/app-settings/normalize-settings.store";
import { ChunkLoaderState } from "@/views/app-settings/chunk-loader.store";
import { CheckinAppStatusListState } from "@/views/checkin-app-status-list/checkin-app-status-list.store";
import { IrisMessageListState } from "@/views/iris-message-list/iris-message-list.store";

export type RootState = {
  home: HomeState;
  eventTrackingForm: EventTrackingFormState;
  eventTrackingList: EventTrackingListState;
  eventTrackingDetails: EventTrackingDetailsState;
  userLogin: UserLoginState;
  indexTrackingForm: IndexTrackingFormState;
  indexTrackingList: IndexTrackingListState;
  indexTrackingDetails: IndexTrackingDetailsState;
  adminUserList: AdminUserListState;
  adminUserCreate: AdminUserCreateState;
  adminUserEdit: AdminUserEditState;
  // @todo - indexTracking: optional remove next line once index cases are permanently activated again
  indexTrackingSettings: IndexTrackingSettingsState;
  normalizeSettings: NormalizeSettingsState;
  chunkLoader: ChunkLoaderState;
  checkinAppStatusList: CheckinAppStatusListState;
  irisMessageList: IrisMessageListState;
};
