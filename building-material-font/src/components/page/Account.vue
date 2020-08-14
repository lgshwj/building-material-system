<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-group"></i> 账户管理
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

                <el-input v-model="query.username" placeholder="账户" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            </div>
            <el-table
                    :data="accountData"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
                    @selection-change="handleSelectionChange"
            >
                <el-table-column type="selection" width="55" align="center" prop="id"></el-table-column>
                <el-table-column prop="username" label="账户"></el-table-column>
                <el-table-column prop="nickname" label="昵称"></el-table-column>
                <el-table-column prop="createTime" label="创建时间" :formatter="dateFormat"></el-table-column>
                <el-table-column prop="updateTime" label="更新时间" :formatter="dateFormat"></el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button
                                type="text"
                                icon="el-icon-s-data"
                                class="green"
                                @click="handleRight(scope.$index, scope.row)"
                        >授权</el-button>
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
                <el-form-item label="账户" prop="username">
                    <el-input v-model="editForm.username" placeholder="请输入账户"></el-input>
                </el-form-item>
                <el-form-item label="昵称" prop="nickname">
                    <el-input v-model="editForm.nickname" placeholder="请输入昵称"></el-input>
                </el-form-item>
                <el-form-item label="新的密码">
                    <el-input type="password" v-model="editForm.password" placeholder="请输入新密码"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="cancelEdit">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>

        <!--新增弹出框-->
        <el-dialog title="新增" :visible.sync="addVisible" width="30%">
            <el-form ref="addForm" :rules="rules" :model="addForm" label-width="80px">
                <el-form-item label="账户" prop="username">
                    <el-input v-model="addForm.username" placeholder="请输入账户"></el-input>
                </el-form-item>
            </el-form>
            <el-form ref="addForm" :rules="rules" :model="addForm" label-width="80px">
                <el-form-item label="昵称" prop="nickname">
                    <el-input v-model="addForm.nickname" placeholder="请输入昵称"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveAdd">确 定</el-button>
            </span>
        </el-dialog>


        <el-dialog title="授权" :visible.sync="rightVisible" width="40%">
            <div class="container">
                <div class="form-box">
                    <el-form label-width="80px">
                        <el-form-item label="账户权限"></el-form-item>
                        <el-form-item label="" >
                            <el-checkbox-group @change="checkTheRight" v-model="rights" >
                                <el-checkbox v-for="nav in allNavigation" :label="nav.id" :key="nav.id">{{nav.navigationName}}</el-checkbox>
                            </el-checkbox-group>
                        </el-form-item>
                    </el-form>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="cancelRightManage">取 消</el-button>
                <el-button type="primary" @click="submitRights">提 交</el-button>
            </span>
        </el-dialog>

    </div>
</template>

<script>
    import { queryByLimitAccountName, delAccount, addAccount , updateAccount , queryRightByUserId , updateAccountRight } from '../../api/account';
    import moment from 'moment';
    export default {
        name: 'accountTable',
        data() {
            return {
                query: {
                    username: '',
                    pageIndex: 1,
                    pageSize: 10
                },
                accountData: [],
                multipleSelection: [],
                editVisible: false,
                pageTotal: 0,
                editForm: {},
                idx: -1,
                id: -1,
                rules: {
                    username: [{ required: true, message: '请输入账户', trigger: 'blur' }],
                    nickname: [{required: true, message: '请输入昵称', trigger: 'blue'}],
                },
                addForm: {},
                addVisible: false,
                allNavigation:[],
                rightVisible: false,
                checkedValue:[],
                rights: [],
                userIdRight: 0,
            };
        },
        created() {
            this.getByLimitAccount();
        },
        methods: {
            // 获取数据
            getByLimitAccount() {
                queryByLimitAccountName(this.query).then(res => {
                    if (res.code === '1001') {
                        this.accountData = res.data;
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
                this.getByLimitAccount();
            },

            // 删除操作
            handleDelete(index, row) {
                // 二次确认删除
                this.$confirm('确定要删除吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    var del = [];
                    del.push(row.id);
                    delAccount(del).then(res => {
                        if (res.code === "1001") {
                            this.$message.success(res.msg);
                            this.multipleSelection = [];
                            this.getByLimitAccount();
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
                        delAccount(delArray).then(res => {
                            if (res.code === "1001") {
                                this.$message.success(res.msg);
                                this.multipleSelection = [];
                                this.getByLimitAccount();
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

            // 授权操作
            handleRight(index, row) {
                let query = {"userId": row.id};
                this.userIdRight = row.id;
                queryRightByUserId(query).then(res => {
                    let temp = res.data.right;
                    this.rights = [];//清空
                    for (let i=0 ; i<temp.length ; i++) {
                        this.rights.push(temp[i].navigationId);
                    }
                    this.allNavigation = res.data.navigation;
                });
                this.rightVisible = true;
            },

            checkTheRight(value) {
                this.checkedValue = value;
            },

            submitRights() {
                updateAccountRight(this.checkedValue , this.userIdRight).then(res =>{
                    if (res.code === "1001") {
                        this.$message.success(res.msg);
                        this.rightVisible = false;
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
            cancelRightManage() {
                this.rightVisible = false;
            },
            cancelEdit() {
                this.editForm = {};
                this.editVisible = false;
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
                        updateAccount(this.editForm).then(res => {
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
                            this.getByLimitAccount();
                        });
                    } else {
                        this.$message.error('修改失败');
                    }
                });

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
                        addAccount(this.addForm).then(res => {
                            if(res.code === '1001') {
                                this.addVisible = false;
                                this.$message.success(res.msg);
                                this.getByLimitAccount();
                            } else if (res.code === '2001') {
                                this.$message.error(res.msg);
                            } else {//3001
                                this.$message.error(res.msg);
                                localStorage.removeItem('userName');
                                localStorage.removeItem('token');
                                this.$router.push('/login');
                            }
                        });
                    } else {
                        this.$message.error('新增失败');
                    }
                });

            },

            // 分页导航
            handlePageChange(val) {
                this.$set(this.query, 'pageIndex', val);
                this.getByLimitAccount();
            },

            // 时间格式胡
            dateFormat:function(row, column) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm:ss");
            }
        }
    };
</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 120px;
    }

    .handle-input {
        width: 190px;
        display: inline-block;
    }
    .table {
        width: 100%;
        font-size: 14px;
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
    .green {
        color: #67C23A;
    }
</style>
