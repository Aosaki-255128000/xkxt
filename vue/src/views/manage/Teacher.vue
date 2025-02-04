<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="请输入用户名" suffix-icon="el-icon-search" v-model="username"></el-input>
      <el-input style="width: 200px" placeholder="请输入教师姓名" suffix-icon="el-icon-search" class="ml-5" v-model="name"></el-input>
      <el-input style="width: 200px" placeholder="请输入教师工号" suffix-icon="el-icon-search" class="ml-5" v-model="jobNumber"></el-input>
      <el-input style="width: 200px" placeholder="请输入教师职称" suffix-icon="el-icon-search" class="ml-5" v-model="title"></el-input>
      <el-input style="width: 200px" placeholder="请输入教师院系号" suffix-icon="el-icon-search" class="ml-5" v-model="departmentId"></el-input>
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
    <el-table-column prop="username" label="用户名" width="140"></el-table-column>
    <el-table-column prop="name" label="姓名" width="120"></el-table-column>
    <el-table-column prop="sex" label="性别"></el-table-column>
    <el-table-column prop="title" label="职称"></el-table-column>
    <el-table-column prop="jobNumber" label="工号"></el-table-column>
    <el-table-column prop="departmentId" label="院系号"></el-table-column>
    <el-table-column prop="role" label="角色"></el-table-column>
    <el-table-column prop="birthDate" label="出生日期"></el-table-column>
    <el-table-column label="操作" width="200" align="center">
      <template v-slot:default="scope">
        <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
        <el-popconfirm
            class="ml-5"
            confirm-button-text="确定"
            cancel-button-text="我再想想"
            icon="el-icon-info"
            icon-color="red"
            title="这是一段内容确定删除吗？"
            @confirm="del(scope.row.id)"
        >
          <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
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
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>

  <el-dialog title="教师信息" :visible.sync="dialogFormVisible">
    <el-form label-width="60px" size="small">
      <el-form-item label="教师用户名">
        <el-input v-model="form.username" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="姓名">
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-select v-model="form.sex" placeholder="请选择性别">
          <el-option label="男" value="男"></el-option>
          <el-option label="女" value="女"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="职称">
        <el-select v-model="form.title" placeholder="请选择职称">
          <el-option label="教授" value="教授"></el-option>
          <el-option label="副教授" value="副教授"></el-option>
          <el-option label="讲师" value="讲师"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="教师工号">
        <el-input v-model="form.jobNumber" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="院系号">
        <el-select v-model="form.departmentId" placeholder="请选择院系">
          <el-option
              v-for="dept in departments"
              :key="dept.id"
              :label="dept.name"
              :value="dept.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="角色">
        <el-select v-model="form.role" placeholder="请选择角色">
          <el-option label="老师" value="teacher"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="出生日期">
        <el-date-picker
            v-model="form.birthDate"
            type="date"
            placeholder="选择日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
        ></el-date-picker>
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
  name: "Teacher.vue",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      username: "",
      name: "",
      sex: "",
      title: "",
      jobNumber: "",
      departmentId: null,
      role: "",
      birthDate: null,
      form: "",
      dialogFormVisible: false,
      multipleSelection: [],
      headerBg: 'headerBg',
      departments: []
    }
  },
  created() {
    this.load();
    this.loadDepartments();
  },
  methods: {
    load() {
      this.request.get("/teacher/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username,
          name: this.name,
          sex: this.sex,
          title: this.title,
          jobNumber: this.jobNumber,
          departmentId: this.departmentId,
          birthDate: this.birthDate,
          role: this.role,
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
    loadDepartments() {
      this.request
          .get("/department")
          .then((res) => {
            console.log(res);
            this.departments = res;
          })
          .catch((err) => {
            console.error("加载院系失败", err);
            this.$message.error("院系数据加载失败，请稍后再试");
          });
    },
    save() {
      this.request.post("/teacher", this.form).then(res => {
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

    del(id) {
      this.request.delete(`/teacher/${id}`).then(res => {
        if(res) {
          this.$message.success("删除成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },

    closeDialog() {
      this.dialogFormVisible = false;
    },

    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },

    reset() {
      this.username = ""
      this.name = ""
      this.sex = ""
      this.title = ""
      this.jobNumber = ""
      this.departmentId = null
      this.role = ""
      this.birthDate = null
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