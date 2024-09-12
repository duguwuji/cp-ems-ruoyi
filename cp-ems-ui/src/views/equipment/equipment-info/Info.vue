<template>
  <div>
    <el-form label-width="100px">
      <el-form-item label="名称" prop="name">
          <el-input v-model="equipmentInfo.name" placeholder="请输入名称" :disabled="true" />
        </el-form-item>
        <el-form-item label="设备SN" prop="sn">
          <el-input v-model="equipmentInfo.sn" placeholder="请输入设备SN" :disabled="true" />
        </el-form-item>
        <el-form-item label="设备型号" prop="model">
          <el-input v-model="equipmentInfo.model" placeholder="请输入设备型号" :disabled="true" />
        </el-form-item>
        <el-form-item label="设备类型" prop="type">
          <el-select v-model="equipmentInfo.type" placeholder="请选择" :disabled="true">
            <el-option
              v-for="item in dict.type.energy_type"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="设备描述" prop="description">
          <el-input v-model="equipmentInfo.description" placeholder="请输入设备描述" :disabled="true" />
        </el-form-item>
        <el-form-item label="设备图片" prop="img">
          <!-- <el-input v-model="form.img" placeholder="请输入设备图片" /> -->
          <imageUpload v-if="equipmentInfo.img" v-model="equipmentInfo.img" :limit="2" :values="equipmentInfo.img" :disable="true"/>
          <span v-else style="color: var(--base-color-3)">暂无内容</span>
        </el-form-item>
        <el-form-item label="设备二维码" prop="qrCode">
          <!-- <el-input v-model="equipmentInfo.qrCode" placeholder="请输入设备二维码" :disabled="true" /> -->
          <imageUpload v-if="equipmentInfo.qrCode" v-model="equipmentInfo.qrCode" :limit="2" :values="equipmentInfo.qrCode" :disable="true"/>
          <span v-else style="color: var(--base-color-3)">暂无内容</span>
        </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  dicts: ['energy_type'],
  props: {
    equipmentInfo: {
      type: Object,
      required: true
    }
  },
  data() {
    return {

    }
  },
  methods: {

  }
}
</script>

<style scoped>

</style>
