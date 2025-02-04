<template>
  <div class="login-container">
    <el-card class="box-card" shadow="never">
      <div slot="header" class="clearfix header-title">
        <span>用户登录</span>
      </div>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px" class="demo-ruleForm">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="ruleForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="ruleForm.password" autocomplete="off" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="ruleForm.role" placeholder="请选择角色" style="width: 100%;">
            <el-option label="管理员" value="admin"></el-option>
            <el-option label="学生" value="student"></el-option>
            <el-option label="老师" value="teacher"></el-option>

          </el-select>
        </el-form-item>
        <el-form-item>
          <div class="button-container">
            <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
            <el-button @click="resetForm('ruleForm')" style="margin-top: 10px;">重置</el-button>
          </div>
        </el-form-item>
      </el-form>
      <p class="register-link text-center">
        没有账号？<router-link to="/register">立即注册</router-link>
      </p>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'LoginView',
  data() {
    return {
      ruleForm: {
        username: '',
        password: '',
        role: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        role: [
          { required: true, message: '请选择角色', trigger: 'change' }
        ]
      }
    };
  },
  methods: {
    async submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          console.log(this.ruleForm);  // 打印检查密码字段
          try {
            // 根据角色动态选择后端登录接口和跳转路径
            let loginUrl = '';
            let redirectPath = '';
            loginUrl = `/${this.ruleForm.role}/login`;

            if (this.ruleForm.role === 'admin') {
              redirectPath = '/manage';
            } else if (this.ruleForm.role === 'student') {
              redirectPath = '/studentView';
            } else if (this.ruleForm.role === 'teacher') {
              redirectPath = '/teacherView';
            }

            const response = await this.$axios.post(loginUrl, this.ruleForm);

            if (response.data.code === 200) {
              this.$message.success('登录成功！');
              // 存储 token
              const token = response.data.data.token;
              console.log("Received JWT: ", token);
              localStorage.setItem('token', token);
              // 跳转
              await this.$router.push(redirectPath);
            } else {
              this.$message.error(response.data.message || '用户名或密码错误');
            }
          } catch (error) {
            this.$message.error('服务器异常，请稍后重试');
          }
        }
      });
    },

    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.box-card {
  width: 400px;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.header-title {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
}

.register-link {
  margin-top: 15px;
  font-size: 14px;
  color: #909399;
}

.register-link a {
  color: #409EFF;
  text-decoration: none;
}

.text-center {
  text-align: center;
}

.button-container {
  display: flex;
  flex-direction: column;
  align-items: center; /* 水平居中 */
}

.button-container .el-button {
  width: 100%; /* 确保按钮宽度为容器的100% */
  margin: 5px 0; /* 给按钮之间添加一些间距 */
}
</style>