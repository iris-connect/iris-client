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
                  :headers="tableData.headers"
                  :items="userList"
                  :items-per-page="5"
                  class="elevation-1 mt-5"
                  :search="tableData.search"
                  data-test="view.data-table"
                >
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
                <v-alert v-if="listLoadingError" text type="error">{{
                  listLoadingError
                }}</v-alert>
                <v-alert v-if="userDeleteError" text type="error">{{
                  userDeleteError
                }}</v-alert>
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
import { User, UserRole } from "@/api";
import UserDeleteButton from "@/views/admin-user-list/components/user-delete-button.vue";
import IrisDataTable from "@/components/iris-data-table.vue";

// @todo: check if id is really optional! Handle, edit & delete actions + vue :key accordingly
type TableRow = {
  id?: string;
  lastName?: string;
  firstName?: string;
  userName: string;
  role: string;
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
  tableData = {
    search: "",
    expanded: [],
    select: [],
    headers: [
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
      {
        text: "",
        value: "actions",
        sortable: false,
        align: "end",
        cellClass: "text-no-wrap",
      },
    ],
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
      const { id, lastName, firstName, userName, role } = user;
      return {
        id,
        lastName,
        firstName,
        userName,
        role: UserRoleName.get(role) ?? "-",
      };
    });
  }

  async deleteUser(id: string): Promise<void> {
    await store.dispatch("adminUserList/deleteUser", id);
  }
}
</script>
