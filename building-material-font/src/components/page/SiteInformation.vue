<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-news"></i> 工地信息
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button
                        type="primary"
                        icon="el-icon-delete"
                        class="handle-del mr10"
                        @click="delAllSelection"
                >批量删除</el-button>
                <el-button
                        type="success"
                        icon="el-icon-plus"
                        class="handle-del mr10"
                        @click="handleAdd"
                >新增</el-button>

                <el-input v-model="query.siteName" placeholder="工地名称" class="handle-input mr10"></el-input>
                <el-select v-model="query.siteState" placeholder="状态" class="handle-select mr10">
                    <el-option key="0" label="在建" value="0"></el-option>
                    <el-option key="1" label="竣工" value="1"></el-option>
                </el-select>
                <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
                <el-button type="info" icon="el-icon-refresh" @click="doEmpty">清空</el-button>
            </div>
            <el-table
                    :data="siteInformationData"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
                    @selection-change="handleSelectionChange"
            >
                <el-table-column type="selection" width="55" align="center" prop="id"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" prop="siteName" label="名称"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" prop="siteAddress" label="地址"></el-table-column>
                <el-table-column prop="siteContact" label="负责人"></el-table-column>
                <el-table-column prop="contactPhone" label="联系电话"></el-table-column>
                <el-table-column prop="siteState" label="状态">
                    <template scope="scope">
                        <el-tag type="danger" v-if="scope.row.siteState == 0">在建</el-tag>
                        <el-tag type="success" v-else>竣工</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="startDate" label="开工日期"></el-table-column>
                <el-table-column prop="expectedEndDate" label="预期完成日期"></el-table-column>
                <el-table-column prop="factEndDate" label="实际完成日期"></el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button
                                type="text"
                                icon="el-icon-info"
                                class="green"
                                @click="handPut(scope.$index, scope.row)"
                        >供料</el-button>
                        <el-button
                                type="text"
                                icon="el-icon-edit"
                                @click="handleEdit(scope.$index, scope.row)"
                        >编辑</el-button>
                        <el-button
                                type="text"
                                icon="el-icon-delete"
                                class="red"
                                @click="handleDelete(scope.$index, scope.row)"
                        >删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                        background
                        layout="total, prev, pager, next"
                        :current-page="query.pageIndex"
                        :page-size="query.pageSize"
                        :total="pageTotal"
                        @current-change="handlePageChange"
                ></el-pagination>
            </div>
        </div>


        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
            <el-form ref="editForm" :rules="rules" :model="editForm" label-width="80px">
                <el-form-item label-width="106px" label="名称" prop="siteName">
                    <el-input v-model="editForm.siteName" placeholder="请输入工地名称"></el-input>
                </el-form-item>
                <el-form-item label-width="106px" label="地址" prop="siteAddress">
                    <el-input v-model="editForm.siteAddress" placeholder="请输入工地地址"></el-input>
                </el-form-item>
                <el-form-item label-width="106px" label="负责人" prop="siteContact">
                    <el-input v-model="editForm.siteContact" placeholder="请输入负责人"></el-input>
                </el-form-item>
                <el-form-item label-width="106px" label="电话" prop="contactPhone">
                    <el-input v-model="editForm.contactPhone" placeholder="请输入电话号码"></el-input>
                </el-form-item>
                <el-form-item label-width="106px"  label="开工日期" prop="startDate">
                    <el-date-picker v-model="editForm.startDate" style="width: 100%" type="date" placeholder="请选择开工日期" value-format="yyyy-MM-dd" ></el-date-picker>
                </el-form-item>
                <el-form-item label-width="106px" label="预期完成日期" prop="expectedEndDate">
                    <el-date-picker v-model="editForm.expectedEndDate" style="width: 100%" type="date" placeholder="请选择预期完成日期" value-format="yyyy-MM-dd" ></el-date-picker>
                </el-form-item>
                <el-form-item label-width="106px" label="实际完成日期" prop="factEndDate">
                    <el-date-picker :picker-options="factEndDateFun"  v-model="editForm.factEndDate" style="width: 100%" type="date" placeholder="请选择预期完成日期" value-format="yyyy-MM-dd" ></el-date-picker>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>

        <!--新增弹出框-->
        <el-dialog title="新增" :visible.sync="addVisible" width="30%">
            <el-form ref="addForm" :rules="rules" :model="addForm" label-width="80px">
                <el-form-item label-width="106px" label="名称" prop="siteName">
                    <el-input v-model="addForm.siteName" placeholder="请输入工地名称"></el-input>
                </el-form-item>
                <el-form-item label-width="106px" label="地址" prop="siteAddress">
                    <el-input v-model="addForm.siteAddress" placeholder="请输入工地地址"></el-input>
                </el-form-item>
                <el-form-item label-width="106px" label="负责人" prop="siteContact">
                    <el-input v-model="addForm.siteContact" placeholder="请输入负责人"></el-input>
                </el-form-item>
                <el-form-item label-width="106px" label="电话" prop="contactPhone">
                    <el-input v-model="addForm.contactPhone" placeholder="请输入电话号码"></el-input>
                </el-form-item>
                <el-form-item label-width="106px"  label="开工日期" prop="startDate">
                    <el-date-picker v-model="addForm.startDate" style="width: 100%" type="date" placeholder="请选择开工日期" value-format="yyyy-MM-dd" ></el-date-picker>
                </el-form-item>
                <el-form-item label-width="106px" label="预期完成日期" prop="expectedEndDate">
                    <el-date-picker v-model="addForm.expectedEndDate" style="width: 100%" type="date" placeholder="请选择预期完成日期" value-format="yyyy-MM-dd" ></el-date-picker>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveAdd">确 定</el-button>
            </span>
        </el-dialog>
    </div>

</template>

<script>
    import { querySiteById , querySiteWithLimit , addSite , delSite, updateSite } from '../../api/siteInformation';
    import {isvalidPhone} from '../common/validate';
    var validPhone=(rule, value, callback)=>{
        if (!value){
            callback(new Error('请输入手机号码'))
        }else  if (!isvalidPhone(value)){
            callback(new Error('请输入正确的11位手机号码'))
        }else {
            callback()
        }
    };
    export default {
        name: 'siteInformationTable',
        data() {
            return {
                query: {
                    siteName: '',
                    siteState: '',
                    pageIndex: 1,
                    pageSize: 10
                },
                siteInformationData: [],
                multipleSelection: [],
                editVisible: false,
                value: 50,
                pageTotal: 0,
                editForm: {},
                idx: -1,
                id: -1,
                rules: {
                    siteName: [{ required: true, message: '请输入工地名称', trigger: 'blur' }],
                    siteAddress: [{ required: true, message: '请输入工地地址', trigger: 'blur' }],
                    siteContact: [{ required: true, message: '请输入负责人', trigger: 'blur' }],
                    contactPhone: [{ required: true, trigger: 'blur', validator: validPhone }],
                    startDate: [{ required: true, message: '请选择开工日期', trigger: 'blur' }],
                    expectedEndDate: [{ required: true, message: '请选择预期完成日期', trigger: 'blur' }]
                },
                addForm: {},
                addVisible: false,
                factEndDateFun: {
                    disabledDate(time) {
                        return time.getTime() > Date.now() - 8.64e6;//如果没有后面的-8.64e6就是不可以选择今天的
                    }
                },
                addMaterialVisible: false,
            };
        },
        created() {
            this.getSiteInformaitonData();
        },
        methods: {


            // 获取数据
            getSiteInformaitonData() {
                querySiteWithLimit(this.query).then(res => {
                    if (res.code === '1001') {
                        this.siteInformationData = res.data;
                        this.pageTotal = res.total;
                    } else if (res.code === '2001') {
                        this.$message.error(res.msg);
                    } else {//3001
                        this.$message.error(res.msg);
                        localStorage.removeItem('userName');
                        localStorage.removeItem('token');
                        this.$router.push('/login');
                    }
                });
            },

            // 触发搜索按钮
            handleSearch() {
                this.$set(this.query, 'pageIndex', 1);
                this.getSiteInformaitonData();
            },
            // 情况搜索框内容
            doEmpty() {
                this.query = {
                    "siteName": '',
                    "siteState": '',
                    "pageIndex": 1,
                    "pageSize": 10
                };
            },
            // 删除操作
            handleDelete(index, row) {
                // 二次确认删除
                this.$confirm('确定要删除吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    var del = [];
                    del.push(row.id);
                    delSite(del).then(res => {
                        if (res.code === "1001") {
                            this.$message.success(res.msg);
                            this.multipleSelection = [];
                            this.getSiteInformaitonData();
                        } else if (res.code === '2001') {
                            this.$message.error(res.msg);
                        } else {//3001
                            this.$message.error(res.msg);
                            localStorage.removeItem('userName');
                            localStorage.removeItem('token');
                            this.$router.push('/login');
                        }
                    });
                }).catch(() => {});
            },

            // 多选操作
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            // 多选删除
            delAllSelection() {
                const length = this.multipleSelection.length;
                if (length === 0) {
                    this.$message.warning('请至少选择一行数据');
                } else {
                    var delArray = [];
                    for (let i = 0; i < length; i++) {
                        delArray.push(this.multipleSelection[i].id);
                    }

                    this.$confirm('确定要删除吗？', '提示', {
                        type: 'warning'
                    }).then(() => {
                        delSite(delArray).then(res => {
                            if (res.code === "1001") {
                                this.$message.success(res.msg);
                                this.multipleSelection = [];
                                this.getSiteInformaitonData();
                            } else if (res.code === '2001') {
                                this.$message.error(res.msg);
                            } else {//3001
                                this.$message.error(res.msg);
                                localStorage.removeItem('userName');
                                localStorage.removeItem('token');
                                this.$router.push('/login');
                            }
                        });
                    }).catch(() => {});
                }
            },

            handPut(index, row) {
               /* if(row.siteState !== 0) {
                    this.$message.error('该工地项目已竣工');
                    return;
                }*/
                this.$router.push({
                    path: '/siteUsage',
                    query: {
                        siteId: row.id,
                        siteState: row.siteState
                    }
                })
            },
            // 打开编辑弹出框
            handleEdit(index, row) {
                this.idx = index;
                this.editForm = row;
                this.editVisible = true;
            },
            // 保存编辑信息
            saveEdit() {
                this.$refs.editForm.validate(valid => {
                    if (valid) {
                        console.log(this.editForm.factEndDate);
                        if (this.editForm.startDate > this.editForm.expectedEndDate || this.editForm.startDate > this.editForm.factEndDate) {
                            this.$message.error('预期或实际完成日期不能比开工日期早');
                            return;
                        }
                        if (this.editForm.factEndDate != null) {
                            this.editForm.siteState = 1;
                        }
                        updateSite(this.editForm).then(res => {
                            if(res.code === '1001') {
                                this.editVisible = false;
                                this.$message.success(res.msg);
                            } else if (res.code === '2001') {
                                this.$message.error(res.msg);
                            } else {//3001
                                this.$message.error(res.msg);
                                localStorage.removeItem('userName');
                                localStorage.removeItem('token');
                                this.$router.push('/login');
                            }
                            this.getSiteInformaitonData();
                        });
                    } else {
                        this.$message.error('修改失败');
                    }
                });

            },
            handleDetailAdd() {
                this.addMaterialVisible = true;
            },
            //打开新增弹出框
            handleAdd() {
                this.addVisible = true;
                this.addForm = {};
            },
            //保存新增信息
            saveAdd() {
                this.$refs.addForm.validate(valid => {
                    if (valid) {
                        if (this.addForm.startDate > this.addForm.expectedEndDate) {
                            this.$message.error('预期完成日期不能比开工日期早');
                        } else {
                            addSite(this.addForm).then(res => {
                                if(res.code === '1001') {
                                    this.addVisible = false;
                                    this.$message.success(res.msg);
                                    this.getSiteInformaitonData();
                                } else if (res.code === '2001') {
                                    this.$message.error(res.msg);
                                } else {//3001
                                    this.$message.error(res.msg);
                                    localStorage.removeItem('userName');
                                    localStorage.removeItem('token');
                                    this.$router.push('/login');
                                }
                            });
                        }


                    } else {
                        this.$message.error('新增失败');
                    }
                });

            },

            // 分页导航
            handlePageChange(val) {
                this.$set(this.query, 'pageIndex', val);
                this.getSiteInformaitonData();
            }
        }
    };
</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 190px;
    }

    .handle-input {
        width: 190px;
        display: inline-block;
    }
    .table {
        width: 100%;
        font-size: 14px;
    }
    .green {
        color: #67C23A;
    }
    .red {
        color: #ff0000;
    }
    .mr10 {
        margin-right: 10px;
    }
    .table-td-thumb {
        display: block;
        margin: auto;
        width: 40px;
        height: 40px;
    }
    .putAddBt {
        font-size: 5px;
        padding: 9px 15px;
        margin-left: 5px;
        margin-bottom: 5px;
        float: right;
    }
    /*.el-form-item {
        width: 60px;
    }*/
</style>
