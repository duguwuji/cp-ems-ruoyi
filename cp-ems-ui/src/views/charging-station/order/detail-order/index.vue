<template>
  <div class="app-container bg-container">
    <div class="detail-top">
      <!-- <div style="font-size: 26px;font-weight: bold;color: #595959;">订单详情</div> -->
      <el-row :gutter="12">
        <el-col :span="12">
          <div style="min-height: 300px;">
            <div class="order-title">订单信息</div>
            <el-descriptions :column="2" style="padding: 0 24px;">
              <el-descriptions-item label="订单编号">{{orderInfo.orderNo}}</el-descriptions-item>
              <el-descriptions-item label="下单时间">{{orderInfo.startTime}}</el-descriptions-item>
              <el-descriptions-item label="用户名称">{{orderInfo.userName}}</el-descriptions-item>
              <el-descriptions-item label="联系电话">{{orderInfo.phone}}</el-descriptions-item>
              <el-descriptions-item label="账户余额">{{orderInfo.settleBalance}} 元</el-descriptions-item>
              <el-descriptions-item label="车牌号">{{orderInfo.carNo}}</el-descriptions-item>
              <el-descriptions-item label="VIN">{{orderInfo.carVin}}</el-descriptions-item>
              <el-descriptions-item label="品牌">--</el-descriptions-item>
              <el-descriptions-item label="车型">--</el-descriptions-item>
              <el-descriptions-item label="充电方式">
                <dict-tag :options="dict.type.charge_method" :value="orderInfo.chargeMethod" size="mini"/>
              </el-descriptions-item>
              <el-descriptions-item label="充电电量">{{orderInfo.energy}} kW·h</el-descriptions-item>
              <el-descriptions-item label="充电时长">{{orderInfo.chargeDuration}} h</el-descriptions-item>
              <el-descriptions-item label="结算金额">{{orderInfo.settlePrice}} 元</el-descriptions-item>
              <el-descriptions-item label="优惠金额">{{orderInfo.discountAmt}} 元</el-descriptions-item>
              <el-descriptions-item label="支付金额">{{orderInfo.paidPrice}} 元</el-descriptions-item>
              <el-descriptions-item label="支付方式">
                <dict-tag :options="dict.type.pay_type" :value="orderInfo.payType" size="mini"/>
              </el-descriptions-item>
              <el-descriptions-item label="订单来源">
                <dict-tag :options="dict.type.order_source" :value="orderInfo.orderSource" size="mini"/>
              </el-descriptions-item>
              <el-descriptions-item label="订单状态">
                <dict-tag :options="dict.type.order_status" :value="orderInfo.orderStatus" size="mini"/>
              </el-descriptions-item>
              <el-descriptions-item label="结算类型">
                <dict-tag :options="dict.type.settle_type" :value="orderInfo.settleType" size="mini"/>
              </el-descriptions-item>
              <el-descriptions-item label="备注">{{orderInfo.remark ? orderInfo.remark : '--'}}</el-descriptions-item>
            </el-descriptions>
          </div>
        </el-col>
        <el-col :span="12">
          <div style="min-height: 300px;">
            <div class="order-title">充电桩信息</div>
            <el-descriptions :column="2" style="padding: 0 24px;">
              <el-descriptions-item label="终端编码">34RFT54YT56Y65G543F</el-descriptions-item>
              <el-descriptions-item label="终端名称">{{orderInfo.pileName}}</el-descriptions-item>
              <el-descriptions-item label="品牌">测试品牌</el-descriptions-item>
              <el-descriptions-item label="型号">test-01</el-descriptions-item>
              <el-descriptions-item label="归属电站">{{orderInfo.stationName}}</el-descriptions-item>
              <el-descriptions-item label="归属商户">{{orderInfo.merchantName}}</el-descriptions-item>
              <el-descriptions-item label="联系电话">{{merchant.contact}}</el-descriptions-item>
              <el-descriptions-item label="电站地址">测试地址</el-descriptions-item>
            </el-descriptions>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="detail-bottom">
      <el-row :gutter="12">
        <el-col :span="12">
          <div style="min-height: 350px;margin-bottom: 24px;">
            <div class="order-title">需求电压/实际电压</div>
            <OrderLineChartVue height="300px" :seriesName="['需求电压', '实际电压']"/>
          </div>
        </el-col>
        <el-col :span="12">
          <div style="min-height: 350px;margin-bottom: 24px;">
            <div class="order-title">需求电流/实际电流</div>
            <OrderLineChartVue height="300px" :seriesName="['需求电流', '实际电流']" yName="电流/A" :itemColor="['#7d6fed', '#37dcfd']" :chartData="{require: [346,268, 316, 346, 248, 313, 293],real: [246,283, 316, 219, 386, 327, 276]}"/>
          </div>
        </el-col>
        <el-col :span="12">
          <div style="min-height: 350px;margin-bottom: 24px;">
            <div class="order-title">SOC</div>
            <OrderLineChartVue height="300px" :seriesName="['SOC']" yName="SOC/%" :itemColor="['#77f1dc', '#37dcfd']" :chartData="{require: [36,47, 56, 58, 68, 69, 80]}"/>
          </div>
        </el-col>
        <el-col :span="12">
          <div style="min-height: 350px;margin-bottom: 24px;">
            <div class="order-title">电池温度</div>
            <OrderLineChartVue height="300px" :seriesName="['电池温度']" yName="温度/℃" :itemColor="['#fb9959', '#37dcfd']" :chartData="{require: [53,47, 47, 52, 56, 49, 57]}"/>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import {getOrderInfo} from "@/api/chargingStation/orderInfo"
import OrderLineChartVue from './OrderLineChart.vue'
export default {
  dicts: ['order_status', 'charge_method', 'order_source', 'settle_type', 'pay_type'],
  components: {
    OrderLineChartVue
  },
  data() {
    return {
      orderInfo: {},
      merchant: {}
    }
  },
  created() {
    const orderId = this.$route.params && this.$route.params.orderId;
    this.getOrder(orderId)
  },
  methods: {
    // 获取订单详情
    getOrder(orderId) {
      getOrderInfo(orderId).then(res => {
        this.orderInfo = res.data
        this.merchant = res.data.merchant
      })
    },
  }
}
</script>

<style scoped>
.app-container {
  min-height: calc(100vh - 84px);
  padding: 12px;
}
.detail-top {
  background: var(--base-item-bg);
  padding: 12px;
}
.detail-bottom {
  background: var(--base-item-bg);
  padding: 12px 12px 0 12px;
  margin-top: 12px;
}
.order-title {
  margin-bottom: 24px;
  font-size: 18px;
  color: var(--base-color-1);
  font-weight: bold;
  display: flex;
  align-items: center;
}
.order-title::before {
  content: '';
  width: 6px;
  height: 24px;
  background-color: var(--current-color);
  display: inline-block;
  margin-right: 8px;
}
</style>
