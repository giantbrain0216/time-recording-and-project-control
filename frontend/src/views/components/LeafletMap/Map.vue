<template>

  <div style="height: 300px; width: 100%;">
<!--    <div style="height: 200px; overflow: auto;">-->
<!--      <p>First marker is placed at {{ withPopup.lat }}, {{ withPopup.lng }}</p>-->
<!--      <p>Center is at {{ currentCenter }} and the zoom is: {{ currentZoom }}</p>-->
<!--      <button @click="showLongText">-->
<!--        Toggle long popup-->
<!--      </button>-->
<!--      <button @click="showMap = !showMap">-->
<!--        Toggle map-->
<!--      </button>-->
<!--    </div>-->
    <l-map
        v-if="showMap"
        :zoom="zoom"
        :center="center"
        :options="mapOptions"
        style="height: 80%;border-radius: 10px !important; "
    >
      <l-tile-layer
          :url="url"
          :attribution="attribution"
      />
<!--      <l-marker :lat-lng="withPopup">-->
<!--        <l-popup>-->
<!--          <div @click="innerClick">-->
<!--            I am a popup-->
<!--            <p v-show="showParagraph">-->
<!--              Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque-->
<!--              sed pretium nisl, ut sagittis sapien. Sed vel sollicitudin nisi.-->
<!--              Donec finibus semper metus id malesuada.-->
<!--            </p>-->
<!--          </div>-->
<!--        </l-popup>-->
<!--      </l-marker>-->
      <l-marker :lat-lng="withTooltip">
        <l-tooltip :options="{ permanent: true, interactive: true }">
          <div>
            {{name}}
          </div>
        </l-tooltip>
      </l-marker>
    </l-map>
  </div>
</template>

<script>
import { latLng } from "leaflet";
import { LMap, LTileLayer, LMarker, LTooltip } from "vue2-leaflet";
import { Icon } from 'leaflet';
import axios from "axios";

export default {
  name: "Map",
  props: {address:String,name:String},
  components: {
    LMap,
    LTileLayer,
    LMarker,
    LTooltip
  },
  data() {
    return {
      zoom: 13,
      center: latLng(48.70977345, 8.99925166764352),
      url: 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
      attribution:
          '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors',
      withTooltip: latLng(48.70977345, 8.99925166764352),
      currentZoom: 11.5,
      mapOptions: {
        zoomSnap: 0.5
      },
      showMap: true
    };
  },

  watch: {
    address: async function() {

      axios.get("https://nominatim.openstreetmap.org/search?q=" + this.address.replace(" ", "+") + "&format=json").then((response) => {
        this.center = latLng(response.data[0].lat,response.data[0].lon)
        this.withTooltip = latLng(response.data[0].lat,response.data[0].lon)
      })

    }
  },

  methods: {

  },
  async created() {
    delete Icon.Default.prototype._getIconUrl;
    Icon.Default.mergeOptions({
      iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
      iconUrl: require('leaflet/dist/images/marker-icon.png'),
      shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
    });
    if(this.address != null){
      axios.get("https://nominatim.openstreetmap.org/search?q=" + this.address.replace(" ", "+") + "&format=json").then((response) => {
        this.center = latLng(response.data[0].lat,response.data[0].lon)
        this.withTooltip = latLng(response.data[0].lat,response.data[0].lon)
      })}

  }
};
</script>