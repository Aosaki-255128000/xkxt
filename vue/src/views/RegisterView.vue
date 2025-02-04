<template>
  <div class="register-container">
    <el-card class="box-card" shadow="never">
      <div slot="header" class="clearfix header-title">
        <span>用户注册</span>
      </div>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px" class="demo-ruleForm">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="ruleForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="ruleForm.password" autocomplete="off" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPassword">
          <el-input type="password" v-model="ruleForm.checkPassword" autocomplete="off" placeholder="请再次输入密码"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="ruleForm.role" placeholder="请选择角色" style="width: 100%;">
            <el-option label="管理员" value="admin"></el-option>
          </el-select>
        </el-form-item>
        <!-- 可以根据需要添加更多的表单字段 -->
        <el-form-item>
          <div class="button-container">
            <el-button type="primary" @click="submitForm('ruleForm')">注册</el-button>
            <el-button @click="resetForm('ruleForm')" style="margin-top: 10px;">重置</el-button>
          </div>
        </el-form-item>
      </el-form>
      <p class="register-link text-center">
        已有账号？<router-link to="/login">立即登录</router-link>
      </p>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'RegisterView',
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.checkPassword !== '') {
          this.$refs.ruleForm.validateField('checkPassword');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.ruleForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      ruleForm: {
        username: '',
        password: '',
        checkPassword: '',
        role: 'admin'
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { validator: validatePass, trigger: 'blur' }
        ],
        checkPassword: [
          { validator: validatePass2, trigger: 'blur' }
        ],
        role: [
          { required: true, message: '请选择角色', trigger: 'change' }
        ]
      }
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const registerData = {
            username: this.ruleForm.username,
            password: this.ruleForm.password,
            role: this.ruleForm.role,
          };

          this.$axios.post('/admin/register', registerData)
              .then(response => {
                if (response.data.code === 200) {
                  this.$message({
                    message: '注册成功！',
                    type: 'success',
                  });
                  this.$router.push('/login');
                } else {
                  this.$message({
                    message: response.data.message || '注册失败，请重试！',
                    type: 'error',
                  });
                }
              })
              .catch(error => {
                console.error('注册失败:', error);
                this.$message({
                  message: '注册失败，服务器错误！',
                  type: 'error',
                });
              });
        } else {
          console.log('表单验证失败');
          return false;
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
.register-container {
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