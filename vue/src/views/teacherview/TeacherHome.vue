<template>
  <div>
    <h1>欢迎您，{{ teacherName }}！这里是主页</h1>
  </div>
</template>

<script>
import { jwtDecode } from 'jwt-decode';

export default {
  name: "TeacherHome",
  data() {
    return {
      teacherName: ''
    };
  },
  created() {
    // 从本地存储获取token
    const token = localStorage.getItem('token');

    if (token) {
      try {
        const decoded = jwtDecode(token); // 使用正确的 jwt_decode
        console.log("Decoded JWT:", decoded);
        this.teacherName = decoded.name || "未知"; // 防止 name 为空
      } catch (error) {
        console.error('解析Token失败:', error);
        this.$message.error('登录信息已失效，请重新登录');
        this.$router.push('/login');
      }
    } else {
      // 没有token则跳转登录
      this.$router.push('/login');
    }
  }
}
</script>

<style scoped>
h1 {
  color: #2c3e50;
  margin: 20px 0;
}
</style>