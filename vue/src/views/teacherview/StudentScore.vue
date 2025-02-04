<template>
  <div>

    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="请输入学生ID" suffix-icon="el-icon-search" class="ml-5" v-model="studentId"></el-input>
      <el-input style="width: 200px" placeholder="请输入学期" suffix-icon="el-icon-search" class="ml-5" v-model="semester"></el-input>
      <el-input style="width: 200px" placeholder="请输入课号" suffix-icon="el-icon-search" class="ml-5" v-model="courseId"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
    </div>

    <div style="padding: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class = "el-icon-circle-plus-outline"/></el-button>
      <el-button type="danger" @click="">批量删除<i class = "el-icon-remove-outline"/></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80px"></el-table-column>
      <el-table-column prop="studentId" label="学生ID" width="120"></el-table-column>
      <el-table-column prop="studentName" label="学生姓名" width="120"></el-table-column>
      <el-table-column prop="semester" label="学期" width="140"></el-table-column>
      <el-table-column prop="courseId" label="课号" width="120"></el-table-column>
      <el-table-column prop="courseName" label="课程名称" width="120"></el-table-column>
      <el-table-column prop="usualPerformance" label="平时成绩"></el-table-column>
      <el-table-column prop="testScore" label="考试成绩"></el-table-column>
      <el-table-column prop="totalScore" label="总评成绩"></el-table-column>

      <el-table-column label="操作" width="200" align="center">
        <template v-slot:default="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
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
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="成绩填入" :visible.sync="dialogFormVisible">
      <el-form label-width="100px" size="small">
        <el-form-item label="平时成绩">
          <el-input v-model="form.usualPerformance" autocomplete="off" style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item label="考试成绩">
          <el-input v-model="form.testScore" autocomplete="off" style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item label="总评成绩">
          <el-input v-model="form.totalScore" autocomplete="off" style="width: 200px"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "StudentScore",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      studentId: "",
      semester: "",
      courseId: "",
      courseName: "",
      usualPerformance: null,
      testScore: null,
      totalScore: null,
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
      this.request.get("/courseSelection/studentResult", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          studentId: this.studentId,
          studentName: this.studentName,
          semester: this.semester,
          courseId: this.courseId,
          courseName: this.courseName,
          usualPerformance: this.usualPerformance,
          testScore: this.testScore,
          totalScore: this.totalScore
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data
        this.total = res.total
      }).catch(
          err => {
            console.log("请求失败", err);
            this.$message.error("数据加载失败，请稍后再试");
          })
    },

    save() {
      this.request.post("/courseSelection", this.form).then(res => {
        if(res) {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
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
      this.studentId = ""
      this.semester = ""
      this.courseId = ""
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