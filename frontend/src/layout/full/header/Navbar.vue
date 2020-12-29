<template>
	<header class="gridx">
		<vs-navbar
      v-model="indexActive"
      :color="topbarColor"
      class="topnavbar" text-color="rgba(255,255,255,0.7)" active-text-color="rgba(255,255,255,1)">
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
      <div slot="title">
        <div class="hiddenDesktop cursor-pointer" @click.stop="activeSidebar"><vs-icon icon="menu"></vs-icon></div>
      </div>

      <vs-input style="color: #2b2b2b;font-size: 17px" class="inputx" />
      <vs-button class="m-1 fa fa-trash" color=""
                 icon="search" type="filled"></vs-button>






      <vs-spacer></vs-spacer>
       

    </vs-navbar>
     
	</header>

</template>

<script>
export default {
	name : 'Navbar',
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
    showToggle: false
    
  }),

  methods: {
      //This is for sidebar trigger in mobile
      activeSidebar() {
          this.$store.commit('IS_SIDEBAR_ACTIVE', true);
      }
      
  }
}
</script>
