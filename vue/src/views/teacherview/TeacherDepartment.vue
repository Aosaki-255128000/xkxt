<template>
  <div>
    <div style="padding: 10px 0">
      <el-input style="width: 200px" suffix-icon="el-icon-search" placeholder="请输入学院名称" class="ml-5" v-model="name"></el-input>
      <el-input style="width: 200px" suffix-icon="el-icon-location-information" placeholder="请输入地址" class="ml-5" v-model="address"></el-input>
      <el-input style="width: 200px" suffix-icon="el-icon-phone-outline" placeholder="请输入地址" class="ml-5" v-model="phone"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
    </div>


    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column prop="id" label="ID" width="80px"></el-table-column>
      <el-table-column prop="name" label="学院名称"></el-table-column>
      <el-table-column prop="address" label="学院地址"></el-table-column>
      <el-table-column prop="phone" label="学院电话"></el-table-column>
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
  </div>
</template>

<script>
import {request} from "axios";

export default {
  name: "TeacherDepartment.vue",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      address: "",
      phone: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      headerBg: 'headerBg'
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(){
      this.request.get("/department/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          address: this.address,
          phone: this.phone
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
      this.username = ""
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
    }
  }
}
</script>

<style>
.headerBg {
  background-color: aliceblue !important;
}
</style>