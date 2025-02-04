<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="请输入学期" suffix-icon="el-icon-search" class="ml-5" v-model="semester"></el-input>
      <el-input style="width: 200px" placeholder="请输入课号" suffix-icon="el-icon-search" class="ml-5" v-model="courseId"></el-input>
      <el-input style="width: 200px" placeholder="请输入教师工号" suffix-icon="el-icon-search" class="ml-5" v-model="jobNumber"></el-input>
      <el-input style="width: 200px" placeholder="请输入开课时间" suffix-icon="el-icon-search" class="ml-5" v-model="classTime"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="semester" label="学期"></el-table-column>
      <el-table-column prop="courseId" label="课号"></el-table-column>
      <el-table-column prop="course.name" label="课程名称"></el-table-column>
      <el-table-column prop="course.credit" label="学分"></el-table-column>
      <el-table-column prop="course.creditHour" label="学时"></el-table-column>
      <el-table-column prop="jobNumber" label="教师工号"></el-table-column>
      <el-table-column prop="teacher.name" label="教师姓名"></el-table-column>
      <el-table-column prop="classTime" label="开课时间"></el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template v-slot:default="scope">
          <el-button type="success" @click="">选课 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text="确定"
              cancel-button-text="我再想想"
              icon="el-icon-info"
              icon-color="red"
              title="取消选课？"
              @confirm=""
          >
            <el-button type="danger" slot="reference">退课 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="sizes, prev, pager, next, jumper">
      </el-pagination>
    </div>


  </div>
</template>

<script>
export default {
  name: "SelectCourse.vue",
  data() {
      return {
        tableData: [],
        total: 0,
        pageNum: 1,
        pageSize: 10,
        semester: "",
        courseId: "",
        jobNumber: "",
        teacherName: "",
        classTime: "",
        form: "",
        dialogFormVisible: false,
        multipleSelection: [],
        headerBg: 'headerBg',
      }
  },
  created() {
    this.load();
  },
  methods: {
    load() {
      this.request.get("/openCourse/studentPage", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          semester: this.semester,
          courseId: this.courseId,
          jobNumber: this.jobNumber,
          teacherName: this.teacherName,
          classTime: this.classTime
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data
        this.total = res.total
      })
          .catch(
              err => {
                console.error("请求失败", err);
                this.$message.error("数据加载失败，请稍后再试");
              })
    },

    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },

    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },

    closeDialog() {
      this.dialogFormVisible = false;
    },

    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },

    reset() {
      this.semester = ""
      this.courseId = ""
      this.jobNumber = ""
      this.classTime = ""
      this.load()
    },
    handleSizeChange(pageSize){
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
  }
}
</script>

<style scoped>
.headerBg {
  background-color: aliceblue !important;
}
</style>