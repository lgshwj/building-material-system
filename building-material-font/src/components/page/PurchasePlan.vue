<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cart"></i> 采购审核
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
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
                    header-cell-class-name="table-header"
            >
                <el-table-column type="index" label="#" align="center"></el-table-column>
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
                                class="green"
                                icon="el-icon-check"
                                @click="handleEdit(scope.$index, scope.row)"
                        >审核</el-button>
                        <el-button
                                type="text"
                                icon="el-icon-edit"
                                @click="handldUpdate(scope.row)"
                        >更新</el-button>
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
            <span slot="footer" class="dialog-footer">
                <el-button :disabled="disablebt" type="danger" @click="passPurchasePlan(1)">驳 回</el-button>
                <el-button :disabled="disablebt" type="primary" @click="passPurchasePlan(2)">通 过</el-button>
            </span>
        </el-dialog>

    </div>
</template>

<script>
    import { queryPurchasePlanWithLimit , queryPurchaseList , approvalPurchasePlan , updatePurchasePlanToHavaBuy } from '../../api/purchasePlan';
    import moment from 'moment';
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
                purchasePlanData: [],
                editVisible: false,
                pageTotal: 0,
                editForm: {},
                idx: -1,
                id: -1,
                purchaseList: {},
                purchasePlanId: 0,
                disablebt: true,
                approvalNote: '',
            };
        },
        created() {
            this.getPurchasePlan();
        },
        methods: {
            // 获取数据
            getPurchasePlan() {
                queryPurchasePlanWithLimit(this.query).then(res => {
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

            //更新
            handldUpdate(row) {
                console.log(row.id);
                if (row.state === 2) {// 仅有已批准的采购计划状态才可更新为已采购
                    this.$confirm('更新采购计划状态为已采购, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        updatePurchasePlanToHavaBuy(row).then(res => {
                            if (res.code === '1001') {
                                this.$message.success(res.msg);
                            } else {
                                this.$message.error(res.msg);
                            }
                            this.getPurchasePlan();
                        });

                    }).catch(() => {
                        this.$message.error('已取消更新采购计划状态');
                    });
                } else {
                    this.$message.error('仅有状态为已批准的采购计划才可更新');
                }
            },

            // 打开编辑弹出框
            handleEdit(index, row) {
                this.idx = index;

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
                this.editVisible = true;
            },
            passPurchasePlan(state) {
                this.$prompt('请输入审核备注（批准理由或驳回理由）', '审核备注', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    showClose: false
                }).then(({ value }) => {
                    var data = {
                        "token": localStorage.getItem("token"),
                        "purchasePlanId": this.purchasePlanId,
                        "state":state,
                        "approvalNote": value,
                        "dealMoney": this.purchaseList.dealMoney
                    };
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

    .mr10 {
        margin-right: 10px;
    }
    .detailPurchase {
        margin-bottom: 5px;
    }
    .green {
        color: #67C23A;
    }

</style>
