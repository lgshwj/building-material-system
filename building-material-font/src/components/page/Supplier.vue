<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-shop"></i> 经销商管理
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

                <el-input v-model="query.supplierNumber" placeholder="编号" class="handle-input mr10"></el-input>
                <el-input v-model="query.supplierName" placeholder="名称" class="handle-input mr10"></el-input>
                <el-input v-model="query.supplierAddress" placeholder="地址" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
                <el-button type="info" icon="el-icon-refresh" @click="doEmpty">清空</el-button>
            </div>
            <el-table
                    :data="supplierData"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
                    @selection-change="handleSelectionChange"
            >
                <el-table-column type="selection" width="55" align="center" prop="id"></el-table-column>
                <el-table-column prop="supplierNumber" label="经销商编号"></el-table-column>
                <el-table-column prop="supplierName" label="经销商名称"></el-table-column>
                <el-table-column prop="supplierContacts" label="联系人"></el-table-column>
                <el-table-column prop="supplierPhone" label="联系电话"></el-table-column>
                <el-table-column prop="supplierAddress" label="经销商地址"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" prop="supplierNote" label="经销商备注"></el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button
                                type="text"
                                icon="el-icon-edit"
                                @click="handleEdit(scope.$index, scope.row)"
                        >编辑</el-button>
                        <el-button
                                type="text"
                                icon="el-icon-lx-like"
                                class="green"
                                @click="handleFactoryEvluate(scope.row)"
                        >评价</el-button>
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
                <el-form-item label="名称" prop="supplierName">
                    <el-input v-model="editForm.supplierName" placeholder="请输入经销商名称"></el-input>
                </el-form-item>
                <el-form-item label="联系人" prop="supplierContacts">
                    <el-input v-model="editForm.supplierContacts" placeholder="请输入经销商联系人"></el-input>
                </el-form-item>
                <el-form-item label="联系电话" prop="supplierPhone">
                    <el-input v-model="editForm.supplierPhone" placeholder="请输入联系电话"></el-input>
                </el-form-item>
                <el-form-item label="地址" prop="supplierAddress">
                    <el-input v-model="editForm.supplierAddress" placeholder="请输入经销商地址"></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="supplierNote">
                    <el-input type="textarea" v-model="editForm.supplierNote" placeholder="请输入经销商备注"></el-input>
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
                <el-form-item label="名称" prop="supplierName">
                    <el-input v-model="addForm.supplierName" placeholder="请输入经销商名称"></el-input>
                </el-form-item>
                <el-form-item label="联系人" prop="supplierContacts">
                    <el-input v-model="addForm.supplierContacts" placeholder="请输入经销商联系人"></el-input>
                </el-form-item>
                <el-form-item label="联系电话" prop="supplierPhone">
                    <el-input v-model="addForm.supplierPhone" placeholder="请输入联系电话"></el-input>
                </el-form-item>
                <el-form-item label="地址" prop="supplierAddress">
                    <el-input v-model="addForm.supplierAddress" placeholder="请输入经销商地址"></el-input>
                </el-form-item>
                <el-form-item label="备注" prop="supplierNote">
                    <el-input type="textarea" v-model="addForm.supplierNote" placeholder="请输入经销商备注"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveAdd">确 定</el-button>
            </span>
        </el-dialog>

        <el-dialog title="评价" :visible.sync="supplierEvluateVisible" width="40%">
            <div class="handle-box">
                <el-select clearable  v-model="evaluateQuery.supplierPoint"  placeholder="满意度" class="handle-select mr10">
                    <el-option key=1 label="极差" value=1></el-option>
                    <el-option key=2 label="失望" value=2></el-option>
                    <el-option key=3 label="一般" value=3></el-option>
                    <el-option key=4 label="满意" value=4></el-option>
                    <el-option key=5 label="惊喜" value=5></el-option>
                </el-select>
                <el-input v-model="evaluateQuery.supplierComment" placeholder="关键字" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="el-icon-search" @click="handleEvaluateSearch">搜索</el-button>
            </div>


            <el-timeline>
                <el-timeline-item
                        v-for="(evaluate) in supplierEvaluateData"
                        :timestamp="evaluate.evaluteTime.substr(0, 19)"
                        placement="top">
                    <el-card>
                        <h4>{{evaluate.supplierComment}}</h4>
                        <p style="padding-top: 5px"><el-rate
                                disabled
                                v-model="evaluate.supplierPoint"
                                show-text>
                        </el-rate></p>
                        <p style="padding-top: 5px">{{evaluate.username}} 评论于 {{evaluate.evaluteTime.substr(0, 19)}}</p>
                    </el-card>
                </el-timeline-item>
            </el-timeline>

            <el-pagination
                    small
                    layout="prev, pager, next"
                    :current-page="evaluateQuery.pageIndex"
                    :page-size="evaluateQuery.pageSize"
                    @current-change="handleEvluatePageChange"
                    :total="total">
            </el-pagination>
        </el-dialog>

    </div>
</template>

<script>
    import { querySupplierWithLimit , updateSupplier , addSupplier , delSupplier } from '../../api/supplier';
    import { isvalidPhone } from '../common/validate';
    import { getSupplierEvaluate } from '../../api/evaluate';

    var validPhone=(rule, value, callback)=>{
        if (!value){
            callback(new Error('请输入电话'))
        }else  if (!isvalidPhone(value)){
            callback(new Error('请输入正确的11位手机号码'))
        }else {
            callback()
        }
    };
    export default {
        name: 'supplierTable',
        data() {
            return {
                query: {
                    supplierNumber: '',
                    supplierName: '',
                    supplierAddress: '',
                    supplierEvaluate: '',
                    pageIndex: 1,
                    pageSize: 10
                },
                supplierData: [],
                multipleSelection: [],
                editVisible: false,
                pageTotal: 0,
                editForm: {},
                idx: -1,
                id: -1,
                rules: {
                    supplierName: [{ required: true, message: '请输入经销商名称', trigger: 'blur' }],
                    supplierContacts: [{ required: true, message: '请输入经销商联系人', trigger: 'blur' }],
                    supplierPhone: [{ required: true, trigger: 'blur', validator: validPhone }],
                    supplierAddress: [{ required: true, message: '请输入经销商地址', trigger: 'blur' }],
                },
                addForm: {},
                addVisible: false,
                supplierEvluateVisible: false,
                supplierEvaluateData: [],
                total: 0,
                evaluateQuery: {
                    supplierPoint: '',
                    supplierComment: '',
                    pageIndex: 1,
                    pageSize: 1
                },
            };
        },
        created() {
            this.getSupplierWithLimit();
        },
        methods: {
            handleFactoryEvluate(row) {
                this.supplierEvluateVisible = true;
                this.$set(this.evaluateQuery, 'supplierId', row.id);
                this.getSupplierEvaluates();
            },
            getSupplierEvaluates() {
                getSupplierEvaluate(this.evaluateQuery).then(res => {
                    this.supplierEvaluateData = res.data;
                    this.total = res.total;
                })
            },
            handleEvluatePageChange(val) {
                this.$set(this.evaluateQuery, 'pageIndex', val);
                this.getSupplierEvaluates();
            },

            handleEvaluateSearch() {
                this.$set(this.evaluateQuery, 'pageIndex', 1);
                this.getSupplierEvaluates();
            },
            // 获取数据
            getSupplierWithLimit() {
                querySupplierWithLimit(this.query).then(res => {
                    if (res.code === '1001') {
                        this.supplierData = res.data;
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
                this.getSupplierWithLimit();
            },
            // 情况搜索框内容
            doEmpty() {
                this.query = {"supplierNumber": '',
                    "supplierName": '',
                    "supplierAddress": '',
                    "supplierEvaluate": '',
                    "pageIndex": 1,
                    "pageSize": 10};
            },
            // 删除操作
            handleDelete(index, row) {
                // 二次确认删除
                this.$confirm('确定要删除吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    var del = [];
                    del.push(row.id);
                    delSupplier(del).then(res => {
                        if (res.code === "1001") {
                            this.$message.success(res.msg);
                            this.multipleSelection = [];
                            this.getSupplierWithLimit();
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
                        delSupplier(delArray).then(res => {
                            if (res.code === "1001") {
                                this.$message.success(res.msg);
                                this.multipleSelection = [];
                                this.getSupplierWithLimit();
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
                        updateSupplier(this.editForm).then(res => {
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
                            this.getSupplierWithLimit();
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
                        addSupplier(this.addForm).then(res => {
                            if(res.code === '1001') {
                                this.addVisible = false;
                                this.$message.success(res.msg);
                                this.getSupplierWithLimit();
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
                this.getSupplierWithLimit();
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
