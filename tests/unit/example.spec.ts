import { shallowMount } from "@vue/test-utils";
import HomeView from "@/views/home/home.vue";

describe("HelloWorld.vue", () => {
  it("can be mounted", () => {
    const wrapper = shallowMount(HomeView);
    expect(wrapper.exists()).toBe(true);
  });
});
