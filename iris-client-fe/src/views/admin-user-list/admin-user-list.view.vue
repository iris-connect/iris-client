<template>
  <div>
    <v-row>
      <v-col cols="12">
        <div class="mb-6">
          <v-btn
            class="float-right"
            color="primary"
            :to="{ name: 'admin-user-create' }"
            data-test="view.link.create"
            >Neues Konto anlegen
          </v-btn>
        </div>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-card>
          <v-card-title>Kontoverwaltung</v-card-title>
          <v-card-text>
            <v-row>
              <v-col>
                <v-text-field
                  v-model="tableData.search"
                  append-icon="mdi-magnify"
                  label="Search"
                  single-line
                  hide-details
                  data-test="search"
                ></v-text-field>
                <iris-data-table
                  :loading="isBusy"
                  :headers="tableHeaders"
                  :items="userList"
                  :items-per-page="5"
                  class="elevation-1 mt-5"
                  :search="tableData.search"
                  data-test="view.data-table"
                >
                  <template #item.mfa="{ item }">
                    <v-icon v-if="item.useMfa === false">mdi-cancel</v-icon>
                    <template v-else>
                      <v-icon
                        v-if="item.mfaSecretEnrolled === false"
                        color="error"
                      >
                        mdi-qrcode-scan
                      </v-icon>
                      <v-icon v-else color="success">mdi-check-circle</v-icon>
                    </template>
                  </template>
                  <template #item.locked="{ item }">
                    <v-icon v-if="item.locked" color="error">mdi-lock</v-icon>
                    <v-icon v-else color="success">mdi-check-circle</v-icon>
                  </template>
                  <template #item.actions="{ item }">
                    <v-btn
                      icon
                      large
                      class="ml-3 text-decoration-none"
                      :to="{ name: 'admin-user-edit', params: { id: item.id } }"
                      :disabled="isBusy"
                      data-test="edit"
                    >
                      <v-icon> mdi-pencil </v-icon>
                    </v-btn>
                    <user-delete-button
                      icon
                      large
                      color="error"
                      class="ml-3"
                      @click="deleteUser(item.id)"
                      :disabled="
                        isBusy ||
                        $store.getters['userLogin/isCurrentUser'](item.id)
                      "
                      :user-name="item.userName"
                      data-test="delete"
                    >
                      <v-icon> mdi-delete </v-icon>
                    </user-delete-button>
                  </template>
                </iris-data-table>
              </v-col>
            </v-row>
            <v-row v-if="hasError">
              <v-col>
                <v-alert v-if="listLoadingError" text type="error">
                  {{ listLoadingError }}
                </v-alert>
                <v-alert v-if="userDeleteError" text type="error">
                  {{ userDeleteError }}
                </v-alert>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import store from "@/store";
import { ErrorMessage } from "@/utils/axios";
import { MfaOption, User, UserRole } from "@/api";
import UserDeleteButton from "@/views/admin-user-list/components/user-delete-button.vue";
import IrisDataTable from "@/components/iris-data-table.vue";
import { mfaApi } from "@/modules/mfa/services/api";

// @todo: check if id is really optional! Handle, edit & delete actions + vue :key accordingly
type TableRow = {
  id?: string;
  lastName?: string;
  firstName?: string;
  userName: string;
  role: string;
  useMfa: boolean;
  mfaSecretEnrolled: boolean;
  locked: boolean;
};

const UserRoleName = new Map<UserRole, string>([
  [UserRole.Admin, "Administration"],
  [UserRole.User, "Nutzung"],
]);

@Component({
  components: { IrisDataTable, UserDeleteButton },
  async beforeRouteEnter(to, from, next) {
    next();
    await store.dispatch("adminUserList/fetchUserList");
  },
  beforeRouteLeave(to, from, next) {
    store.commit("adminUserList/reset");
    next();
  },
})
export default class AdminUserListView extends Vue {
  fetchMfaConfig = mfaApi.fetchMfaConfig();
  mounted() {
    this.fetchMfaConfig.execute();
  }

  get mfaOption(): MfaOption | undefined {
    return this.fetchMfaConfig.state.result?.mfaOption;
  }

  get tableHeaders(): Array<unknown> {
    return [
      {
        text: "Nachname",
        value: "lastName",
        align: "start",
      },
      {
        text: "Vorname",
        value: "firstName",
      },
      {
        text: "Anmeldename",
        value: "userName",
      },
      {
        text: "Rolle",
        value: "role",
      },
      this.mfaOption !== undefined && this.mfaOption !== MfaOption.DISABLED
        ? {
            text: "2FA",
            value: "mfa",
            align: "end",
          }
        : null,
      {
        text: "Status",
        value: "locked",
        align: "end",
      },
      {
        text: "",
        value: "actions",
        sortable: false,
        align: "end",
        cellClass: "text-no-wrap",
      },
    ].filter((v) => v);
  }

  tableData = {
    search: "",
    expanded: [],
    select: [],
  };

  get listLoading(): boolean {
    return store.state.adminUserList.userListLoading;
  }

  get listLoadingError(): ErrorMessage {
    return store.state.adminUserList.userListLoadingError;
  }

  get userDeleteOngoing(): boolean {
    return store.state.adminUserList.userDeleteOngoing;
  }

  get userDeleteError(): ErrorMessage {
    return store.state.adminUserList.userDeleteError;
  }

  get isBusy(): boolean {
    return this.listLoading || this.userDeleteOngoing;
  }

  get hasError(): boolean {
    return !!this.listLoadingError || !!this.userDeleteError;
  }

  get userList(): TableRow[] {
    const users: Array<User> = store.state.adminUserList.userList?.users || [];
    return users.map((user) => {
      const {
        id,
        lastName,
        firstName,
        userName,
        role,
        useMfa,
        mfaSecretEnrolled,
        locked,
      } = user;
      return {
        id,
        lastName,
        firstName,
        userName,
        role: UserRoleName.get(role) ?? "-",
        useMfa,
        mfaSecretEnrolled,
        locked,
      };
    });
  }

  async deleteUser(id: string): Promise<void> {
    await store.dispatch("adminUserList/deleteUser", id);
  }
}
</script>
