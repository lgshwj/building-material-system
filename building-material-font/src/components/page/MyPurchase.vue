<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cart"></i> 我的采购
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
                <!--<el-button
                        type="success"
                        icon="el-icon-plus"
                        class="handle-del mr10"
                        @click="addHandle"
                >采购申请</el-button>-->


                <el-input v-model="query.purchaseNumber" placeholder="采购编号" class="handle-input mr10"></el-input>
                <el-select v-model="query.state" placeholder="状态" class="handle-select mr10">
                    <el-option key="0" label="未审核" value="0" >未审核</el-option>
                    <el-option key="1" label="已驳回" value="1" >已驳回</el-option>
                    <el-option key="2" label="已批准" value="2" >已批准</el-option>
                    <el-option key="3" label="已采购" value="3" >已采购</el-option>
                    <el-option key="4" label="已入库" value="4" >已入库</el-option>
                </el-select>
                <el-date-picker v-model="query.startTime" class="handle-input mr10" type="datetime" placeholder="选择申请开始时间" value-format="yyyy-MM-dd HH:mm:ss" ></el-date-picker>
                <el-date-picker v-model="query.endTime" class="handle-input mr10" type="datetime" placeholder="选择申请结束时间" value-format="yyyy-MM-dd HH:mm:ss" ></el-date-picker>
                <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
                <el-button type="info" icon="el-icon-refresh" @click="doEmpty">清空</el-button>
            </div>
            <el-table
                    :data="purchasePlanData"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
                    @selection-change="handleSelectionChange"
            >
                <el-table-column type="selection" width="55" align="center" prop="id"></el-table-column>
                <el-table-column prop="purchaseNumber" label="采购编号"></el-table-column>
                <el-table-column prop="applyUsername" label="申请人"></el-table-column>
                <el-table-column width="140" prop="applyDate" :formatter="dateFormat" label="申请时间"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" prop="applyNote" label="申请备注"></el-table-column>
                <el-table-column
                        prop="state"
                        label="状态"
                        width="100"
                        :filters="[{ text: '未审核', value: 0 }, { text: '已驳回', value: 1 }, {text: '已批准', value: 2}, {text: '已采购', value: 3}, {text: '已入库', value: 4}]"
                        :filter-method="filterTagState"
                        filter-placement="bottom-end">
                    <template slot-scope="scope">
                        <el-tag
                                :type="scope.row.state === 0 ? 'danger' : scope.row.state === 1 ? 'info' : scope.row.state === 2 ? 'warning' : scope.row.state === 3 ? 'primary' : 'success'"
                                close-transition>{{scope.row.state === 0 ? "未审核" : scope.row.state === 1 ? "已驳回" : scope.row.state === 2 ? "已批准": scope.row.state === 3 ? '已采购' : '已入库'}}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="approvalUsername" label="审核人"></el-table-column>
                <el-table-column width="140" prop="approvalDate" :formatter="dateFormat" label="审核时间"></el-table-column>
                <el-table-column :show-overflow-tooltip="true" prop="approvalNote" label="审核备注"></el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button
                                type="text"
                                icon="el-icon-chat-line-square"
                                @click="handleEdit(scope.$index, scope.row)"
                        >详情</el-button>
                        <el-button
                                type="text"
                                icon="el-icon-lx-like"
                                class="green"
                                @click="handleEvaluate(scope.row)"
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

        <el-dialog title="采购评价" :visible.sync="evaluateVisible" width="30%">
            <el-form ref="evaluateForm" :rules="rules"  :model="evaluateForm">
                <el-form-item label="经销商打分" prop="supplierPoint" label-width="100px">
                    <el-rate class="rateInput" v-model="evaluateForm.supplierPoint" show-text></el-rate>
                </el-form-item>
                <el-form-item label="经销商评论" prop="supplierComment" label-width="100px">
                    <el-input type="textarea" v-model="evaluateForm.supplierComment" placeholder="我想说..."></el-input>
                </el-form-item>
                <el-form-item label="生产商打分" prop="factoryPoint" label-width="100px">
                    <el-rate class="rateInput" v-model="evaluateForm.factoryPoint" show-text></el-rate>
                </el-form-item>
                <el-form-item label="生产商评论" prop="factoryComment" label-width="100px">
                    <el-input type="textarea" v-model="evaluateForm.factoryComment" placeholder="我想说..."></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="evaluateVisible = false">取 消</el-button>
                <el-button :disabled="btSaveEvaluate" type="primary" @click="saveEvaluate">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 编辑弹出框 -->
        <el-dialog title="采购详情" :visible.sync="editVisible" width="40%">
            <el-input class="detailPurchase" :disabled="true" v-model="purchaseList.materialNumber">
                <template slot="prepend">材料编号</template>
            </el-input>
            <el-input class="detailPurchase" :disabled="true" v-model="purchaseList.materialName">
                <template slot="prepend">材料名称</template>
            </el-input>
            <el-input class="detailPurchase" :disabled="true" v-model="purchaseList.norm">
                <template slot="prepend">材料规格</template>
            </el-input>
            <el-input class="detailPurchase" :disabled="true" v-model="purchaseList.materialQuality">
                <template slot="prepend">材料材质</template>
            </el-input>
            <el-input class="detailPurchase" :disabled="true" v-model="purchaseList.purchaseQuantity+' '+ purchaseList.materialUnit">
                <template slot="prepend">采购数量</template>
            </el-input>
            <el-input class="detailPurchase" :disabled="true" v-model="purchaseList.supplierName">
                <template slot="prepend">经销商名</template>
            </el-input>
            <el-input class="detailPurchase" :disabled="true" v-model="purchaseList.factoryName">
                <template slot="prepend">厂家名称</template>
            </el-input>
            <el-input class="detailPurchase" :disabled="true" v-model="'￥'+purchaseList.materialPrice">
                <template slot="prepend">销售单价</template>
            </el-input>
            <el-input class="detailPurchase" :disabled="true" v-model="'￥'+purchaseList.dealMoney">
                <template slot="prepend">采购总价</template>
            </el-input>
        </el-dialog>

    </div>
</template>

<script>
    import { queryPurchasePlanWithLimit , queryPurchaseList , approvalPurchasePlan , delPurchasePlan } from '../../api/purchasePlan';
    import moment from 'moment';
    import { updateSupplier } from '../../api/supplier';
    import { addEvaluate , queryEvaluateByPurchasePlanId } from '../../api/evaluate'
    export default {
        name: 'purchasePlanTable',
        data() {
            return {
                query: {
                    purchaseNumber: '',
                    state: '',
                    startTime: '',
                    endTime: '',
                    pageIndex: 1,
                    pageSize: 10
                },
                addForm: {},
                count: 0,
                multipleSelection: [],
                purchasePlanData: [],
                addVisible: false,
                editVisible: false,
                pageTotal: 0,
                editForm: {},
                idx: -1,
                id: -1,
                purchaseList: [],
                purchasePlanId: 0,
                disablebt: true,
                approvalNote: '',
                evaluateVisible: false,
                evaluateForm: {
                    id: 0,
                    supplierId: 0,
                    supplierPoint: 0,
                    supplierComment: '',
                    factoryId: 0,
                    factoryPoint: 0,
                    factoryComment: ''
                },
                rules: {
                    supplierPoint: [{ required: true, message: '请给经销商配货服务打分', trigger: 'blur' }],
                    supplierComment: [{ required: true, message: '请输入对经销商的评论', trigger: 'blur' }],
                    factoryPoint: [{ required: true, message: '请给生产商材料质量打分', trigger: 'blur' }],
                    factoryComment: [{ required: true, message: '请输入对生产商的评论', trigger: 'blur' }],
                },
                btSaveEvaluate: false,
            };
        },
        created() {
            this.getPurchasePlan();
        },
        methods: {
            // 获取数据
            getPurchasePlan() {
                this.$set(this.query,'token',localStorage.getItem("token"));
                queryPurchasePlanWithLimit(this.query).then(res => {
                    console.log(res);
                    if (res.code === '1001') {
                        this.purchasePlanData = res.data;
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
                this.getPurchasePlan();
            },
            // 情况搜索框内容
            doEmpty() {
                this.query = {
                    "purchaseNumber": '',
                    "state": '',
                    "startTime": '',
                    "endTime": '',
                    "pageIndex": 1,
                    "pageSize": 10
                };
            },


            filterTagState(value, row) {
                return row.state === value;
            },

            handleEvaluate(row) {
              if (row.state < 3) {
                  this.$message.error('该采购计划状态不为已采购或已入库，暂不能评价');
              } else {
                  this.evaluateVisible = true;
                  console.log(row.supplierId);
                  let data = {'purchasePlanId': row.id};
                  queryEvaluateByPurchasePlanId(data).then(res => {
                      if (res.code === '1001') {
                          this.evaluateForm = res.data;
                          this.btSaveEvaluate = true;
                      } else if (res.code === '2001') { // 没有被评论
                          this.removeEvaluateForm();
                          this.evaluateForm.purchasePlanId = row.id;
                          this.evaluateForm.supplierId = row.supplierId;
                          this.evaluateForm.factoryId = row.factoryId;
                          this.btSaveEvaluate = false;
                      }
                  });

              }
            },
            saveEvaluate() {

                this.$refs.evaluateForm.validate(valid => {
                    if (valid) {
                        if (this.evaluateForm.supplierPoint === 0 || this.evaluateForm.factoryPoint === 0) {
                            this.$message.error('请给供应商或生产商打分');
                        } else {
                            //console.log(this.evaluateForm)
                            this.$set(this.evaluateForm, 'token', localStorage.getItem('token'));
                            addEvaluate(this.evaluateForm).then(res => {
                                if (res.code === '1001') {
                                    this.evaluateVisible = false;
                                    this.$message.success(res.msg);
                                }
                            })
                        }

                        /*updateSupplier(this.editForm).then(res => {
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
                        });*/
                    } else {
                        this.$message.error('评论失败');
                    }
                });
            },
            removeEvaluateForm() {
                this.evaluateForm = {
                    'id': 0,
                    'supplierId': 0,
                    'supplierPoint': 0,
                    'supplierComment': '',
                    'factoryId': 0,
                    'factoryPoint': 0,
                    'factoryComment': ''
                }
            },
            // 打开编辑弹出框
            handleEdit(index, row) {
                this.idx = index;
                this.editVisible = true;
                this.purchasePlanId = row.id;
                if (row.state === 0) {
                    this.disablebt = false;
                } else {
                    this.disablebt = true;
                }
                queryPurchaseList(row.id).then(res => {
                    if (res.code === '1001') {
                        this.purchaseList = res.data;
                    }
                });
            },
            passPurchasePlan(state) {
                this.$prompt('请输入审核备注（批准理由或驳回理由）', '审核备注', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    showClose: false
                }).then(({ value }) => {
                    var data = {"token": localStorage.getItem("token"), "purchasePlanId": this.purchasePlanId, "state":state, "approvalNote": value};
                    approvalPurchasePlan(data).then(res => {
                        if (res.code === '1001') {
                            this.$message.success(res.msg);
                            this.editVisible = false;
                        } else if (res.code === '2001') {
                            this.$message.error(res.msg);
                        } else {//3001
                            this.$message.error(res.msg);
                            localStorage.removeItem('userName');
                            localStorage.removeItem('token');
                            this.$router.push('/login');
                        }
                        this.getPurchasePlan();
                    });

                }).catch(() => {
                });
            },

            // 分页导航
            handlePageChange(val) {
                this.$set(this.query, 'pageIndex', val);
                this.getPurchasePlan();
            },

            // 时间格式化
            dateFormat:function(row, column) {
                var date = row[column.property];
                if (date == undefined) {
                    return "";
                }
                return moment(date).format("YYYY-MM-DD HH:mm");
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
                        delPurchasePlan(delArray).then(res => {
                            if (res.code === "1001") {
                                this.$message.success(res.msg);
                                this.multipleSelection = [];
                                this.getPurchasePlan();
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
            // 删除操作
            handleDelete(index, row) {
                // 二次确认删除
                this.$confirm('确定要删除吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    var del = [];
                    del.push(row.id);
                    delPurchasePlan(del).then(res => {
                        if (res.code === "1001") {
                            this.$message.success(res.msg);
                            this.multipleSelection = [];
                            this.getPurchasePlan();
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
        }
    };
</script>

<style scoped>
    .red {
        color: #ff0000;
    }
    .green {
        color: #67C23A;
    }
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

    .mr10 {
        margin-right: 10px;
    }
    .rateInput {
        padding-top: 5px;
    }

</style>
