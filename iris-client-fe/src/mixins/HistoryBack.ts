import { Vue } from "vue-property-decorator";

const HistoryBack = (target: string) => {
  return Vue.extend({
    beforeRouteEnter(to, from, next) {
      next((vm) => {
        if (typeof from.name === "string") {
          (vm as unknown as { prevLocation: string }).prevLocation = from.name;
        }
      });
    },
    data() {
      return {
        target,
        prevLocation: null,
      };
    },
    methods: {
      goBack(): void {
        if (this.prevLocation === target) {
          return this.$router.back();
        }
        this.$router.replace({ name: target });
      },
    },
  });
};

export default HistoryBack;
