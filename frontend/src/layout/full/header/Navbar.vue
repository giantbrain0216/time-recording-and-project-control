<template>
	<header class="gridx">
		<vs-navbar
      v-model="indexActive"
      :color="topbarColor"
      class="topnavbar"
      style="z-index: 1000"
    >

      <!---
      Template logo
      --> 
      <div slot="title" class="themelogo">
        <img :src="logo" v-if="logo" alt="Dashboard"/>
        <span class="logo-text pr-2 pl-2 ml-1 mr-1" v-if="title">{{ title }}</span>
      </div>
      <!---
      Mobile toggle
      -->
      <vs-input style="color:black" icon="search" placeholder="Search" v-model="search" @keyup.enter='$router.push({name:"Search Results", params: {searchKey: search}});search=""'/>

      <div slot="title">
        <div class="hiddenDesktop cursor-pointer" @click.stop="activeSidebar"><vs-icon icon="menu"></vs-icon></div>
      </div>









      <vs-spacer></vs-spacer>

<!--      <vs-dropdown vs-trigger-click left class="cursor-pointer pr-2 pl-2 ml-1 mr-4">-->
<!--        <a class="text-white-dark" href="#">{{}}<vs-icon icon="wb_sunny"></vs-icon></a>-->
<!--          <vs-dropdown-menu class="topbar-dd">-->
<!--            <vs-dropdown-item><vue-weather :api-key="weatherKey" units="uk" hide-header="false"/></vs-dropdown-item>-->
<!--          </vs-dropdown-menu>-->
<!--      </vs-dropdown>-->
      <vue-weather :api-key="weatherKey" hide-header units="uk"  />

    </vs-navbar>
     
	</header>

</template>

<script>
import VueWeather from "vue-weather-widget";
import config from "@/config";

export default {
	name : 'Navbar',
  components : {
    VueWeather
  },
  props: {
      topbarColor: {
          type: String,
          default: "#2962ff",
      },
      title: {
            type: String,
      },
      logo: {
          type: String
      },
  },
  data:()=>({
    indexActive: 0,
    showToggle: false,
    search: "",
    weatherKey: config.apiKeyWeather,
    currentWeather : ""
    
  }),

  methods: {
      //This is for sidebar trigger in mobile
      activeSidebar() {
          this.$store.commit('IS_SIDEBAR_ACTIVE', true);
      }
      
  }
}
</script>
