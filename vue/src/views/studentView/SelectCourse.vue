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

    <div style="margin-bottom: 20px">
      <el-select v-model="currentSemester" @change="loadTimetable">
        <el-option label="2025-第一学期" value="202501"></el-option>
        <el-option label="2025-第二学期" value="202502"></el-option>
      </el-select>
    </div>

    <!-- 课程表容器 -->
    <template>
      <el-table
          :data="gridData"
          border
          style="width: 100%;"
          :span-method="spanMethod"
          :cell-style="cellStyle">

        <!-- 时间列 -->
        <el-table-column
            prop="time"
            label="时间"
            width="120"
            align="center">
        </el-table-column>

        <!-- 星期列 -->
        <el-table-column
            v-for="(day, index) in ['周一','周二','周三','周四','周五']"
            :key="index"
            :prop="`day${index+1}`"
            :label="day"
            align="center">
          <template v-slot="{ row, column }">
            <div v-if="row[column.property]"
                 class="course-cell"
                 :style="{
               backgroundColor: row[column.property].color,
               height: cellHeight(row[column.property].span)
             }">
              <div class="course-content">
                {{ row[column.property].courseName }}
                <el-divider style="margin: 5px 0"></el-divider>
                {{ row[column.property].teacherName }}
              </div>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </template>


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
        currentSemester: "202501",
        semester: "",
        courseId: "",
        jobNumber: "",
        teacherName: "",
        classTime: "",
        form: "",
        dialogFormVisible: false,
        multipleSelection: [],
        timetableData:[],
        gridData: [],
        headerBg: 'headerBg',
      }
  },
  created() {
    this.load();
    this.loadTimetable();
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
      }).catch(
          err => {
            console.error("请求失败", err);
            this.$message.error("数据加载失败，请稍后再试");
          })
    },

    loadTimetable() {
      this.request.get("/courseSelection/timetable", {
        params: {
          semester: this.currentSemester
        }
      }).then(res => {
        console.log("后端返回的数据：", res);

        // 检查返回数据格式
        if (res.code !== 200 || !Array.isArray(res.data.data)) {
          this.$message.error("数据格式错误，请联系管理员！");
          return;
        }

        this.processTimetableData(res.data.data)
      }).catch(
          err => {
            console.error("请求失败", err);
            this.$message.error("数据加载失败，请稍后再试");
          })
    },

    processTimetableData(rawData) {

      console.log("rawData:", rawData);

      // 创建12节课的时间槽位
      const timeSlots = Array.from({length: 12}, (_, i) => ({
        time: `${i+1} ${this.getTimeRange(i+1)}`,
        day1: null, day2: null, day3: null, day4: null, day5: null
      }));

      // 颜色生成器（按课程ID生成固定颜色）
      const colorMap = new Map();
      const hueBase = Math.random() * 360; // 生成基础色相

      rawData.forEach(selection => {
        console.log("当前数据:", selection);
        if (!selection.classTime || typeof selection.classTime !== "string") {
          console.error("classTime 非法数据:", selection);
          return; // 跳过这个数据
        }
        // 解析classTime格式 "星期三1-4"
        const [weekdayStr, slotRange] = selection.classTime.split(/(?<=星期[一二三四五])/);
        if (!slotRange) {
          console.error("classTime 解析失败:", selection.classTime);
          return;
        }
        const weekdayMap = {
          "星期一": 1, "星期二": 2, "星期三": 3, "星期四": 4, "星期五": 5
        };
        const weekday = weekdayMap[weekdayStr];

        // 解析节次范围
        const [start, end] = slotRange.split('-').map(Number);

        // 生成课程颜色（相同课程相同颜色）
        if (!colorMap.has(selection.courseId)) {
          colorMap.set(selection.courseId,
              `hsl(${(hueBase + Math.random()*50) % 360}, 70%, 60%)`);
        }

        // 填充时间段
        for (let slot = start; slot <= end; slot++) {
          const slotIndex = slot - 1;
          const prop = `day${weekday}`;

          // 只填充第一个单元格，后续合并
          if (slot === start) {
            timeSlots[slotIndex][prop] = {
              courseName: selection.courseName,
              teacherName: selection.teacherName,
              color: colorMap.get(selection.courseId),
              span: end - start + 1
            };
          } else {
            timeSlots[slotIndex][prop] = { ...timeSlots[start-1][prop] };
          }
        }
      });

      this.gridData = timeSlots;
    },

    spanMethod({ row, column, rowIndex, columnIndex }) {
      if (columnIndex === 0) return;

      const prop = column.property
      const course = row[prop];

      if (course && course.span) {
        return {
          rowspan: course.span,
          colspan: 1
        };
      }
      return { rowspan: 1, colspan: 1 };
    },

    getTimeRange(slot) {
      const timeMap = {
        1: '8:00-8:45',
        2: '8:55-9:40',
        3: '10:00-10:45',
        4: '10:55-11:40',
        5: '13:00-13:45',
        6: '13:55-14:40',
        7: '15:00-15:45',
        8: '15:55-16:40',
        9: '18:00-18:45',
        10: '18:55-19:40',
        11: '20:00-20:45',
        12: '20:55-21:40'
      };
      return timeMap[slot];
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
    cellHeight(span) {
      // 每节课高度60px，合并单元格需要计算总高度
      return span ? `${span * 60 - 2}px` : 'auto';
    },
    cellStyle({ row, column, rowIndex }) {
      // 隐藏被合并单元格的边框
      if (column.property !== 'time' && row[column.property]?.span) {
        return { border: 'none' };
      }
    }

  }
}
</script>

<style scoped>
.headerBg {
  background-color: aliceblue !important;
}
.course-cell {
  padding: 0 !important;
  position: relative;
}
.course-content {
  padding: 8px;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  color: white;
  font-size: 12px;
}
</style>