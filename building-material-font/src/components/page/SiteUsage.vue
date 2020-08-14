<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-news"></i> 用料详情
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">

                <el-button
                        type="success"
                        icon="el-icon-plus"
                        class="handle-del mr10"
                        @click="handleAdd"
                >投入材料</el-button>

                <el-input v-model="query.materialName" placeholder="材料名" class="handle-input mr10"></el-input>
                <el-select v-model="query.type" placeholder="消耗率" class="handle-select mr10">
                    <el-option key="1" label="0-50%" value="1"></el-option>
                    <el-option key="2" label="50%-80%" value="2"></el-option>
                    <el-option key="3" label="80-100%" value="3"></el-option>
                </el-select>
                <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
                <el-button type="info" icon="el-icon-refresh" @click="doEmpty">清空</el-button>
            </div>
            <el-table
                    :data="UsageData"
                    border
                    class="table"
                    ref="multipleTable"
                    header-cell-class-name="table-header"
            >
                <el-table-column type="index" label="#" align="center"></el-table-column>
                <el-table-column prop="materialName" label="材料名"></el-table-column>
                <el-table-column prop="norm" label="规格"></el-table-column>
                <el-table-column prop="materialQuality" label="材质"></el-table-column>
                <el-table-column prop="materialUseQuantity" label="消耗量"></el-table-column>
                <el-table-column prop="materialTotalQuantity" label="总量"></el-table-column>
                <el-table-column prop="materialUnit" label="单位"></el-table-column>
                <el-table-column  label="消耗率（约值）">
                    <template scope="scope">
                        <el-tag type="danger" v-if="scope.row.materialUseQuantity/scope.row.materialTotalQuantity >= 0.8">
                            {{(scope.row.materialUseQuantity/scope.row.materialTotalQuantity).toFixed(2)*100+'%'}}
                        </el-tag>
                        <el-tag type="success" v-if="scope.row.materialUseQuantity/scope.row.materialTotalQuantity <= 0.5">
                            {{(scope.row.materialUseQuantity/scope.row.materialTotalQuantity).toFixed(2)*100+'%'}}
                        </el-tag>
                        <el-tag type="warning" v-if="scope.row.materialUseQuantity/scope.row.materialTotalQuantity > 0.5 && scope.row.materialUseQuantity/scope.row.materialTotalQuantity<0.8">
                            {{(scope.row.materialUseQuantity/scope.row.materialTotalQuantity).toFixed(2)*100+'%'}}
                        </el-tag>
                    </template>
                </el-table-column>

                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button
                                type="text"
                                icon="el-icon-lx-add"
                                class="green"
                                @click="handleAppend(scope.row)"
                        >追加</el-button>
                        <el-button
                                type="text"
                                icon="el-icon-edit"
                                @click="handldUpdateUse(scope.row)"
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

        <!-- 追加投入材料弹出框 -->
        <el-dialog title="追加材料" :visible.sync="appendVisible" width="30%">
            <el-form ref="appendForm" :rules="rules" :model="appendForm" label-width="80px">
                <el-form-item label="追加数量" prop="putQuantity">
                    <el-input :min="1" :max="appendForm.maxAddQuantity" type="number" placeholder="请输入材料追加数量" v-model="appendForm.putQuantity">
                        <template slot="append">{{appendForm.materialUnit}}</template>
                    </el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="appendVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveAppend">确 定</el-button>
            </span>
        </el-dialog>

        <el-dialog title="更新消耗量" :visible.sync="updateUseVisible" width="30%">
            <el-form ref="updateUseForm" :rules="rules" :model="updateUseForm" label-width="80px">
                <el-form-item label="消耗数量" prop="useQuantity">
                    <el-input :min="updateUseForm.materialUseQuantity" :max="updateUseForm.materialTotalQuantity" type="number" placeholder="请输入材料消耗数量" v-model="updateUseForm.useQuantity">
                        <template slot="append">{{updateUseForm.materialUnit}}</template>
                    </el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="updateUseVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateUseQuantity">确 定</el-button>
            </span>
        </el-dialog>

        <!--新投入一种材料弹出框-->
        <el-dialog title="投入材料" :visible.sync="addVisible" width="30%">
            <el-form ref="addForm" :rules="rules" :model="addForm" label-width="80px">
                <el-form-item label="材料信息" prop="materialId">
                    <el-cascader
                            style="width: 100%"
                            clearable
                            ref="selectMaterialName"
                            placeholder="可输入材料名称搜索，例如：水泥"
                            :options="optionsSearch"
                            @change="analysisIt"
                            v-model="addForm.materialId"
                            filterable></el-cascader>
                </el-form-item>
                <el-form-item label="投入数量" prop="putQuantity">
                    <el-input type="number" v-model="addForm.putQuantity" placeholder="请输入投入数量"></el-input>
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
    import { queryMaterialInCategory } from '../../api/materialStock';
    import { addUsage , getUsage  , appendMaterialTotal , updateUseQuantity } from '../../api/siteInformation'
    export default {
        name: 'siteUsageTable',
        data() {
            return {
                query: {
                    materialName: '',
                    type: '',
                    pageIndex: 1,
                    pageSize: 10
                },
                UsageData: [],
                appendVisible: false,
                pageTotal: 0,
                appendForm: {},
                rules: {
                    materialId: [{ required: true, message: '请选择投入材料', trigger: 'blur' }],
                    putQuantity: [{ required: true, message: '请输入材料投入数量', trigger: 'blur' }],
                    useQuantity: [{ required: true, message: '请输入材料消耗数量', trigger: 'blur' }],
                },
                addForm: {},
                addVisible: false,
                siteId: -1,
                optionsSearch: [],
                siteState: 0,
                updateUseVisible: false,
                updateUseForm: {},
            };
        },
        created() {
            this.getSiteId();
            this.getByLimitUsage();
        },
        methods: {
            // 获取数据
            getByLimitUsage() {
                this.$set(this.query, 'siteId', this.siteId);
                getUsage(this.query).then(res => {
                    if (res.code === '1001') {
                        this.UsageData = res.data;
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
                this.getByLimitUsage();
            },

            handldUpdateUse(row) {
                if (this.siteState === 1) {
                    this.$message.error('该工地项目已竣工');
                    return;
                }
                this.updateUseForm = row;
                this.updateUseVisible = true;
            },
            updateUseQuantity() {
                this.$refs.updateUseForm.validate(valid => {
                    if (valid) {
                        if (this.updateUseForm.useQuantity <= this.updateUseForm.materialUseQuantity) {
                            this.$message.error('该材料消耗数量必须大于原有消耗数量');
                            return;
                        }
                        if (this.updateUseForm.useQuantity > this.updateUseForm.materialTotalQuantity) {
                            this.$message.error('该材料消耗数量不能大于材料总量');
                            return;
                        }
                        updateUseQuantity(this.updateUseForm).then(res => {
                            if(res.code === '1001') {
                                this.updateUseVisible = false;
                                this.$message.success(res.msg);
                            } else if (res.code === '2001') {
                                this.$message.error(res.msg);
                            } else {//3001
                                this.$message.error(res.msg);
                                localStorage.removeItem('userName');
                                localStorage.removeItem('token');
                                this.$router.push('/login');
                            }
                            this.getByLimitUsage();
                        });
                    } else {
                        this.$message.error('更新材料消耗量失败');
                    }
                });
            },

            // 打开追加材料弹出框
            handleAppend(row) {
                if (this.siteState === 1) {
                    this.$message.error('该工地项目已竣工');
                    return;
                }
                this.appendForm = row;
                this.appendVisible = true;
            },
            // 保存追加信息
            saveAppend() {
                this.$refs.appendForm.validate(valid => {
                    if (valid) {
                        if (this.appendForm.putQuantity < 1) {
                            this.$message.error('追加材料数量不能小于1');
                            return;
                        }

                        if (this.appendForm.putQuantity > this.appendForm.maxAddQuantity) {
                            this.$message.error('该材料库存余量不足');
                            return;
                        }
                        appendMaterialTotal(this.appendForm).then(res => {
                            if(res.code === '1001') {
                                this.appendVisible = false;
                                this.$message.success(res.msg);
                            } else if (res.code === '2001') {
                                this.$message.error(res.msg);
                            } else {//3001
                                this.$message.error(res.msg);
                                localStorage.removeItem('userName');
                                localStorage.removeItem('token');
                                this.$router.push('/login');
                            }
                            this.getByLimitUsage();
                        });
                    } else {
                        this.$message.error('追加材料失败');
                    }
                });

            },

            //打开投入材料弹出框
            handleAdd() {
                if (this.siteState === 1) {
                    this.$message.error('该工地项目已竣工');
                    return;
                }
                this.addVisible = true;
                this.addForm = {};
                this.searchMaterialToCategory();
            },
            analysisIt(value) {// 获取下拉框选择值
                this.addForm.materialId = value[1];
            },//从服务器拉取下拉框可选择的数据
            searchMaterialToCategory() {
                let data = {"siteId": this.siteId};
                queryMaterialInCategory(data).then(res => {
                    if (res.code === '1001') {
                        this.optionsSearch = res.data;
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
            //保存新增信息
            saveAdd() {
                this.$set(this.addForm, 'siteId', this.siteId);
                this.$refs.addForm.validate(valid => {
                    if (this.addForm.putQuantity < 1) {
                        this.$message.error('追加材料数量不能小于1');
                        return;
                    }
                    if (valid) {
                        addUsage(this.addForm).then(res => {
                            if(res.code === '1001') {
                                this.addVisible = false;
                                this.$message.success(res.msg);
                            } else if (res.code === '2001') {
                                this.$message.error(res.msg);
                            } else {//3001
                                this.$message.error(res.msg);
                                localStorage.removeItem('userName');
                                localStorage.removeItem('token');
                                this.$router.push('/login');
                            }
                            this.getByLimitUsage();
                        });
                    } else {
                        this.$message.error('投入材料失败');
                    }
                });

            },
            getSiteId() {
                this.siteId = this.$route.query.siteId;
                this.siteState = this.$route.query.siteState;
                if (this.siteId === undefined || this.siteId <= 0) {
                    this.$router.go(-1);
                }

            },
            // 分页导航
            handlePageChange(val) {
                this.$set(this.query, 'pageIndex', val);
                this.getByLimitUsage();
            },

            // 情况搜索框内容
            doEmpty() {
                this.query = {
                    "materialName": '',
                    "type": '',
                    "pageIndex": 1,
                    "pageSize": 10
                };
            },
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
    .green {
        color: #67C23A;
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
</style>
