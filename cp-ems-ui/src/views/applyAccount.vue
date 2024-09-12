<template>
  <div class="login">
    <div class="login-content">
      <img class="login-pic2" src="@/assets/logo/logo-heng.png" alt="" />
      <div class="login-form2">
        <div class="qrcode">
          <div style="text-align: center; font-size: 24px;position:absolute;top:18px;width:200px;">
            联系我们
          </div>
          <img class="qr-pic" src="../assets/images/wechat-QR.png" alt="" />
        </div>
        <div class="content-line">
        </div>
        <div class="submit-form">
          <div style="text-align: center; font-size: 24px;position:absolute;top:18px;width:456px;">
            申请体验账号
          </div>
          <div v-if="isShow" style="text-align:center;">信息已提交成功，我们会尽快与您联系。</div>
          <el-form v-else ref="form" :model="form" :rules="rules" style="margin-top: 24px;">
            <el-form-item prop="name">
              <el-input v-model="form.name" type="text" auto-complete="off" placeholder="请输入您的姓名" class="unchanged">
                <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
              </el-input>
            </el-form-item>
            <el-form-item prop="phone">
              <el-input v-model="form.phone" type="text" auto-complete="off" placeholder="请输入您的手机号" class="unchanged">
                <svg-icon slot="prefix" icon-class="phone" class="el-input__icon input-icon" />
              </el-input>
            </el-form-item>
            <el-form-item prop="email">
              <el-input v-model="form.email" type="text" auto-complete="off" placeholder="请输入邮箱" class="unchanged">
                <svg-icon slot="prefix" icon-class="email" class="el-input__icon input-icon" />
              </el-input>
            </el-form-item>
            <el-form-item prop="corporateName">
              <el-input v-model="form.corporateName" type="text" auto-complete="off" placeholder="请输入公司名称" class="unchanged">
                <svg-icon slot="prefix" icon-class="chart" class="el-input__icon input-icon" />
              </el-input>
            </el-form-item>

            <el-form-item prop="remark">
              <el-input v-model="form.remark" type="textarea" auto-complete="off" rows="5" placeholder="您的需求及问题..." class="unchanged">
                <svg-icon slot="prefix" icon-class="input" class="el-input__icon input-icon" />
              </el-input>
            </el-form-item>
            <el-button :loading="loading" size="medium" type="primary" class="btn-fixed"
              style="width: 70%;margin-left: 15%" @click="handleApply">
              <span>提交申请</span>
              <!-- <span v-else>登 录 中...</span> -->
            </el-button>
          </el-form>
        </div>
      </div>
    </div>
    <div class="bg-line"></div>

    <!--  底部  -->
    <div class="el-login-footer">
      <!-- <span>Copyright © 2022-2023 苏州云脉 版权所有.</span> -->
    </div>
  </div>
</template>

<script>

export default {
  name: "applyAccount",
  data() {
    return {
      form: {},
      rules: {
        name: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        email: [
          { required: true, message: "邮箱不能为空", trigger: "blur" }
        ],
        corporateName: [
          { required: true, message: "公司名称不能为空", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "联系电话不能为空", trigger: "blur" }
        ],
      },
      loading: false,
      isShow: false,
    };
  },
  created() {
  },
  methods: {
    handleApply() {
      this.$refs["form"].validate(valid => {
        if(valid) {
          this.loading = true
          fetch('https://formspree.io/f/xwkjbzkd', {
            method: 'POST',
            body: JSON.stringify(this.form),
            headers: {
              'Accept': 'application/json'
            }
          }).then(res => {
            if(res.ok) {
              this.$modal.msgSuccess('信息提交成功！')
              this.isShow = true
            } else {
              res.json().then(data => {
                if(Object.hasOwn(data, 'errors')) {
                  console.log(data['errors'].map(error => error['message']).join(', '));
                  this.$modal.msgError(data['errors'].map(error => error['message']).join(', '))
                } else {
                  this.$modal.msgError('遇到了一个问题，请通过微信咨询。')
                }
              })
            }
          }).finally(() => {
            this.loading = false
          })
        }
        // if (valid) {
        //   this.buttonLoading = true;
        //   addAccountApplication(this.form).then(response => {
        //     this.$modal.msgSuccess("提交成功");
        //     this.open = false;
        //     this.getList();
        //   }).finally(() => {
        //     this.buttonLoading = false;
        //   });
        // }
      });
    }
  },
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
.login {
    position: relative;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: center;
    -ms-flex-pack: center;
    justify-content: center;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    height: 100%;
    background-image: url("../assets/images/cta-bg.jpg");
    background-size: cover;
}
.login-content {
    z-index: 999;
    padding: 0 10.8%;
    position: absolute;
    top: 25%;
    left: 50%;
    -webkit-transform: translateX(-50%);
    transform: translateX(-50%);
}
.title-of-choose {
  width: 86.2%;
  margin: 0 6.9% 14px;
}

.title-item {
  position: relative;
  display: flex;
  justify-content: space-between;
}

.title-item::after {
  content: "";
  position: absolute;
  left: 0;
  bottom: 0;
  height: 2px;
  width: 100%;
  background-color: rgba(165, 165, 165, 0.363);
  z-index: 99;
}

.login-role {
  cursor: pointer;
  font-size: 24px;
  line-height: 64px;
  padding: 0 36px;
  font-weight: bold;
  color: #0087e7;
  border-bottom: 2px solid #0087e7;
  z-index: 999;
}

.login-role-normal {
  cursor: pointer;
  font-size: 24px;
  line-height: 64px;
  padding: 0 36px;
  z-index: 999;
}

.cube-t {
  width: 168px;
  background-color: #0087e7;
}

.login-pic2 {
  position: absolute;
  width: 40%;
  top: -24%;
  left: 50%;
  transform: translateX(-50%);
  z-index: 3;
}

.home-b {
  z-index: 999;
  position: absolute;
  top: 3%;
  right: 3%;
}

.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form2 {
  display: flex;
  justify-content: space-around;
  align-items: center;
  border-radius: 6px;
  background: #ffffff;
  width: 1000px;
  padding: 18px 44px 18px;
  box-shadow: 0px 6px 10px rgba(0, 0, 0, 0.3);

  .el-input {
    height: 40px;

    input {
      height: 40px;
    }
  }

  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}

.login-code {
  width: 33%;
  height: 38px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}

.login-code-img {
  height: 38px;
}

.dividing-line {
  background-image: url("../assets/images/cta-bg.jpg");
  background-size: 100% 100%;
  text-align: center;
  color: #c6c6c6;
  font-size: 0.94rem;
}
.qrcode{
  align-content: center;
}
.qr-pic{
  height: 200px;
}
.submit-form{
  width: 50%;
}
.content-line {
  width:1px;
  background:linear-gradient(to bottom, rgba(3, 3, 3, 0), rgba(3, 3, 3, .4), rgba(3, 3, 3, 0));
  height:499px;
}
</style>
