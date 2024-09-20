<template>
  <div style="height: 100vh; display: flex; align-items: center; justify-content: center; background-color: #C4BE2E">
    <div style="display: flex; background-color: white; width: 75%; border-radius: 5px; overflow: hidden">
      <div style="flex: 1">
        <img src="@/assets/company.png" alt="" style="width: 100%">
      </div>
      <div style="flex: 1; display: flex; align-items: center; justify-content: center">
        <el-form :model="user" style="width: 80%" :rules="rules" ref="loginRef">
          <div style="font-size: 20px; font-weight: bold; text-align: center; margin-bottom: 20px">欢迎申请注册公司系统</div>
          <el-form-item prop="tenentid">
            <el-input prefix-icon="el-icon-star-on" size="medium" placeholder="请创建专属租户@exp:saas" v-model="user.tenentid"></el-input>
          </el-form-item>
          <el-form-item prop="tenentname">
            <el-input prefix-icon="el-icon-user" size="medium" placeholder="请输入公司名" v-model="user.tenentname"></el-input>
          </el-form-item>
          <el-form-item prop="fullname">
            <el-input prefix-icon="el-icon-user" size="medium" placeholder="请输入申请人全名" v-model="user.fullname"></el-input>
          </el-form-item>
          <el-form-item prop="Role">
            <el-input prefix-icon="el-icon-user" size="medium" placeholder="申请人职务@exp:CEO" v-model="user.role"></el-input>
          </el-form-item>
          <el-form-item prop="phonenumber" >
            <el-input prefix-icon="el-icon-phone" size="medium" placeholder="请输入中国手机号"
                      v-model="user.phonenumber"  maxlength="11" show-word-limit  ></el-input>
          </el-form-item>

          <el-form-item prop="code">
            <div style="display: flex">
              <el-input placeholder="请输入验证码" prefix-icon="el-icon-circle-check" size="medium" style="flex: 1" v-model="user.code"></el-input>
              <div style="flex: 1; height: 36px">
                <valid-code @update:value="getCode" />
              </div>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" style="width: 100%" @click="submit">提交申请</el-button>
          </el-form-item>
          <div style="display: flex">
            <div style="flex: 1">已有租户账号？请 <span style="color: #0f9876; cursor: pointer" @click="$router.push('/login')">登入</span></div>
            <div style="flex: 1; text-align: right"><span style="color: #0f9876; cursor: pointer">联络我们</span></div>
          </div>
        </el-form>
      </div>
    </div>

  </div>
</template>

<script>
import ValidCode from "@/components/ValidCode";

export default {
  name: "tenent",
  components: {
    ValidCode
  },
  data() {

    // 验证码校验
    const validateCode = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入验证码'))
      } else if (value.toLowerCase() !== this.code) {
        callback(new Error('验证码错误'))
      } else {
        callback()
      }
    }

    return {
      code: '',  // 验证码组件传递过来的code
      user: {
        code: '',   // 表单里用户输入的code 验证码
        tenentid: '',
        tenentname: '',
        fullname: '',
        Role: '',
        phonenumber: ''

      },
      rules: {
        tenentid: [
          { required: true, message: '请输入专属字号', trigger: 'blur' },
        ],
        tenentname: [
          { required: true, message: '请输入公司名', trigger: 'blur' },
        ],
        fullname: [
          { required: true, message: '请输入全名', trigger: 'blur' },
        ],
        Role: [
          { required: true, message: '请输入职务', trigger: 'blur' },
        ],
        phonenumber: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
        ],
        code: [
          { validator: validateCode, trigger: 'blur' }
        ],
      }
    }
  },
  created() {

  },
  methods: {
    getCode(code) {
      this.code = code.toLowerCase()
    },
    submit() {
      this.$refs['loginRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$request.post('/tenent', this.user).then(res => {
            if (res.code === '200') {
              this.$router.push('/login')
              this.$message.success('申请成功')
              localStorage.setItem("saas", JSON.stringify(res.data))  // 存储用户数据(还没数据）
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>