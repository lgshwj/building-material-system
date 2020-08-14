<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="icon icondetail icon-lg-kucun"></i> 库存管理
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
                >材料新增</el-button>
                <el-button
                        type="success"
                        icon="el-icon-s-order"
                        class="handle-del mr10"
                        @click="handlewarehouse"
                >材料入库</el-button>
            </div>
            <div class="handle-box">
                <el-input v-model="query.materialNumber" placeholder="材料编号" class="handle-input mr10"></el-input>
                <el-input v-model="query.materialName" placeholder="材料名称" class="handle-input mr10"></el-input>
                <el-select v-model="query.materialCategoryId" placeholder="材料类别" class="handle-select mr10">
                    <el-option
                            v-for="item in materialCategoryData"
                            :key="item.id"
                            :label="item.materialCategoryName"
                            :value="item.id"
                    ></el-option>
                </el-select>
                <el-select v-model="query.materialQuantity" placeholder="库存情况" class="handle-select mr10">
                    <el-option key="1" label="库存不足" value="1"></el-option>
                    <el-option key="2" label="库存适量" value="2"></el-option>
                    <el-option key="3" label="库存足量" value="3"></el-option>
                </el-select>
                <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
                <el-button type="info" icon="el-icon-refresh" @click="doEmpty">清空</el-button>
            </div>

            <div class="handle-box">
            </div>


            <el-table
                    :data="materialStockData"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
                    @selection-change="handleSelectionChange"
            >
                <el-table-column type="selection" width="55" align="center" prop="id"></el-table-column>
                <el-table-column prop="materialNumber" label="材料编号"></el-table-column>
                <el-table-column prop="materialName" label="材料名称"></el-table-column>
                <el-table-column prop="norm" label="规格"></el-table-column>
                <el-table-column prop="materialQuality" label="材质"></el-table-column>
                <el-table-column prop="materialCategoryName" label="材料类别"></el-table-column>
                <el-table-column prop="materialLow" label="库存下限"></el-table-column>
                <el-table-column prop="materialHigh" label="库存上限"></el-table-column>
                <el-table-column label="库存情况">
                    <template scope="scope">
                        <el-tag type="danger" v-if="scope.row.materialQuantity < scope.row.materialLow">库存不足</el-tag>
                        <el-tag v-else-if="scope.row.materialQuantity > scope.row.materialHigh">库存足量</el-tag>
                        <el-tag type="success" v-else>库存适量</el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="materialQuantity" label="材料数量"></el-table-column>
                <el-table-column prop="materialUnit" label="材料单位"></el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button
                                type="text"
                                icon="el-icon-goods"
                                class="green"
                                @click="handleBuy(scope.$index, scope.row)"
                        >采购</el-button>
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
                <el-form-item label="材料名称" prop="materialName">
                    <el-input v-model="editForm.materialName" placeholder="请输入材料名称"></el-input>
                </el-form-item>
                <el-form-item label="材料规格" prop="norm">
                    <el-input v-model="editForm.norm" placeholder="请输入材料规格"></el-input>
                </el-form-item>
                <el-form-item label="材料材质" prop="materialQuality">
                    <el-input v-model="editForm.materialQuality" placeholder="请输入材料材质"></el-input>
                </el-form-item>
                <el-form-item label="材料类别" prop="materialCategoryId">
                    <el-select v-model="editForm.materialCategoryId" placeholder="请选择材料类别" class="handle-select1 mr10">
                        <el-option
                                v-for="item in materialCategoryData"
                                :key="item.id"
                                :label="item.materialCategoryName"
                                :value="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="库存下限" prop="materialLow">
                    <el-input type="number" v-model="editForm.materialLow" placeholder="请输入库存下限"></el-input>
                </el-form-item>
                <el-form-item label="库存上限" prop="materialHigh">
                    <el-input type="number" v-model="editForm.materialHigh" placeholder="请输入库存上限"></el-input>
                </el-form-item>
                <el-form-item label="材料单位" prop="materialUnit">
                    <el-input v-model="editForm.materialUnit" placeholder="请输入材料单位"></el-input>
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
                <el-form-item label="材料名称" prop="materialName">
                    <el-input v-model="addForm.materialName" placeholder="请输入材料名称"></el-input>
                </el-form-item>
                <el-form-item label="材料规格" prop="norm">
                    <el-input v-model="addForm.norm" placeholder="请输入材料规格"></el-input>
                </el-form-item>
                <el-form-item label="材料材质" prop="materialQuality">
                    <el-input v-model="addForm.materialQuality" placeholder="请输入材料材质"></el-input>
                </el-form-item>
                <el-form-item label="材料类别" prop="materialCategoryId">
                    <el-select v-model="addForm.materialCategoryId" placeholder="请选择材料类别" class="handle-select1 mr10">
                        <el-option
                                v-for="item in materialCategoryData"
                                :key="item.id"
                                :label="item.materialCategoryName"
                                :value="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="库存下线" prop="materialLow">
                    <el-input type="number" v-model="addForm.materialLow" placeholder="请输入库存下限"></el-input>
                </el-form-item>
                <el-form-item label="库存上限" prop="materialHigh">
                    <el-input type="number" v-model="addForm.materialHigh" placeholder="请输入库存上限"></el-input>
                </el-form-item>
                <el-form-item label="材料单位" prop="materialUnit">
                    <el-input v-model="addForm.materialUnit" placeholder="请输入材料单位"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveAdd">确 定</el-button>
            </span>
        </el-dialog>

        <el-dialog title="采购申请" :visible.sync="buyVisible" width="30%">
            <el-form ref="buyForm" :rules="rules" :model="buyForm" label-width="80px">
                <el-form-item label="材料编号" prop="materialNumber">
                    <el-input :disabled="true" v-model="buyForm.materialNumber" placeholder="请输入材料编号"></el-input>
                </el-form-item>
                <el-form-item label="材料编号" prop="materialName">
                    <el-input :disabled="true" v-model="buyForm.materialName" placeholder="请输入材料名称"></el-input>
                </el-form-item>
                <el-form-item label="材料规格" prop="norm">
                    <el-input :disabled="true" v-model="buyForm.norm" placeholder="请输入材料规格"></el-input>
                </el-form-item>
                <el-form-item label="材料材质" prop="materialQuality">
                    <el-input :disabled="true" v-model="buyForm.materialQuality" placeholder="请输入材料材质"></el-input>
                </el-form-item>
                <el-form-item label="材料单位" prop="materialUnit">
                    <el-input :disabled="true" v-model="buyForm.materialUnit" placeholder="请输入材料单位"></el-input>
                </el-form-item>
                <el-form-item label="采购数量" prop="purchaseQuantity">
                    <el-input type="number" v-model="buyForm.purchaseQuantity" placeholder="请输入采购数量"></el-input>
                </el-form-item>
                <el-form-item label="经销商名" prop="supplierId">
                    <el-select v-model="buyForm.supplierId" placeholder="请选择供应商" class="handle-select1 mr10">
                        <el-option
                                v-for="item in supplierData"
                                :key="item.id"
                                :label="item.supplierName"
                                :value="item.id"
                        ></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="申请备注" prop="applyNote">
                    <el-input type="textarea"  v-model="buyForm.applyNote" placeholder="请输入申请备注信息"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="buyVisible = false">取 消</el-button>
                <el-button type="primary" @click="buyApply">确 定</el-button>
            </span>
        </el-dialog>

        <el-dialog title="材料入库" :visible.sync="warehouseVisible" width="30%">
            <el-form ref="putInpurchasePlan" :rules="rules" :model="putInpurchasePlan" label-width="80px">
                <el-form-item label="采购计划" prop="purchasePlanId">
                    <el-select v-model="putInpurchasePlan.purchasePlanId" placeholder="请选择采购计划" class="handle-select1 mr10">
                        <el-option
                                v-for="item in approvalData"
                                :key="item.id"
                                :label="item.materialNumber+'：'+item.materialName+'|'+item.norm+'|'+item.materialQuality+'|'+item.materialQuantity+item.materialUnit"
                                :value="item.id"
                        >{{item.materialNumber+'：'+item.materialName+'|'+item.norm+'|'+item.materialQuality+'|'+item.materialQuantity+item.materialUnit}}</el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="warehouseVisible = false">取 消</el-button>
                <el-button type="primary" @click="putInStock">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import { queryMaterialStock , updateMaterial , addMaterial , delMaterial } from '../../api/materialStock';
    import { queryFactoryAll } from '../../api/factory';
    import { queryCategroyAll} from '../../api/materialCategory';
    import { isvalidPurchaseQuantity } from '../common/validate';
    import { applyPurchasePlan } from '../../api/purchasePlan';
    import { searchSupplierByMaterialId } from '../../api/quotePrice';
    import { getApprovalPurchase , putInStock } from '../../api/purchasePlan';

    var validPurchaseQuantity = (rule, value, callback)=>{
        if (!value){
            callback(new Error('请输入采购数量'))
        }else  if (!isvalidPurchaseQuantity(value)){
            callback(new Error('采购数量必须大于0'))
        }else {
            callback()
        }
    };
    export default {
        name: 'materialStockTable',
        data() {
            return {
                query: {
                    materialNumber: '',
                    materialName: '',
                    materialQuantity: '',
                    materialCategoryId: '',
                    supplierId: '',
                    factoryId: '',
                    pageIndex: 1,
                    pageSize: 10
                },
                materialStockData: [],
                multipleSelection: [],
                editVisible: false,
                warehouseVisible: false,
                pageTotal: 0,
                editForm: {},
                idx: -1,
                id: -1,
                rules: {
                    materialName: [
                        { required: true, message: '请输入材料名称', trigger: 'blur'},
                        { type: 'string', min: 2, message: '材料名称长度不允许小于2位', trigger: 'blur' }
                    ],
                    norm: [{ required: true, message: '请输入材料规格', trigger: 'blur' }],
                    materialQuality: [{ required: true, message: '请输入材料材质', trigger: 'blur' }],
                    materialCategoryId: [{ required: true, message: '请选择材料类别', trigger: 'blur' }],
                    materialLow: [{ required: true, message: '请输入库存下限', trigger: 'blur' }],
                    materialHigh: [{ required: true, message: '请输入库存上限', trigger: 'blur' }],
                    materialUnit: [{ required: true, message: '请输入材料单位', trigger: 'blur' }],
                    applyNote: [{ required: true, message: '请输入申请备注信息', trigger: 'blur' }],
                    purchaseQuantity: [{ required: true, trigger: 'blur', validator: validPurchaseQuantity }],
                    supplierId: [{ required: true, message: '请输入选择经销商', trigger: 'blur' }],
                    purchasePlanId: [{ required: true, message: '请选择采购计划', trigger: 'blur' }],
                },
                addForm: {},
                buyForm: {},
                addVisible: false,
                buyVisible: false,
                supplierData: [],
                factoryData: [],
                materialCategoryData: [],
                approvalData: [],
                putInpurchasePlan: {},
            };
        },
        created() {
            this.queryFactory();
            this.queryMaterialCategroy();
            this.getByLimitMaterialStock();
        },
        methods: {
            // 获取数据
            getByLimitMaterialStock() {
                queryMaterialStock(this.query).then(res => {
                    if (res.code === '1001') {
                        this.materialStockData = res.data;
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
                this.getByLimitMaterialStock();
            },

            // 删除操作
            handleDelete(index, row) {
                // 二次确认删除
                this.$confirm('确定要删除吗？', '提示', {
                    type: 'warning'
                }).then(() => {
                    var del = [];
                    del.push(row.id);
                    delMaterial(del).then(res => {
                        if (res.code === "1001") {
                            this.$message.success(res.msg);
                            this.multipleSelection = [];
                            this.getByLimitMaterialStock();
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
                        delMaterial(delArray).then(res => {
                            if (res.code === "1001") {
                                this.$message.success(res.msg);
                                this.multipleSelection = [];
                                this.getByLimitMaterialStock();
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

            // 打开材料入库弹出框
            handlewarehouse() {
                this.putInpurchasePlan = {};
                this.queryApprovalPurchasePlan();
                this.warehouseVisible = true;
            },
            // 打开采购弹出框
            handleBuy(index, row) {
                this.idx = index;
                this.buyForm = row;
                let temp = {"materialId": row.id};
                this.querySupplier(temp);
            },
            putInStock() {
                this.$refs.putInpurchasePlan.validate(valid => {
                    if (valid) {
                        putInStock(this.putInpurchasePlan).then(res => {
                            if(res.code === '1001') {
                                this.warehouseVisible = false;
                                this.$message.success(res.msg);
                            } else if (res.code === '2001') {
                                this.$message.error(res.msg);
                            } else {//3001
                                this.$message.error(res.msg);
                                localStorage.removeItem('userName');
                                localStorage.removeItem('token');
                                this.$router.push('/login');
                            }
                            this.getByLimitMaterialStock();
                        })
                    } else {
                        this.$message.error('材料入库失败');
                    }
                })
            },
            buyApply() {
                this.$refs.buyForm.validate(valid => {
                    if (valid) {
                        let tempForm = this.buyForm;
                        let temp = {
                            "supplierId": tempForm.supplierId,
                            "materialId": tempForm.id,
                            "applyNote": tempForm.applyNote,
                            "purchaseQuantity": tempForm.purchaseQuantity,
                            "token": localStorage.getItem("token")
                        };

                        applyPurchasePlan(temp).then(res => {
                            if(res.code === '1001') {
                                this.buyVisible = false;
                                this.$message.success(res.msg);
                            } else if (res.code === '2001') {
                                this.$message.error(res.msg);
                            } else {//3001
                                this.$message.error(res.msg);
                                localStorage.removeItem('userName');
                                localStorage.removeItem('token');
                                this.$router.push('/login');
                            }
                            this.getByLimitMaterialStock();
                        })
                    } else {
                        this.$message.error('申请失败');
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
                        this.$delete(this.editForm,'child');
                        updateMaterial(this.editForm).then(res => {
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
                            this.getByLimitMaterialStock();
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
                        addMaterial(this.addForm).then(res => {
                            if(res.code === '1001') {
                                this.addVisible = false;
                                this.$message.success(res.msg);
                                this.getByLimitMaterialStock();
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
                this.getByLimitMaterialStock();
            },

            // 清空说说看
            doEmpty() {
                this.query = {
                    "materialNumber": '',
                    "materialName": '',
                    "materialQuantity": '',
                    "materialCategoryId": '',
                    "supplierId": '',
                    "factoryId": '',
                    "pageIndex": 1,
                    "pageSize": 10
                };
            },
            // 获取供应商数据
            querySupplier(data) {
                 searchSupplierByMaterialId(data).then(res => {
                    if (res.code === '1001') {
                        this.supplierData = res.data;
                        if (this.supplierData.length === 0) {
                            this.$message.error("该种材料未有经销商报价，暂不能采购");
                        } else {
                            this.buyVisible = true;
                        }
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
            // 获取厂家数据
            queryFactory() {
                queryFactoryAll().then(res => {
                    if (res.code === '1001') {
                        this.factoryData = res.data;
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
            queryApprovalPurchasePlan() {
                getApprovalPurchase().then(res => {
                    if (res.code === '1001') {
                        this.approvalData = res.data;
                        console.log(res.data)
                    } else if (res.code === '2001') {
                        this.$message.error(res.msg);
                    } else {//3001
                        this.$message.error(res.msg);
                        localStorage.removeItem('userName');
                        localStorage.removeItem('token');
                        this.$router.push('/login');
                    }
                })
            },
            queryMaterialCategroy() {
                queryCategroyAll().then(res => {
                    if (res.code === '1001') {
                        this.materialCategoryData = res.data;
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
        }
    };
</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 200px;
    }
    .handle-select1 {
        width: 100%;
    }
    .handle-input {
        width: 200px;
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

</style>
