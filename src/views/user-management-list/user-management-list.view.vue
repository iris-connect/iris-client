<template>
  <div>
    <v-row>
      <v-col cols="12">
        <div class="mb-6">
          <v-btn class="float-right" color="primary" :to="{ name: 'user-new' }"
            >Neuen Nutzer anlegen
          </v-btn>
        </div>
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-card>
          <v-card-title>Benutzerverwaltung</v-card-title>
          <v-card-text>
            <v-row>
              <v-col>
                <v-text-field
                  v-model="tableData.search"
                  append-icon="mdi-magnify"
                  label="Search"
                  single-line
                  hide-details
                ></v-text-field>
                <v-data-table
                  :loading="listLoading"
                  :headers="tableData.headers"
                  :items="userList"
                  :items-per-page="5"
                  class="elevation-1 mt-5"
                  :search="tableData.search"
                >
                </v-data-table>
              </v-col>
            </v-row>
            <v-row>
              <v-col v-if="listLoadingError">
                <v-alert text type="error">{{ listLoadingError }}</v-alert>
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

type TableRow = {
  lastName?: string;
  firstName?: string;
  userName?: string;
  role?: string;
};

const UserRoleName = new Map<UserRole | undefined, string>([
  [UserRole.Admin, "Administrator"],
  [UserRole.User, "Nutzer"],
]);

@Component({
  async beforeRouteEnter(_from, _to, next) {
    next();
    await store.dispatch("userManagementList/fetchUserList");
  },
  beforeRouteLeave(to, from, next) {
    store.commit("userManagementList/reset");
    next();
  },
})
export default class UserManagementListView extends Vue {
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
        text: "Benutzername",
        value: "userName",
      },
      {
        text: "Rolle",
        value: "role",
      },
    ],
  };

  get listLoading(): boolean {
    return store.state.userManagementList.userListLoading;
  }

  get listLoadingError(): ErrorMessage {
    return store.state.userManagementList.userListLoadingError;
  }

  get userList(): TableRow[] {
    const users: Array<User> =
      store.state.userManagementList.userList?.users || [];
    return users.map((user) => {
      const { lastName, firstName, userName, role } = user;
      return {
        lastName,
        firstName,
        userName,
        role: UserRoleName.get(role),
      };
    });
  }
}
</script>
