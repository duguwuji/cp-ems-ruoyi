<template>
  <div class="app-container flex-between bg-container">
    <div class="camera-tree">
      <TopologicaTree :showCheckbox="true" />
    </div>
    <div class="camera-list">
      <div
        class="camera-item"
        :class="cameraActive == 0 ? 'camera-item-active' : ''"
        @click="changeChannel(0)"
      >
        <CameraPlay v-if="cameraList[0]" :code="cameraList[0].cameraSn" />
      </div>
      <div
        class="camera-item"
        :class="cameraActive == 1 ? 'camera-item-active' : ''"
        @click="changeChannel(1)"
      >
        <CameraPlay v-if="cameraList[1]" :code="cameraList[1].cameraSn" />
      </div>
      <div
        class="camera-item"
        :class="cameraActive == 2 ? 'camera-item-active' : ''"
        @click="changeChannel(2)"
      >
        <CameraPlay v-if="cameraList[2]" :code="cameraList[2].cameraSn" />
      </div>
      <div
        class="camera-item"
        :class="cameraActive == 3 ? 'camera-item-active' : ''"
        @click="changeChannel(3)"
      >
        <CameraPlay v-if="cameraList[3]" :code="cameraList[3].cameraSn" />
      </div>
    </div>
    <!-- <div class="monitor-list" v-if="showList">
                <div class="monitor-item" v-for="(item, index) in monitor" :key="index">
                    <div class="item-title">{{ item.cameraName }}</div>
                    <div class="item-video">
                        <CameraPlay :code="item.cameraIndexCode" :accessToken="item.accessToken"
                            :accessUrl="item.accessUrl" />
                    </div>
                </div>
            </div> -->
  </div>
</template>

<script>
import TopologicaTree from "@/components/TopologicaTree";
import { getUrlBySerialNumber } from "@/api/system/cameraConfig";
import CameraPlay from "@/components/Camera/CameraPlay.vue";
import { listCameraConfig } from "@/api/system/cameraConfig";
export default {
  name: "RealtimeVideo",
  components: {
    CameraPlay,
    TopologicaTree,
  },
  data() {
    return {
      cameraList: [],
      cameraActive: 0,
    };
  },
  created() {
    // this.getMonitors();
    this.getDefaultCamera();
  },
  methods: {
    getDefaultCamera() {
      let params = {
        pageSize: 4,
        PageNum: 1,
      };
      listCameraConfig(params).then((res) => {
        this.cameraList = res.rows;
      });
    },
    changeChannel(code) {
      this.cameraActive = code;
    },
    // 获取当前页的监控数据
    // getMonitors() {
    //     let code = "G70578329";
    //     getUrlBySerialNumber(code).then(response => {
    //         let data ={
    //             accessToken:response.data.cameraToken,
    //             accessUrl:response.data.url,
    //             cameraIndexCode:response.data.cameraKey,
    //             cameraName: response.data.cameraName
    //         };
    //         this.monitor.push(data);
    //     });
    // },
  },
};
</script>
<style scoped>
.app-container {
  min-height: 500px;
  height: calc(100vh - 84px);
}
.camera-tree {
  width: 20%;
  background: var(--base-item-bg);
  margin-right: 10px;
  padding: 6px;
}
.camera-list {
  width: calc(80% - 6px);
  background: var(--base-item-bg);
  padding: 12px;
  display: grid;
  grid: 50% 50% / 50% 50%;
  /* // grid-gap: 12px; */
}
.camera-item {
  background: #4c4b4b;
  width: 100%;
  height: 100%;
  border: 1px solid #343434;
}
.camera-item-active {
  border: 1px solid var(--current-color);
}
</style>
