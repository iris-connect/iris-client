<template>
  <info-grid data-test="facility.info" :content="gridContent" />
</template>

<script lang="ts">
import { Component, Vue } from "vue-property-decorator";
import { VRFacility } from "@/api";
import { PropType } from "vue";
import InfoGrid from "@/components/info-grid.vue";

const VaccinationReportFacilityInfoProps = Vue.extend({
  inheritAttrs: false,
  props: {
    facility: {
      type: Object as PropType<VRFacility | null>,
      default: null,
    },
  },
});

@Component({
  components: {
    InfoGrid,
  },
})
export default class VaccinationReportFacilityInfo extends VaccinationReportFacilityInfoProps {
  get contact(): string[] {
    const contact = this.facility?.contactPerson;
    return [
      [contact?.firstName ?? "", contact?.lastName ?? ""].join(" ").trim(),
      contact?.eMail ?? "",
      contact?.phone ?? "",
    ].filter((v) => !!v);
  }
  get address(): string[] {
    const address = this.facility?.address;
    return [
      [address?.street ?? "", address?.houseNumber ?? ""].join(" ").trim(),
      [address?.zipCode ?? "", address?.city ?? ""].join(" ").trim(),
    ].filter((v) => v);
  }
  get gridContent() {
    return [
      [["Einrichtung", this.facility?.name ?? ""]],
      [
        ["Adresse", this.address],
        ["Ansprechpartner", this.contact],
      ],
    ];
  }
}
</script>
